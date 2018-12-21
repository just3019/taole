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
 * @date 2018-12-21 16:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaoleApplication.class})//classes 指定启动类,加载环境
@Transactional
@CommonsLog
public class MailServiceTest {

    @Autowired
    private MailService mailService;


    @Test
    public void test(){
        mailService.send("监控反馈", "https://item.jd.com/21968806158.html");
    }
}
