package com.byc.im.controller;

import com.byc.common.mvc.WebResult;
import com.byc.im.entity.Message;
import com.byc.im.service.IMService;
import com.byc.im.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("message")
/**消息控制器*/
public class MessageController {
    @Autowired
    IMService IMService;

    @RequestMapping("list")
    public WebResult messages(Long send,Long recive){
        List<Message> messages = IMService.findMessages(send, recive);
        return WebResult.success(messages.stream().map(MessageVO::of).collect(Collectors.toList()));
    }

    @RequestMapping("test")
    public String test(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "succ";
    }
}
