package com.byc.permission.security.mvc.vo.system;

import com.byc.common.utils.TimeFormatter;
import com.byc.permission.shiro.mvc.vo.PageVo;
import com.byc.persisent.permission.entity.SysUser;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

    private static UserVO build(SysUser user){
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

    public static PageVo page(Page<SysUser> page){
        List<UserVO> collect = page.getContent().stream().map(UserVO::build).collect(Collectors.toList());
        PageVo<UserVO> pageVo = new PageVo();
        pageVo.setList(collect);
        pageVo.setTotal(page.getTotalElements());
        pageVo.setPage(page.getNumber());
        pageVo.setSize(page.getSize());
        return pageVo;
    }
}
