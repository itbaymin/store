package com.byc.im.controller;

import com.byc.common.mvc.WebResult;
import com.byc.common.utils.AssertUtil;
import com.byc.im.entity.User;
import com.byc.im.service.IMService;
import com.byc.im.support.ChatGroup;
import com.byc.im.support.SocketChannelGroup;
import com.byc.im.support.UserGroup;
import com.byc.im.support.common.APPConfig;
import com.byc.im.support.pojo.GroupChat;
import com.byc.im.support.pojo.Messages;
import com.byc.im.utils.StateCode;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    IMService service;
    @Autowired
    APPConfig config;


    @RequestMapping("/")
    public String tologin(){
        return "login";
    }

    @PostMapping("/login")
    /**登陆*/
    public String doLogin(String username, String password, Model model) {
        User user = service.login(username, password);
        UserGroup.addUser(user);
        user.build();
        model.addAttribute("address",config.getWebsocket().getAddr());
        model.addAttribute("user", user);
        return "index";
    }

    @PostMapping("/register")
    @ResponseBody
    /**注册*/
    public WebResult doRegister(String username, String password) {
        return service.register(username, password);
    }

    @RequestMapping("/searchOnlineUser")
    @ResponseBody
    /**搜索用户*/
    public WebResult getOnlineUser(String keyWord){
        AssertUtil.assertTrue(StringUtils.isNotBlank(keyWord),StateCode.CODE_BUSINESS,"请输出关键字");
        List<User> users = UserGroup.USER.stream().filter(user -> user.getUsername().contains(keyWord)).collect(Collectors.toList());
        return WebResult.success(users);
    }

    @RequestMapping("/agreeeFriend")
    @ResponseBody
    /**搜索用户*/
    public WebResult agreeFriend(Long apply,Long agree){
        service.addFriend(apply,agree);
        return WebResult.success("");
    }

    @RequestMapping("/addSpecial")
    @ResponseBody
    /**搜索用户*/
    public WebResult addSpecial(Long from,Long to){
        return WebResult.success(service.addSpecial(from,to));
    }

    @RequestMapping("/delFriend")
    @ResponseBody
    /**搜索用户*/
    public WebResult delFriend(Long from,Long to){
        service.delFriend(from,to);
        return WebResult.success("");
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
        Messages message = Messages.build(Messages.GROUP_APPLY, groupChat.toString());
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(message.toString());
        //发送信息出去
        group.writeAndFlush(textWebSocketFrame);
        //清空
        group.clear();
        group.add(SocketChannelGroup.findChannel(groupChat.getCreator()));
        //只保留创建者
        members.add(groupChat.getCreator());
        ChatGroup.insertChatGroupMap(1L,group);
        return WebResult.success(groupChat);
    }

    @RequestMapping("/joinGroups")
    @ResponseBody
    public WebResult joinGroups(String channelId,String groupName) {
        AssertUtil.assertTrue(ChatGroup.isExist(groupName),StateCode.CODE_BUSINESS,"邀请已失效");
        ChannelGroup channels = ChatGroup.getChatGroup(1L);
        channels.add(SocketChannelGroup.findChannel(channelId));
        return WebResult.success("");
    }

}
