package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.demon.bean.PageData;
import org.demon.taole.bean.CommodityQuery;
import org.demon.taole.mapper.CommodityMapper;
import org.demon.taole.mapper.CommodityPriceMapper;
import org.demon.taole.pojo.Commodity;
import org.demon.taole.pojo.CommodityExample;
import org.demon.taole.pojo.CommodityPrice;
import org.demon.taole.pojo.CommodityPriceExample;
import org.demon.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private CommodityPriceMapper commodityPriceMapper;

    public PageData<Commodity> select(CommodityQuery query) {
        CommodityExample example = new CommodityExample();
        CommodityExample.Criteria criteria = example.createCriteria();
        FunctionUtil.whenNonNullDo(criteria::andTaskIdEqualTo, query.taskId);
        FunctionUtil.whenNonNullDo(a -> criteria.andNameLike(StrUtil.format("%{}%", a)), query.name);
        criteria.andCreatetimeGreaterThanOrEqualTo(DateUtil.lastMonth());
        PageData<Commodity> pageData = new PageData<>();
        pageData.count = commodityMapper.countByExample(example);
        example.setLimit(query.getLimit());
        example.setOffset(query.getOffset());
        pageData.list = commodityMapper.selectByExample(example);
        return pageData;
    }

    public PageData<CommodityPrice> select(Integer commodityId) {
        CommodityPriceExample example = new CommodityPriceExample();
        example.createCriteria().andCommodityIdEqualTo(commodityId)
                .andCreatetimeGreaterThanOrEqualTo(DateUtil.lastMonth());
        PageData<CommodityPrice> pageData = new PageData<>();
        pageData.count = commodityPriceMapper.countByExample(example);
        pageData.list = commodityPriceMapper.selectByExample(example);
        return pageData;
    }
}
