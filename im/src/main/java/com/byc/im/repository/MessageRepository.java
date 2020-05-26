package com.byc.im.repository;

import com.byc.im.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by baiyc
 * 2020/5/25/025 19:47
 * Description：聊天消息
 */
public interface MessageRepository extends MongoRepository<Message,Long> {
}
