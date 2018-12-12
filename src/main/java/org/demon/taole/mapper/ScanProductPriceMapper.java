package org.demon.taole.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.demon.taole.pojo.ScanProductPrice;
import org.demon.taole.pojo.ScanProductPriceExample;

import java.util.List;

@Mapper
public interface ScanProductPriceMapper {
    long countByExample(ScanProductPriceExample example);

    int deleteByExample(ScanProductPriceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ScanProductPrice record);

    int insertSelective(ScanProductPrice record);

    List<ScanProductPrice> selectByExample(ScanProductPriceExample example);

    ScanProductPrice selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ScanProductPrice record, @Param("example") ScanProductPriceExample example);

    int updateByExample(@Param("record") ScanProductPrice record, @Param("example") ScanProductPriceExample example);

    int updateByPrimaryKeySelective(ScanProductPrice record);

    int updateByPrimaryKey(ScanProductPrice record);
}