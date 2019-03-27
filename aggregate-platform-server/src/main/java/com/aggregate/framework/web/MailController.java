package com.aggregate.framework.web;

import com.aggregate.framework.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/12/13.
 */

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     *
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public void sendMailMessage() {
       Message message = new Message();

        message.setMessageCode("MissingParameter");
        message.setMessageStatus("Failed");
        message.setCause("缺少参数,请确认");

        mailService.sendMessageMail(message, "测试消息通知", "message.ftl");
    }
}
