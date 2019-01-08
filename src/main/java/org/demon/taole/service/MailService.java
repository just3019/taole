package org.demon.taole.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.taole.bean.Constants;
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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
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


    public void send(String subject, String content, Integer taskId, boolean html) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        Account account = accountMapper.selectByPrimaryKey(task.getUid());
        if (html) {
            sendHtml(subject, content, account.getEmail());
        } else {
            send(subject, content, account.getEmail());
        }
    }


    private void send(String subject, String content, String to) {
        Email email = new Email();
        try {
            email.setToAddress(to);
            email.setSubject(subject);
            email.setContent(content);
            SimpleMailMessage message = new SimpleMailMessage();
            if (!StrUtil.equals(to, recieverAdd)) {
                message.setTo(to, recieverAdd);
            } else {
                message.setTo(to);
            }
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
            email.setRetry(1);
            emailMapper.insertSelective(email);
        }
    }

    private void sendHtml(String subject, String content, String to) {
        Email email = new Email();
        try {
            email.setToAddress(to);
            email.setSubject(subject);
            email.setContent(content);
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderAdd);
            if (!StrUtil.equals(to, recieverAdd)) {
                helper.setTo(new String[]{to, recieverAdd});
            } else {
                helper.setTo(to);
            }
            helper.setSubject(subject);
            helper.setText(content, true);
            sender.send(message);
            log.info(StrUtil.format("\n发送html邮件成功：\n{}\n{}", subject, content));
            email.setStatus("1");
        } catch (Exception e) {
            email.setStatus("0");
            log.error(e);
        } finally {
            email.setRetry(1);
            emailMapper.insertSelective(email);
        }
    }

    String getEmailContent(String name, Integer price, String url, Integer commodityId, String platform,
                           Integer discount) {
        String asdUrl = Constants.convertAsd(url);
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>邮件</title></head><body><h1><img" +
                " src=\"http://res.luckygrra.com/resources/" + platform + ".ico\" width=\"50\" " +
                "height=\"50\"></h1><h2>" + name + "</h2><h3>当前价格：<span style=\"color: red\">" + price + "</span></h3" +
                "><h3>根据平台监控降价：<span style=\"color: red\">" + discount + "</span></h3><h3><a target=\"_blank\" " +
                "href=\"" + url + "\">商品地址</a></h3>" +
                "<h3><a target=\"_blank\" href=\"" + asdUrl + "\">asd</a></h3>" +
                "<h3><a target=\"_blank\" href=\"http://taole.luckygrra.com/web/stat/" + commodityId + "\">波动</a></h3" +
                "></body></html>";
    }

    /**
     * 每天1点删除当前时间一个月前的邮件
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void delete() {
        log.info("定时删除邮件");
        EmailExample example = new EmailExample();
        example.createCriteria().andCreatetimeLessThanOrEqualTo(DateUtil.lastMonth());
        emailMapper.deleteByExample(example);
    }


    /**
     * 每个5分钟轮训一次邮件是否都发送成功
     */
    @Scheduled(cron = "5 0/1 * * * ?")
    public void retry() {
        log.info("重发失败的邮件");
        EmailExample example = new EmailExample();
        example.createCriteria().andStatusEqualTo("0").andRetryLessThan(3);
        example.setLimit(100);
        Optional.ofNullable(emailMapper.selectByExample(example)).orElseGet(ArrayList::new).forEach(this::send);

    }

    /**
     * 轮询使用的发送邮件方法
     *
     * @param email {@link Email}
     */
    private void send(Email email) {
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
            log.error(e);
        } finally {
            emailMapper.updateByPrimaryKeySelective(update);
        }
    }


}
