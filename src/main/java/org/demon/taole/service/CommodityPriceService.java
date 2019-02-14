package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.mapper.CommodityPriceMapper;
import org.demon.taole.pojo.Commodity;
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
     * 删除前一段时间的价格记录，
     * 剩下当天的不同价格
     */
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void deleteTask() {
        Date begin = DateUtil.offsetHour(new Date(), -4);
        Date end = DateUtil.offsetHour(new Date(), 0);
        CommodityExample commodityExample = new CommodityExample();
        commodityExample.createCriteria().andUpdatetimeLessThan(DateUtil.lastWeek());
        Optional.ofNullable(commodityMapper.selectByExample(new CommodityExample())).orElseGet(ArrayList::new)
                .forEach(a -> clearCommodityPrice(a.getId(), begin, end));
    }

    /**
     * 清理commodity_price表数据
     *
     * @param commodityId 商品id
     * @param begin       开始时间
     * @param end         结束时间
     */
    public void clearCommodityPrice(Integer commodityId, Date begin, Date end) {
        List<Map<String, Object>> list = commodityPriceMapper.select(commodityId, begin, end);
        list.forEach(b -> {
            //1.获取这个价格和这个价格的数量
            Long count = (Long) b.get("count");
            Integer price = (Integer) b.get("price");
            //2.获取这个价格前一天的记录，留下第一个记录
            CommodityPriceExample commodityPriceExample = new CommodityPriceExample();
            commodityPriceExample.createCriteria().andCommodityIdEqualTo(commodityId)
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
    }


    /**
     * 每个月1号清理一次价格记录，
     * 上个月相同的商品只留一个最低价
     */
    @Scheduled(cron = "10 10 0 1 * ?")
    public void deleteMonthTask() {
        Date begin = DateUtil.lastMonth();
        Date end = new Date();
        CommodityExample commodityExample = new CommodityExample();
        commodityExample.createCriteria().andUpdatetimeGreaterThan(begin);
        Optional.ofNullable(commodityMapper.selectByExample(new CommodityExample())).orElseGet(ArrayList::new)
                .forEach(i -> clearCommodityPrice(i.getId(), begin, end));
    }

}
