package org.demon.taole.service.suning;

import org.demon.taole.bean.Suning;
import org.demon.taole.mapper.SuningTaskMapper;
import org.demon.taole.pojo.SuningTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-14 10:45
 */
@Service
public class SuningService {

    @Autowired
    private SuningTaskMapper suningTaskMapper;

    public void addUrl(Suning suning) {
        SuningTask suningTask = new SuningTask();
        suningTask.setProductId(suning.id);
        suningTask.setUrl(suning.url);
        suningTaskMapper.insertSelective(suningTask);
    }



}
