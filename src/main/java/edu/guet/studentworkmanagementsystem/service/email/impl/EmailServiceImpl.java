package edu.guet.studentworkmanagementsystem.service.email.impl;

import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.service.email.EmailService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    @Qualifier("emailThreadPool")
    private ThreadPoolTaskExecutor emailThreadPool;
    @Override
    public void sendEmail(String to, String code) {
        asyncSendEmail(to, code);
    }
    public void asyncSendEmail(String to, String code) {
        CompletableFuture.runAsync(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(from);
            message.setSubject("密码找回 --- Student Work Management System");
            String buffer = "验证码："
                    + code
                    + "\n"
                    + "请勿回复此邮件";
            message.setText(buffer);
            try {
                mailSender.send(message);
            } catch (MailException exception) {
                throw new ServiceException(ServiceExceptionEnum.EMAIL_SEND_FAILURE);
            }
        }, emailThreadPool);
    }
}
