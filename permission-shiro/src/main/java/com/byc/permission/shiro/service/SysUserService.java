package com.byc.permission.shiro.service;

import com.byc.persisent.permission.entity.SysUser;

public interface SysUserService {

    SysUser findByUsername(String username);
}
