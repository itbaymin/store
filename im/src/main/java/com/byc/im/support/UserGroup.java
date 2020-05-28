package com.byc.im.support;

import com.byc.im.entity.User;
import com.byc.im.support.pojo.PayLoad;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserGroup {
    public static List<User> USER=new ArrayList<>();

    public static User search(Channel channel){
        for (User user : USER) {
            if (user.getChannelId().equals(channel.id().toString())){
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

    public static void addUser(User user){
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
        PayLoad payLoad = new PayLoad(PayLoad.ONLINE, user);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(payLoad.toString());
        SocketChannelGroup.send2All(textWebSocketFrame);
    }

    public static void removeUser(Channel channel){
        User search = search(channel);
        USER.remove(search);
        PayLoad payLoad = new PayLoad(PayLoad.OFFLINE, search);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(payLoad.toString());
        SocketChannelGroup.send2All(textWebSocketFrame);
    }
}
