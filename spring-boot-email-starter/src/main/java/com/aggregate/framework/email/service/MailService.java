package com.aggregate.framework.email.service;


import com.aggregate.framework.email.config.EmailConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Autowired
    private EmailConfig emailConfig;


    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        ((JavaMailSenderImpl) mailSender).setHost(emailConfig.getHost());
        ((JavaMailSenderImpl) mailSender).setPassword(emailConfig.getPassword());
        ((JavaMailSenderImpl) mailSender).setPort(Integer.parseInt(emailConfig.getPort()));
        ((JavaMailSenderImpl) mailSender).setProtocol(emailConfig.getProtocol());
        ((JavaMailSenderImpl) mailSender).setDefaultEncoding(emailConfig.getEncoding());
        ((JavaMailSenderImpl) mailSender).setUsername(emailConfig.getUsername());
    }

    /**
     * @param params       发送邮件的主题对象 object
     * @param title        邮件标题
     * @param templateName 模板名称
     */
    public void sendMessageMail(Object params, String to,String title, String templateName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getFrom());
            helper.setTo(InternetAddress.parse("523506791@qq.com"));
            helper.setSubject("【" + title + "-" + LocalDate.now() + " " + LocalTime.now().withNano(0) + "】");
            Map<String, Object> model = new HashMap<>();
            model.put("params", params);
            try {
                Template template = configurer.getConfiguration().getTemplate(templateName);
                try {
                    String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                    helper.setText(text, true);
                    mailSender.send(mimeMessage);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
