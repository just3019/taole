package org.demon.taole.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.demon.taole.pojo.ScanProduct;
import org.demon.taole.pojo.ScanProductExample;

public interface ScanProductMapper {
    long countByExample(ScanProductExample example);

    int deleteByExample(ScanProductExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ScanProduct record);

    int insertSelective(ScanProduct record);

    List<ScanProduct> selectByExample(ScanProductExample example);

    ScanProduct selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ScanProduct record, @Param("example") ScanProductExample example);

    int updateByExample(@Param("record") ScanProduct record, @Param("example") ScanProductExample example);

    int updateByPrimaryKeySelective(ScanProduct record);

    int updateByPrimaryKey(ScanProduct record);
}