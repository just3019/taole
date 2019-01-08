package org.demon.taole.service;

import lombok.extern.apachecommons.CommonsLog;
import org.demon.TaoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * desc:
 *
 * @author demon
 * @date 2019-01-08 16:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaoleApplication.class})//classes 指定启动类,加载环境
@Transactional
@CommonsLog
public class CommodityServiceTest {

    @Autowired
    private CommodityService commodityService;

    @Test
    public void test() {
        commodityService.updateCommodityPlatform();
    }
}
