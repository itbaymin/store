package com.byc.api.listener;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * Created by baiyc
 * 2020/5/19/019 17:29
 * Description：
 */
@Data
public class MyEvent extends ApplicationEvent {
    private String messageId;

    public MyEvent(Object source,String messageId) {
        super(source);
        this.messageId = messageId;
    }
}
