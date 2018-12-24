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
        FunctionUtil.whenNonNullDo(a -> criteria.andNameLike(StrUtil.format("%{}%", a)), query.name);
        FunctionUtil.whenNonNullDo(example::setOrderByClause, query.orderBy);
        criteria.andCreatetimeGreaterThanOrEqualTo(DateUtil.lastMonth());
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

}
