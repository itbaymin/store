package com.byc.im.support;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SocketChannelGroup {
    private   static io.netty.channel.group.ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  static ConcurrentMap<String, ChannelId> ChannelMap=new ConcurrentHashMap();
    public  static void addChannel(Channel channel){
        GlobalGroup.add(channel);
        ChannelMap.put(channel.id().asShortText(),channel.id());
    }
    public static void removeChannel(Channel channel){
        GlobalGroup.remove(channel);
        ChannelMap.remove(channel.id().asShortText());

    }
    public static Channel findChannel(String id){
        ChannelId channelId = ChannelMap.get(id);
        if(channelId==null)
            return null;
        return GlobalGroup.find(channelId);
    }
    public static void send2All(TextWebSocketFrame tws){
        GlobalGroup.writeAndFlush(tws);
    }
}
