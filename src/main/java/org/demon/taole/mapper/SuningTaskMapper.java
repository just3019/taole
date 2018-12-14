package org.demon.taole.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.demon.taole.pojo.SuningTask;
import org.demon.taole.pojo.SuningTaskExample;

import java.util.List;

@Mapper
public interface SuningTaskMapper {
    long countByExample(SuningTaskExample example);

    int deleteByExample(SuningTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuningTask record);

    int insertSelective(SuningTask record);

    List<SuningTask> selectByExample(SuningTaskExample example);

    SuningTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuningTask record, @Param("example") SuningTaskExample example);

    int updateByExample(@Param("record") SuningTask record, @Param("example") SuningTaskExample example);

    int updateByPrimaryKeySelective(SuningTask record);

    int updateByPrimaryKey(SuningTask record);
}