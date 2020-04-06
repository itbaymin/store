package com.byc.permission.shiro.service.system;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.persisent.permission.entity.SysUser;

import java.util.List;

public interface UserService {

    SysUser findByUsername(String username);

    List<SysUser> findUsers(QueryParam param);
}
