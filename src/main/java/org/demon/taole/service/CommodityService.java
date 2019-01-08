package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.demon.bean.PageData;
import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.pojo.Commodity;
import org.demon.taole.pojo.CommodityExample;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-18 14:12
 */
@Service
public class CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    public PageData<Commodity> select(CommodityQuery query) {
        CommodityExample example = new CommodityExample();
        CommodityExample.Criteria criteria = example.createCriteria();
        FunctionUtil.whenNonNullDo(criteria::andTaskIdEqualTo, query.taskId);
        if (StrUtil.isNotBlank(query.name)) {
            criteria.andNameLike(StrUtil.format("%{}%", query.name));
        }
        FunctionUtil.whenNonNullDo(example::setOrderByClause, query.orderBy);
        if (StrUtil.isNotBlank(query.platform)) {
            criteria.andPlatformEqualTo(query.platform);
        }
        criteria.andUpdatetimeGreaterThanOrEqualTo(DateUtil.lastMonth());
        criteria.andStatusEqualTo("1");
        PageData<Commodity> pageData = new PageData<>();
        pageData.count = commodityMapper.countByExample(example);
        example.setLimit(query.getLimit());
        example.setOffset(query.getOffset());
        pageData.list = commodityMapper.selectByExample(example);
        return pageData;
    }

    public Commodity select(Integer id) {
        return Optional.ofNullable(commodityMapper.selectByPrimaryKey(id)).orElseGet(Commodity::new);
    }

    public void update(Integer id, Integer sendPrice) {
        Commodity commodity = new Commodity();
        commodity.setId(id);
        commodity.setSendPrice(sendPrice);
        commodityMapper.updateByPrimaryKeySelective(commodity);
    }

    public void noSelect(Integer commodityId) {
        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        commodity.setStatus("0");
        commodityMapper.updateByPrimaryKeySelective(commodity);
    }

    /**
     * 更新商品平台
     */
    @Scheduled(cron = "30 1/5 * * * ?")
    public void updateCommodityPlatform() {
        Commodity commodity = new Commodity();
        commodity.setPlatform("1");
        CommodityExample example = new CommodityExample();
        example.createCriteria().andUrlLike("%product.suning.com%").andPlatformNotEqualTo("1");
        commodityMapper.updateByExampleSelective(commodity, example);
        commodity.setPlatform("2");
        example.clear();
        example.createCriteria().andUrlLike("%item.gome.com.cn%").andPlatformNotEqualTo("2");
        commodityMapper.updateByExampleSelective(commodity, example);
        commodity.setPlatform("3");
        example.clear();
        example.createCriteria().andUrlLike("%goods.kaola.com%").andPlatformNotEqualTo("3");
        commodityMapper.updateByExampleSelective(commodity, example);
        example.clear();
        commodity.setPlatform("4");
        example.createCriteria().andUrlLike("%www.amazon.cn%").andPlatformNotEqualTo("4");
        commodityMapper.updateByExampleSelective(commodity, example);
    }
}
