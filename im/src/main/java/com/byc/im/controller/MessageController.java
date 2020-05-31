package com.byc.im.controller;

import com.byc.common.mvc.WebResult;
import com.byc.im.entity.Message;
import com.byc.im.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("message")
/**消息控制器*/
public class MessageController {
    @Autowired
    UserService userService;

    @RequestMapping("list")
    public WebResult messages(Long send,Long recive){
        List<Message> messages = userService.findMessages(send, recive);
        return WebResult.success(messages);
    }
}
