package com.byc.permission.shiro.service.impl;

import com.byc.permission.shiro.service.SysUserService;
import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserRepository userRepository;


    @Override
    public SysUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
