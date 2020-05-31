package com.byc.im.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    public enum MsgType{
        TXT
    }
    public enum From{
        USER,GROUP
    }
}
