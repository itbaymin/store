package com.byc.im.service;

import com.byc.common.mvc.WebResult;
import com.byc.common.utils.AssertUtil;
import com.byc.im.entity.Message;
import com.byc.im.entity.User;
import com.byc.im.repository.UserRepository;
import com.byc.im.support.SocketChannelGroup;
import com.byc.im.support.UserGroup;
import com.byc.im.support.pojo.Messages;
import com.byc.im.utils.MongoHelper;
import com.byc.im.utils.StateCode;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by baiyc
 * 2020/5/26/026 17:49
 * Description：用户业务
 */
@Service
public class IMService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoHelper mongoHelper;

    /**登陆*/
    public User login(String username,String password){
        User user = mongoTemplate.findOne(query(where("username").is(username)), User.class);
        AssertUtil.assertNotNull(user, StateCode.CODE_BUSINESS,"用户不存在");
        AssertUtil.assertTrue(password.equals(user.getPassword()),StateCode.CODE_BUSINESS,"密码错误");
        return user;
    }

    /**查询指定用户的聊天记录*/
    public List<Message> findMessages(Long send,Long recive){
        Criteria criteria = new Criteria().orOperator(Criteria.where("send").is(send).and("recive").is(recive),
                Criteria.where("recive").is(send).and("send").is(recive));
        Query query = query(criteria);
        return mongoTemplate.find(query,Message.class);
    }

    /**保存消息
     * @param user
     * @param jsonNode
     * @param payLoad */
    public void saveMessage(Message.From user, Object jsonNode, Messages.Payload payLoad){
        Message message = Message.build(user, jsonNode, payLoad);
        //更新用户聊天历史
        User target = UserGroup.search(payLoad.getTarget());
        target.updataFriend(jsonNode,payLoad.getSource());
        message.setId(mongoHelper.getNextSequence(MongoHelper.Collection.MESSAGE));
        mongoTemplate.save(message);
    }

    /**用户注册*/
    public WebResult register(String username, String password) {
        User user = mongoTemplate.findOne(query(where("username").is(username)), User.class);
        AssertUtil.assertNull(user,StateCode.CODE_BUSINESS,"用户名已存在");
        user = new User();
        user.setId(mongoHelper.getNextSequence(MongoHelper.Collection.USER));
        user.setUsername(username);
        user.setPassword(password);
        int random = RandomUtils.nextInt(1, 4);
        user.setHeadImg(random);
        mongoTemplate.save(user);
        return WebResult.success("");
    }

    /**添加好友*/
    public void addFriend(Long apply, Long agree) {
        User send = UserGroup.search(apply);
        User target = UserGroup.search(agree);
        AssertUtil.assertTrue(send!=null && target!=null,StateCode.CODE_BUSINESS,"用户不存在");
        send.addFriend(target);
        target.addFriend(send);
        //发送消息
        Messages message1 = Messages.build(Messages.AGREE_FRIEND,apply,agree,send);
        p2p(message1);
        Messages message2 = Messages.build(Messages.AGREE_FRIEND, agree, apply, target);
        p2p(message2);
    }

    private void p2p(Messages messages){
        Long targetId = messages.getPayLoad().getTarget();
        String channelId = UserGroup.search(targetId).getChannelId();
        Channel channel = SocketChannelGroup.findChannel(channelId);
        if (channel!=null){
            channel.writeAndFlush(new TextWebSocketFrame(messages.toString()));
        }
    }

    /**更新用户*/
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**添加特别关注*/
    public User addSpecial(Long from, Long to) {
        User user1 = UserGroup.search(from);
        User user2 = userRepository.findById(to).get();
        user1.addFavorite(user2);
        return user2;
    }

    public void delFriend(Long from, Long to) {
        User user1 = UserGroup.search(from);
        user1.delFriend(to);
    }
}
