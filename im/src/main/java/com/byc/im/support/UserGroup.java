package com.byc.im.support;

import com.byc.im.entity.User;
import com.byc.im.support.pojo.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserGroup {
    public static List<User> USER=new ArrayList<>();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static User search(Channel channel){
        for (User user : USER) {
            if (user.getChannelId().equals(channel.id().toString())){
                return user;
            }
        }
        return null;
    }

    public static User search(Long id){
        for (User user : USER) {
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    /**绑定用户*/
    public static User bindUserChannel(Long userId,String channelId){
        for (User user : USER) {
            if (user.getId().equals(userId)){
                user.setChannelId(channelId);
                return user;
            }
        }
        return null;
    }

    public static User search(String name){
        for (User user : USER) {
            if (user.getUsername().equals(name)){
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        //顶掉
        User search = search(user.getUsername());
        if (search!=null){
            Channel channel = SocketChannelGroup.findChannel(search.getChannelId());
            try {
                if(channel!=null) {
                    removeUser(channel);
                    channel.disconnect().sync();
                }
            } catch (InterruptedException e) {
                log.error("处理顶下线异常",e);
            }
        }
        USER.add(user);
        Messages message = null;
        try {
            message = Messages.build(Messages.ONLINE, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            log.error("序列化异常",e);
        }
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(message.toString());
        SocketChannelGroup.send2All(textWebSocketFrame);
    }

    public static void removeUser(Channel channel) {
        User user = search(channel);
        USER.remove(user);
        Messages message = null;
        try {
            message = Messages.build(Messages.OFFLINE, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            log.error("序列化异常",e);
        }
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(message.toString());
        SocketChannelGroup.send2All(textWebSocketFrame);
    }
}
