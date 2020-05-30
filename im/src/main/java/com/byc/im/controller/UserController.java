package com.byc.im.controller;

import com.byc.common.mvc.WebResult;
import com.byc.common.utils.AssertUtil;
import com.byc.im.entity.User;
import com.byc.im.service.UserService;
import com.byc.im.support.ChatGroup;
import com.byc.im.support.SocketChannelGroup;
import com.byc.im.support.UserGroup;
import com.byc.im.support.common.APPConfig;
import com.byc.im.support.pojo.GroupChat;
import com.byc.im.support.pojo.PayLoad;
import com.byc.im.utils.StateCode;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    APPConfig config;


    @RequestMapping("/")
    public String tologin(){
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(String username, String password, Model model) {
        User user = userService.login(username, password);
        UserGroup.addUser(user);
        user.build();
        model.addAttribute("address",config.getWebsocket().getAddr());
        model.addAttribute("user", user);
        return "index";
    }

    @RequestMapping("/getOnlineUser")
    @ResponseBody
    public WebResult getOnlineUser(){
        return WebResult.success(UserGroup.USER);
    }

    @RequestMapping("/createGroups")
    @ResponseBody
    public WebResult createGroups(@RequestBody GroupChat groupChat){
        AssertUtil.assertFalse(ChatGroup.isExist(groupChat.getGroupName()), StateCode.CODE_BUSINESS,"群聊名已存在");
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
        return WebResult.success(groupChat);
    }

    @RequestMapping("/joinGroups")
    @ResponseBody
    public WebResult joinGroups(String channelId,String groupName) {
        AssertUtil.assertTrue(ChatGroup.isExist(groupName),StateCode.CODE_BUSINESS,"邀请已失效");
        ChannelGroup channels = ChatGroup.getChatGroup(groupName);
        channels.add(SocketChannelGroup.findChannel(channelId));
        return WebResult.success("");
    }

}
