package com.byc.im.controller;

import com.byc.im.global.ChatGroup;
import com.byc.im.global.SocketChannelGroup;
import com.byc.im.global.UserGroup;
import com.byc.im.pojo.GroupChat;
import com.byc.im.pojo.PayLoad;
import com.byc.im.pojo.User;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @RequestMapping("/")
    public String tologin(){
        return "v1/login";
    }
    @RequestMapping("/doLogin")
    public String doLogin(String name, Map<String,Object> paramMap)
    {
        paramMap.put("name",name);
        return "v1/chatPage";
    }
    @RequestMapping("/getOnlineUser")
    @ResponseBody
    public List<User> getOnlineUser(){
        return UserGroup.USER;
    }

    @RequestMapping("/createGroups")
    @ResponseBody
    public Map createGroups(@RequestBody GroupChat groupChat){
        HashMap<String, Object> map = new HashMap<>();
        if (ChatGroup.isExist(groupChat.getGroupName())){
            map.put("code","300");
            map.put("data","群聊名已存在");
            return map;
        }else{
        List<String> members = groupChat.getGroupMembers();
        DefaultChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        for (String member : members) {
            group.add(SocketChannelGroup.findChannel(member));
        }
        //广播邀请信息
        members.clear();
        PayLoad payLoad = new PayLoad(PayLoad.GROUP_APPLY,groupChat.toString());
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(payLoad.toString());
        //发送信息出去
        group.writeAndFlush(textWebSocketFrame);
        //清空
        group.clear();
        group.add(SocketChannelGroup.findChannel(groupChat.getCreator()));
        //只保留创建者
        members.add(groupChat.getCreator());
        ChatGroup.insertChatGroupMap(groupChat.getGroupName(),group);
        map.put("code","200");
        map.put("data",groupChat);
        return map;
        }
    }
    @RequestMapping("/joinGroups")
    @ResponseBody
    public Map joinGroups(String channelId,String groupName)
    {
        HashMap<String, Object> map = new HashMap<>();
        if (ChatGroup.isExist(groupName)){
            map.put("code","200");
            ChannelGroup channels = ChatGroup.getChatGroup(groupName);
            channels.add(SocketChannelGroup.findChannel(channelId));
            map.put("data","已成功加入");
        }else {
            map.put("code","300");
            map.put("data","邀请已失效");
        }
        return map;
    }

}
