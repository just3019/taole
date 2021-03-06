package org.demon.taole.service;

import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.mapper.DemoMapper;
import org.demon.taole.pojo.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-05 11:32
 */
@Service
@CommonsLog
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public Demo get(Long id) {
        return demoMapper.selectByPrimaryKey(id);
    }

    public Demo save(Demo demo) {
        demoMapper.insertSelective(demo);
        return demo;
    }
}
