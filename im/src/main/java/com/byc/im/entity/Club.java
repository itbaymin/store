package com.byc.im.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by baiyc
 * 2020/5/25/025 19:43
 * Description：群组
 */
@Data
@Document("club")
public class Club implements Serializable {
    private Long id;
    private String name;
    @Field("headImg")
    private String headImg;
    private List<Member> members; //分组
    @Field("create_time")
    private LocalDateTime createTime;

    @Data
    public static class Member{
        private Long id;
        private String username;
        private String password;
        @Field("headImg")
        private String headImg;
    }
}
