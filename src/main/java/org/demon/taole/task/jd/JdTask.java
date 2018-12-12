package org.demon.taole.task.jd;

import lombok.extern.apachecommons.CommonsLog;
import org.demon.pool.ExecutorPool;
import org.demon.taole.service.ScanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-12 10:39
 */
@Component
@CommonsLog
public class JdTask {

    @Autowired
    private ScanProductService scanProductService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void jdQueryListTask() {
        ExecutorPool.getInstance().execute(() -> new JdQueryListTask(scanProductService).run());
    }
}
