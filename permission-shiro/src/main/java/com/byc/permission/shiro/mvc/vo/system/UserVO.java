package com.byc.permission.shiro.mvc.vo.system;

import com.byc.common.utils.TimeFormatter;
import com.byc.persisent.permission.entity.SysRole;
import com.byc.persisent.permission.entity.SysUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
/**系统用户*/
public class UserVO implements Serializable {

    private long id;

    private String username;

    private String password;

    private String salt;

    private int state;

    private String createTime;

    private String updateTime;

    public static UserVO build(SysUser user){
        UserVO vo = new UserVO();
        vo.setUsername(user.getUsername());
        vo.setPassword(user.getPassword());
        vo.setId(user.getId());
        vo.setSalt(user.getSalt());
        vo.setState(user.getState());
        vo.setCreateTime(TimeFormatter.formatTimestamp(TimeFormatter.Format.SIMPLE,user.getCreateTime()));
        vo.setUpdateTime(TimeFormatter.formatTimestamp(TimeFormatter.Format.SIMPLE,user.getUpdateTime()));
        return vo;
    }
}
