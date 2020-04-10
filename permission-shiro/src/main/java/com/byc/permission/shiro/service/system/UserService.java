package com.byc.permission.shiro.service.system;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.persisent.permission.entity.SysUser;
import org.springframework.data.domain.Page;

public interface UserService {

    SysUser findByUsername(String username);

    Page<SysUser> findUsers(QueryParam param);
}
