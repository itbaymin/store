package com.byc.im.entity;

import com.byc.common.utils.TimeFormatter;
import com.byc.im.support.UserGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by baiyc
 * 2020/5/25/025 19:43
 * Description：用户
 */
@Data
@Slf4j
@Document("user")
public class User implements Serializable {
    @Transient
    private String channelId = "";
    private Long id;
    private String username;
    private String password;
    @Field("head_img")
    private String headImg;
    private List<Group> groups;     //分组
    private List<Long> rooms;
    private List<Friend> favorite;  //最爱
    @Transient
    private List<Friend> history;
    @Transient
    private List<Club> clubs;       //群聊

    /**添加好友*/
    public void addFriend(User target) {
        if(!CollectionUtils.isEmpty(groups)){
            for (Group group:groups){
                if(group.getName().equals("默认")){
                    if(CollectionUtils.isEmpty(group.getFriends())){
                        List<Friend> friends = new ArrayList();
                        friends.add(Friend.of(target));
                        group.setFriends(friends);
                    }else {
                        group.getFriends().add(Friend.of(target));
                    }
                    return;
                }
            }
        }
        Group group = new Group();
        List<Friend> friends = new ArrayList();
        friends.add(Friend.of(target));
        group.setFriends(friends);
        group.setCreateTime(LocalDateTime.now());
        if(CollectionUtils.isEmpty(groups))
            this.setGroups(Arrays.asList(group));
        else
            this.getGroups().add(group);
    }

    @Data
    public static class Group{
        private String name = "默认";
        private int flag = 1;
        @Field("create_time")
        private LocalDateTime createTime;
        private List<Friend> friends;
    }

    @Data
    public static class Friend{
        private Long id;
        private String username;
        @Field("head_img")
        private String headImg;
        @Field("unread_num")
        private int unReadNum = 0;
        private String createTime;
        private String content;

        public static Friend of(User target) {
            Friend friend = new Friend();
            friend.setId(target.getId());
            friend.setUsername(target.getUsername());
            friend.setHeadImg(target.getHeadImg());
            friend.setContent("我们已经是好友了，欢迎来撩");
            friend.setCreateTime(TimeFormatter.formatDate(TimeFormatter.Format.SIMPLE));
            return friend;
        }
    }

    //构建参数
    public void build(){
        //填充聊天历史
        this.history = new ArrayList();
        if(!CollectionUtils.isEmpty(groups))
            for (Group group:groups)
                for (Friend friend:group.getFriends()) {
                    if(UserGroup.search(friend.getId())!=null)
                        history.add(friend);
                }
    }

    public void updataFriend(Object data,Long source){
        for (Group group:groups) {
            for (Friend friend : group.getFriends()) {
                if (friend.getId().equals(source)) {
                    friend.setCreateTime(((Map) data).get("createTime").toString());
                    friend.setContent(((Map) data).get("content").toString());
                    return;
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setHeadImg(int random){
        this.headImg = "/image/headimg"+random+".jpg";
    }
}
