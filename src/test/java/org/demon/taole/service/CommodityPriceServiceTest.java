package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.TaoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaoleApplication.class})//classes 指定启动类,加载环境
//@Transactional
@CommonsLog
public class CommodityPriceServiceTest {

    @Autowired
    private CommodityPriceService commodityPriceService;

    @Test
    public void test() throws InterruptedException {
        long t = System.currentTimeMillis();
        Date begin = DateUtil.date(1546272000000L);
        Date end = DateUtil.date(1548950400000L);
        commodityPriceService.deleteTask(begin, end);
        Thread.sleep(1000);
        log.error(System.currentTimeMillis() - t);
    }

}
