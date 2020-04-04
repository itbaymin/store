package com.byc.permission.shiro.service.impl;

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
public class LoginServiceImpl implements LoginService {

    @Autowired
    SysUserService userService;

    @Override
    public LoginResult login(String userName, String password) {
        if (userName == null || userName.isEmpty()) {
            return LoginResult.fail("用户名为空");
        }
        String msg;
        // 1、获取Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();

        // 2、判断当前用户是否登录
        if (currentUser.isAuthenticated()) {
            return LoginResult.succ();
        }

        // 3、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        // 4、认证
        try {
            currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", userName);
            return LoginResult.succ();
        } catch (UnknownAccountException e) {
            log.warn("UnknownAccountException --> 账号不存在");
            msg = "账号不存在";
        } catch (IncorrectCredentialsException e) {
            log.warn("IncorrectCredentialsException --> 密码不正确");
            msg = "密码不正确";
        } catch (AuthenticationException e) {
            log.warn("用户验证失败");
            msg = "AuthenticationException --> 用户验证失败";
        }
        return LoginResult.fail(msg);
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

}
