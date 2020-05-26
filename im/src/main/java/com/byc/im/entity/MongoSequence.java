package com.byc.im.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by baiyc
 * 2020/5/26/026 14:45
 * Description：自增
 */
@Data
public class MongoSequence {
    @Id
    private String id;
    private long seq;
}
