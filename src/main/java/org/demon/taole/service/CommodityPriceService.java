package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.mapper.CommodityPriceMapper;
import org.demon.taole.pojo.CommodityExample;
import org.demon.taole.pojo.CommodityPrice;
import org.demon.taole.pojo.CommodityPriceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommodityPriceService {

    @Autowired
    private CommodityPriceMapper commodityPriceMapper;
    @Autowired
    private CommodityMapper commodityMapper;


    public List<CommodityPrice> select(Integer commodityId) {
        CommodityPriceExample example = new CommodityPriceExample();
        example.createCriteria().andCommodityIdEqualTo(commodityId);
        return Optional.ofNullable(commodityPriceMapper.selectByExample(example)).orElseGet(ArrayList::new);
    }

    /**
     * 删除前一天价格记录，
     * 剩下当天的不同价格
     */
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void deleteTask() {
        Date begin = DateUtil.beginOfDay(DateUtil.yesterday());
        Date end = DateUtil.endOfDay(DateUtil.yesterday());
        CommodityExample commodityExample = new CommodityExample();
        Optional.ofNullable(commodityMapper.selectByExample(new CommodityExample())).orElseGet(ArrayList::new)
                .forEach(a -> {
                    List<Map<String, Object>> list = commodityPriceMapper.select(a.getId(), begin, end);
                    list.forEach(b -> {
                        //1.获取这个价格和这个价格的数量
                        Long count = (Long) b.get("count");
                        Integer price = (Integer) b.get("price");
                        //2.获取这个价格前一天的记录，留下第一个记录
                        CommodityPriceExample commodityPriceExample = new CommodityPriceExample();
                        commodityPriceExample.createCriteria().andCommodityIdEqualTo(a.getId())
                                .andPriceEqualTo(price).andCreatetimeBetween(begin, end);
                        commodityPriceExample.setLimit(Math.toIntExact(count - 1));
                        commodityPriceExample.setOrderByClause(" id desc");
                        List<Long> commodityPriceIds = Optional.ofNullable(commodityPriceMapper
                                .selectByExample(commodityPriceExample)).orElseGet(ArrayList::new).stream()
                                .map(CommodityPrice::getId).collect(Collectors.toList());
                        if (commodityPriceIds.size() == 0) {
                            return;
                        }
                        //3.根据id列表删除
                        CommodityPriceExample deleteExample = new CommodityPriceExample();
                        deleteExample.createCriteria().andIdIn(commodityPriceIds);
                        commodityPriceMapper.deleteByExample(deleteExample);
                    });
                });
    }

}
