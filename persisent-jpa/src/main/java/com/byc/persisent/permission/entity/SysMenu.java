package com.byc.persisent.permission.entity;


import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_menu",indexes = {@Index(name = "index_menuname",unique = true,columnList = "menuname")})
/**系统菜单*/
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11)")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT),columnDefinition = "INT(11) COMMENT '父级菜单ID'")
    private SysMenu parent;

    @OneToMany(mappedBy = "parent")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<SysMenu> children;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '菜单名'")
    private String menuname;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '类型'")
    private String type;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '权限名'")
    private String permission;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL DEFAULT '' COMMENT 'uri'")
    private String uri;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL DEFAULT '' COMMENT '图标'")
    private String icon;

    @Column(columnDefinition = "INT(10) NOT NULL DEFAULT 0 COMMENT '排序'")
    private int sort;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL DEFAULT '' COMMENT '介绍'")
    private String intro;

    @Column(columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否可用'")
    private boolean enable;

    @Column(columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否显示'")
    private boolean display;

    @Column(name = "create_time",columnDefinition = "INT(10) NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private long createTime;

    @Column(name = "update_time",columnDefinition = "INT(10) NOT NULL DEFAULT 0 COMMENT '更新时间'")
    private long updateTime;

    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", menuname='" + menuname + '\'' +
                ", parent='" + parent + '\'' +
                ", type='" + type + '\'' +
                ", permission='" + permission + '\'' +
                ", uri='" + uri + '\'' +
                ", icon='" + icon + '\'' +
                ", sort=" + sort +
                ", intro='" + intro + '\'' +
                ", enable=" + enable +
                ", display=" + display +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
