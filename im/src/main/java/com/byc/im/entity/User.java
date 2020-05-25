package com.byc.im.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * Created by baiyc
 * 2020/5/25/025 19:43
 * Description：用户
 */
@Data
@Document("user")
public class User implements Serializable {

    private Long id;
    private String username;
    private String password;
    @Field("headImg")
    private String headImg;
    private List<Long> friends;
}
