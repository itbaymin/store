package com.byc.im.vo;

import com.byc.im.entity.Message;
import lombok.Data;

/**
 * Created by baiyc
 * 2020/6/6/006 17:00
 * Description：
 */
@Data
public class MessageVO {
    private Long id;
    private Long send;
    private Long recive;
    private Data data;
    private String createTime;

    @lombok.Data
    public static class Data{
        private String headImg;
        private String content;
    }

    public static MessageVO of(Message message){
        Data data = new Data();
        data.setHeadImg(message.getHeadImg());
        data.setContent(message.getContent());
        MessageVO vo = new MessageVO();
        vo.setId(message.getId());
        vo.setSend(message.getSend());
        vo.setRecive(message.getRecive());
        vo.setCreateTime(message.getCreateTime().toString());
        vo.setData(data);
        return vo;
    }
}
