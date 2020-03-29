package com.byc.permission.shiro.controller;

import com.byc.permission.shiro.service.LoginService;
import com.byc.permission.shiro.support.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @GetMapping(value = "/login")
    public String login() {
        return "登录页";
    }

    @PostMapping(value = "/login")
    @Transactional
    public String login(String userName,String password) {
        LoginResult loginResult = loginService.login(userName,password);
        if(loginResult.isLogin()){
            return "登录成功";
        } else {
            return "登录失败：" + loginResult.getResult();
        }
    }

    @GetMapping(value = "/index")
    @RequiresPermissions("主页")
    public String index() {
        return "主页";
    }

    @GetMapping(value = "/logout")
    public String logout() {
        return "退出";
    }

    @GetMapping("/403")
    public String unauthorizedRole(){
        return "没有权限";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}