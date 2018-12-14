package org.demon.taole;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@CommonsLog
public class TaoleApplicationTests {

    @Value("${spring.mail.sender}")
    private String senderAddress;

    @Autowired
    private JavaMailSender sender;

    @Test
    public void sendMail() {
        log.info(senderAddress);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("linhao.song@welian.com");
        message.setFrom(senderAddress);
        message.setSubject("监控消息");
        message.setText("监控最终内容");
        sender.send(message);
    }


}
