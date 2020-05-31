package com.byc.im.service;

import com.byc.common.utils.AssertUtil;
import com.byc.im.entity.Message;
import com.byc.im.entity.User;
import com.byc.im.utils.StateCode;
import com.byc.im.repository.UserRepository;
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
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;

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
}
