package com.byc.im.entity;

import com.byc.im.support.pojo.Messages;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by baiyc
 * 2020/5/25/025 19:43
 * Description：消息
 */
@Data
@Document("message")
public class Message implements Serializable {
    private Long id;
    private From from;
    private Long send;
    @Field("head_img")
    private String headImg;
    private Long recive;
    private MsgType type;
    private String content;
    @Field("create_time")
    private LocalDateTime createTime;

    public static Message build(From from, Object data, Messages.Payload payLoad) {
        Message message = new Message();
        message.setFrom(from);
        message.setSend(payLoad.getSource());
        message.setHeadImg(((Map)data).get("headImg").toString());
        message.setContent(((Map)data).get("content").toString());
        message.setRecive(payLoad.getTarget());
        message.setType(MsgType.TXT);
        message.setCreateTime(LocalDateTime.now());
        return message;
    }

    public enum MsgType{
        TXT
    }
    public enum From{
        USER,GROUP
    }
}
