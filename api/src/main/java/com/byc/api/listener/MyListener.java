package com.byc.api.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by baiyc
 * 2020/5/19/019 17:28
 * Description：
 */
@Component
public class MyListener  {
    private static Map<String,SseEmitter> sseEmitters = new Hashtable<>();

    public void addSseEmitters(String messageId, SseEmitter sseEmitter) {
        sseEmitters.put(messageId, sseEmitter);
    }

    @EventListener
    public void deployEventHandler(MyEvent payCompletedEvent) throws IOException {
        String messageId = payCompletedEvent.getMessageId();
        SseEmitter sseEmitter = sseEmitters.get(messageId);
        sseEmitter.send("支付成功");
        //sseEmitter.complete();
    }
}
