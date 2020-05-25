package com.byc.im.repository;

import com.byc.im.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by baiyc
 * 2020/5/25/025 19:47
 * Description：用户信息
 */
public interface UserRepository extends MongoRepository<User,Long> {
}
