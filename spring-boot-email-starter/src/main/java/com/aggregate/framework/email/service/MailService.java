package com.aggregate.framework.email.service;

import com.aggregate.framework.email.components.MailAuthenticator;
import com.aggregate.framework.email.components.SimpleMail;
import com.aggregate.framework.email.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {


    /**
     * 发送邮件的props文件
     */
    private final transient Properties props = System.getProperties();
    /**
     * 邮件服务器登录验证
     */
    private transient MailAuthenticator authenticator;
    /**
     * 邮箱session
     */
    private transient Session session;

    @Autowired
    private EmailConfig emailConfig;

    @PostConstruct
    private void init() {
        // 初始化props
        props.setProperty("mail.transport.protocol", emailConfig.getProtocol());
        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.host", emailConfig.getHost());
        // 需要请求认证
        props.setProperty("mail.smtp.auth", emailConfig.getAuth());
        props.setProperty("mail.smtp.port", emailConfig.getPort());
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.port",  emailConfig.getPort());
        // 验证
        authenticator = new MailAuthenticator(emailConfig.getUsername(), emailConfig.getPassword());
        // 创建session
        session = Session.getInstance(props, authenticator);
        session.setDebug(true);
    }
    /**
     * 发送邮件
     *
     * @param recipient
     *            收件人邮箱地址
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(String recipient, String subject, Object content) throws Exception {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(emailConfig.getFrom()));
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");
        // 发送
        Transport.send(message);
    }

    /**
     * 群发邮件
     *
     * @param recipients
     *            收件人
     * @param subject
     *            主题
     * @param content
     *            内容
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(List<String> recipients, String subject, Object content)
            throws AddressException, MessagingException {
        // 创建mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发信人
        message.setFrom(new InternetAddress(emailConfig.getFrom()));
        // 设置收件人们
        final int num = recipients.size();
        InternetAddress[] addresses = new InternetAddress[num];
        for (int i = 0; i < num; i++) {
            addresses[i] = new InternetAddress(recipients.get(i));
        }
        message.setRecipients(Message.RecipientType.TO, addresses);
        // 设置主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setContent(content.toString(), "text/html;charset=utf-8");
        // 发送
        Transport.send(message);
    }

    /**
     * 发送邮件
     *
     * @param recipient
     *            收件人邮箱地址 @param mail 邮件对象 @throws AddressException @throws
     *            MessagingException @throws
     */
    public void send(String recipient, SimpleMail mail) throws Exception {
        send(recipient, mail.getSubject(), mail.getContent());
    }

    /**
     * 群发邮件
     *
     * @param recipients
     *            收件人
     * @param mail
     *            邮件对象
     * @throws AddressException
     * @throws MessagingException
     */
    public void send(List<String> recipients, SimpleMail mail) throws AddressException, MessagingException {
        send(recipients, mail.getSubject(), mail.getContent());
    }

}
