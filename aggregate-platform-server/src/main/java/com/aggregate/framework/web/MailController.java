package com.aggregate.framework.web;

import com.aggregate.framework.AggregateCreditClient;
import com.aggregate.framework.dto.RequestClientDto;
import com.aggregate.framework.email.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13.
 */

@RestController
@Slf4j
public class MailController  {

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


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {
        String activeProfile = SpringApplicationContext.getActiveProfile();
        AggregateCreditClient client = AggregateCreditClient.getInstance(activeProfile);


        Map<String,String> map = new HashMap<>(2);
        map.put("name","马涛");
        map.put("identityId","610429199009085178");

        RequestClientDto requestClientDto = new RequestClientDto();
        requestClientDto.setOuterId("test_0001");
        requestClientDto.setServerName("guozhen.queryCredit");
        requestClientDto.setDataMap(map);

        String str = client.sendHttpRequest(requestClientDto);

        log.debug("response values is : {}",str);
    }
}
