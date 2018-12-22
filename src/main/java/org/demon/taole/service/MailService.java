package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.mapper.AccountMapper;
import org.demon.taole.mapper.EmailMapper;
import org.demon.taole.mapper.TaskMapper;
import org.demon.taole.pojo.Account;
import org.demon.taole.pojo.Email;
import org.demon.taole.pojo.EmailExample;
import org.demon.taole.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private AccountMapper accountMapper;


    public void send(String subject, String content, Integer taskId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        Account account = accountMapper.selectByPrimaryKey(task.getUid());
        send(subject, content, account.getEmail());
    }


    public void send(String subject, String content, String to) {
        Email email = new Email();
        try {
            email.setToAddress(to);
            email.setSubject(subject);
            email.setContent(content);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(senderAdd);
            message.setSubject(subject);
            message.setText(content);
            sender.send(message);
            log.info(StrUtil.format("\n发送邮件成功：\n{}\n{}", subject, content));
            email.setStatus("1");
        } catch (Exception e) {
            email.setStatus("0");
            log.error(e);
        } finally {
            emailMapper.insertSelective(email);
        }
    }

    /**
     * 每天1点删除当前时间一个月前的邮件
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void delete() {
        log.info("定时删除邮件");
        EmailExample example = new EmailExample();
        example.createCriteria().andCreatetimeLessThanOrEqualTo(DateUtil.lastMonth());
        emailMapper.deleteByExample(example);
    }


    /**
     * 每个5分钟轮训一次邮件是否都发送成功
     */
    @Scheduled(cron = "5 0/5 * * * ?")
    public void retry() {
        log.info("重发失败的邮件");
        EmailExample example = new EmailExample();
        example.createCriteria().andStatusEqualTo("0").andRetryLessThan(3);
        example.setLimit(100);
        Optional.ofNullable(emailMapper.selectByExample(example)).orElseGet(ArrayList::new).forEach(this::send);

    }

    public void send(Email email) {
        Email update = new Email();
        update.setId(email.getId());
        try {
            update.setRetry(email.getRetry() + 1);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getToAddress());
            message.setFrom(senderAdd);
            message.setSubject(email.getSubject());
            message.setText(email.getContent());
            sender.send(message);
            log.info(StrUtil.format("\n发送邮件成功：\n{}\n{}", email.getSubject(), email.getContent()));
            update.setStatus("1");
        } catch (Exception e) {
            update.setStatus("0");
            log.error(e);
        } finally {
            emailMapper.updateByPrimaryKeySelective(email);
        }
    }


}
