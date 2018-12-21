package org.demon.taole.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * desc: 发送邮件
 *
 * @author demon
 * @date 2018-12-14 16:12
 */
@Service
@CommonsLog
public class MailService {

    @Value("${spring.mail.sender}")
    private String senderAdd;

    @Value("${spring.mail.reciever}")
    private String recieverAdd;

    @Autowired
    private JavaMailSender sender;

    public void send(String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recieverAdd);
        message.setFrom(senderAdd);
        message.setSubject(subject);
        message.setText(content);
        sender.send(message);
        log.info(StrUtil.format("\n发送邮件成功：\n{}\n{}", subject, content));
    }
}
