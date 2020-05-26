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
        user.setId(mongoHelper.getNextSequence(MongoHelper.Collection.USER));
        user.setUsername("白永程");
        user.setPassword("byc123");
        user.setHeadImg("http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg");
        user.setFriends(Arrays.asList(1L));
        userRepository.save(user);
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
        message.setId(1L);
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
        Optional<Message> byId = messageRepository.findById(1L);
        System.out.println(byId.get());
        Query query = new Query();
        query.addCriteria(new Criteria().where("create_time").lt(LocalDateTime.of(2020,5,26,10,0)));
        List<Message> messages = mongoTemplate.find(query, Message.class);
        System.out.println(messages);
    }

}