package com.aggregate.framework.open.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocketMessageController {



    @PostMapping(value = "/sendMessageToUser")
    public void sendMessage() {

    }

}