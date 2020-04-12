package com.byc.persisent.permission.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "sys_user", indexes = {@Index(name = "index_username",unique = true,columnList="username")})
/**系统用户*/
public class SysUser implements Serializable {

    @Id
    @Column(columnDefinition = "INT(11)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '用户名'")
    private String username;

    @Column(columnDefinition = "VARCHAR(60) NOT NULL COMMENT '密码'")
    private String password;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '盐值'")
    private String salt;

    @Column(columnDefinition = "TINYINT(1) NOT NULL COMMENT '账号状态：1、在线 2、下线 3、禁用'")
    private int state;

    @Column(name = "create_time",columnDefinition = "INT(10) NOT NULL COMMENT '创建时间'")
    private long createTime;

    @Column(name = "update_time",columnDefinition = "INT(10) NOT NULL COMMENT '更新时间'")
    private long updateTime;


    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "role_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))})
    private List<SysRole> roles;

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
