package org.demon.taole.task.suning;

import lombok.extern.apachecommons.CommonsLog;
import org.demon.pool.ExecutorPool;
import org.demon.pool.SeleniumDownloader;
import org.demon.taole.mapper.SuningTaskMapper;
import org.demon.taole.pojo.SuningTaskExample;
import org.demon.taole.service.ScanProductService;
import org.demon.taole.service.suning.SuningPageProcesser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.Optional;

/**
 * desc:
 *
 * @author demon
 * @date 2018-12-14 11:09
 */
@Component
@CommonsLog
public class SuningTask {

    @Autowired
    private SuningTaskMapper suningTaskMapper;
    @Autowired
    private ScanProductService scanProductService;

    @Scheduled(cron = "0/60 * * * * ?")
    public void snUrlTask() {
        SuningTaskExample example = new SuningTaskExample();
        example.createCriteria().andStatusEqualTo("1");
        Optional.ofNullable(suningTaskMapper.selectByExample(example)).orElseGet(ArrayList::new)
                .forEach(a -> ExecutorPool.getInstance().execute(() ->
                        Spider.create(new SuningPageProcesser(a, scanProductService))
                                .addUrl(a.getUrl())
                                .setDownloader(new SeleniumDownloader()).run()));
    }
}
