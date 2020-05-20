package com.byc.im.global;

import com.byc.im.pojo.PayLoad;
import com.byc.im.pojo.User;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.List;

public class UserGroup {
    public static List<User> USER=new ArrayList<>();
    public static User search(Channel channel){
        User target = new User();
        target.setChannelId(channel.id().toString());
        for (User user : USER) {
            if (user.equals(target)){
                return user;
            }
        }
        return target;
    }
    public static User search(String name){
        for (User user : USER) {
            if (user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }
    public static void addUser(User user){
        User search = search(user.getName());
        if (search!=null){
            Channel channel = SocketChannelGroup.findChannel(search.getChannelId());
            try {
//                removeUser(channel);
                channel.disconnect().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
