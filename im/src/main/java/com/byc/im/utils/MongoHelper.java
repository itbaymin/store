package com.byc.im.utils;

import com.byc.im.entity.MongoSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by baiyc
 * 2020/5/26/026 14:43
 * Description：mongo工具
 */
@Component
public class MongoHelper {
    @Autowired
    MongoTemplate template;

    @Getter
    @AllArgsConstructor
    public enum Collection{
        USER("user"),
        MESSAGE("message");
        private String name;
    }

    public long getNextSequence(Collection collectionName) {
        MongoSequence seq = template.findAndModify(
                query(where("_id").is(collectionName.getName())),
                new Update().inc("seq", 1),
                options().upsert(true).returnNew(true),
                MongoSequence.class);
        return seq.getSeq();
    }
}
