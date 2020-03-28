package com.byc.persisent.permission.entity;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_menu",indexes = {@Index(name = "index_menuname",unique = true,columnList = "menuname")})
/**系统菜单*/
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11)")
    private long id;

    @OneToOne
    @JoinColumn(name = "pid",columnDefinition = "INT(11) COMMENT '父级菜单ID'")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private SysMenu parent;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '菜单名'")
    private String menuname;

    @Column(name = "create_time",columnDefinition = "INT(10) NOT NULL COMMENT '创建时间'")
    private long createTime;

    @Column(name = "update_time",columnDefinition = "INT(10) NOT NULL COMMENT '更新时间'")
    private long updateTime;
}
