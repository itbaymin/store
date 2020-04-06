package com.byc.permission.shiro.service.system;

import com.byc.persisent.permission.entity.SysUser;

public interface UserService {

    SysUser findByUsername(String username);
}
