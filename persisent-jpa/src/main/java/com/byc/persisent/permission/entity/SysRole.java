package com.byc.persisent.permission.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_role", indexes = {@Index(name = "index_rolename",unique = true,columnList="rolename")})
/**系统角色*/
public class SysRole {
    @Id
    @Column(columnDefinition = "INT(11)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '用户名'")
    private String rolename;

    @Column(name = "create_time",columnDefinition = "INT(10) NOT NULL COMMENT '创建时间'")
    private long createTime;

    @Column(name = "update_time",columnDefinition = "INT(10) NOT NULL COMMENT '更新时间'")
    private long updateTime;


    @ManyToMany
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "role_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "menu_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))})
    private List<SysMenu> menus;
}
