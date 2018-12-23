package org.demon.taole.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.demon.taole.pojo.CommodityPrice;
import org.demon.taole.pojo.CommodityPriceExample;

@Mapper
public interface CommodityPriceMapper {
    long countByExample(CommodityPriceExample example);

    int deleteByExample(CommodityPriceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CommodityPrice record);

    int insertSelective(CommodityPrice record);

    List<CommodityPrice> selectByExample(CommodityPriceExample example);

    CommodityPrice selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CommodityPrice record,
                                 @Param("example") CommodityPriceExample example);

    int updateByExample(@Param("record") CommodityPrice record, @Param("example") CommodityPriceExample example);

    int updateByPrimaryKeySelective(CommodityPrice record);

    int updateByPrimaryKey(CommodityPrice record);

    void insertByBatch(List<CommodityPrice> commodityPrices);

    List<Map<String, Object>> select(Integer id, Date begin, Date end);
}