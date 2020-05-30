package com.byc.im.controller;

import com.byc.im.entity.Message;
import com.byc.im.entity.User;
import com.byc.im.repository.MessageRepository;
import com.byc.im.repository.UserRepository;
import com.byc.im.utils.MongoHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by baiyc
 * 2020/5/25/025 19:46
 * Description：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoHelper mongoHelper;

    @Test
    public void test(){
        User user = new User();
        user.setId(1L);
        user.setUsername("bai1");
        user.setPassword("byc123");
        user.setHeadImg("background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)");

        User.Friend friend1 = new User.Friend();
        friend1.setUsername("bai2");
        friend1.setId(2L);
        friend1.setHeadImg("background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)");
        friend1.setUnReadNum(1);
        friend1.setContent("hello！");
        friend1.setTime("下午2点");
        User.Group group1 = new User.Group();
        group1.setName("家人");
        group1.setFlag(1);
        group1.setCreateTime(LocalDateTime.now());
        group1.setFriends(Arrays.asList(friend1));

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("bai2");
        user2.setPassword("byc123");
        user2.setHeadImg("background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)");
        User.Friend friend2 = new User.Friend();
        friend2.setUsername("bai1");
        friend2.setId(1L);
        friend2.setHeadImg("background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)");
        friend2.setUnReadNum(1);
        friend2.setContent("你好！");
        friend2.setTime("下午2点");
        User.Group group2 = new User.Group();
        group1.setName("家人");
        group1.setFlag(1);
        group2.setCreateTime(LocalDateTime.now());
        group2.setFriends(Arrays.asList(friend2));


        user.setGroups(Arrays.asList(group1));
        userRepository.save(user);
        user2.setGroups(Arrays.asList(group2));
        userRepository.save(user2);
    }
    @Test
    public void test1(){
        userRepository.deleteById(1L);
    }

    @Test
    public void test2(){
        Optional<User> byId = userRepository.findById(1L);
        System.out.println(byId.orElse(new User()));
    }

    @Test
    public void test3(){
        Message message = new Message();
        message.setId(mongoHelper.getNextSequence(MongoHelper.Collection.USER));
        message.setContent("hello");
        message.setFrom(Message.From.USER);
        message.setRecive(1L);
        message.setSend(2L);
        message.setCreateTime(LocalDateTime.now());
        message.setType(Message.MsgType.TXT);
        messageRepository.save(message);
    }
    @Test
    public void test4(){
        //Optional<Message> byId = messageRepository.findById(1L);
        //System.out.println(byId.get());
        Query query = new Query();
        query.addCriteria(new Criteria().where("create_time").gt(LocalDateTime.of(2020,5,26,10,0)));
        List<Message> messages = mongoTemplate.find(query, Message.class);
        System.out.println(messages);
    }

}