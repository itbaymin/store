package com.byc.im.controller;

import com.byc.im.entity.User;
import com.byc.im.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
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

    @Test
    public void test(){
        User user = new User();
        user.setId(1L);
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

}