package com.byc.permission.shiro.service.impl;

import com.byc.permission.shiro.service.CommonService;
import com.byc.permission.shiro.service.LoginService;
import com.byc.permission.shiro.service.SysUserService;
import com.byc.permission.shiro.support.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

}
