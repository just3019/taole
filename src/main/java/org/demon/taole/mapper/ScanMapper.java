package org.demon.taole.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.demon.taole.pojo.Scan;
import org.demon.taole.pojo.ScanExample;

import java.util.List;

@Mapper
public interface ScanMapper {
    long countByExample(ScanExample example);

    int deleteByExample(ScanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Scan record);

    int insertSelective(Scan record);

    List<Scan> selectByExample(ScanExample example);

    Scan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Scan record, @Param("example") ScanExample example);

    int updateByExample(@Param("record") Scan record, @Param("example") ScanExample example);

    int updateByPrimaryKeySelective(Scan record);

    int updateByPrimaryKey(Scan record);

    void insertBatch(List<Scan> scans);
}