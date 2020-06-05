package com.byc.im.support.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * Created by baiyc
 * 2020/6/3/003 16:38
 * Description：消息实体
 */
@Data
public class Messages {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private String code = "200";
    private String message = "success";
    private Payload payLoad;
    /**
     * type mean
     * p    私人消息
     * sys  系统消息
     * g    群聊消息
     * on   上线通知
     * off  离线通知
     * error错误反馈
     * Apply群聊验证
     * **/
    public final static String SYS="SYS";
    public final static String PVP="P";
    public final static String PVG="G";
    public final static String ONLINE="ON";
    public final static String OFFLINE="OFF";
    public final static String ERROR="ERROR";
    public final static String GROUP_APPLY="APPLY";


    @Data
    public static class Payload{
        private Long source;
        private Long target;
        private String type;
        private Object data;
    }

    public static Messages build(String type,Object data){
        Payload payload = new Payload();
        payload.setData(data);
        payload.setType(type);
        Messages messages = new Messages();
        messages.setPayLoad(payload);
        return messages;
    }

    public static Messages err(String message) {
        Messages messages = new Messages();
        messages.setCode("500");
        messages.setMessage(message);
        return messages;
    }

    @Override
    public String toString(){
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

}
