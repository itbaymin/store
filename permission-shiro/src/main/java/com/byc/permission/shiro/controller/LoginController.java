package com.byc.permission.shiro.controller;

import com.byc.permission.shiro.service.LoginService;
import com.byc.permission.shiro.support.LoginResult;
import com.byc.permission.shiro.support.result.JsonResult;
import com.byc.permission.shiro.support.result.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Controller
public class LoginController {
    @Resource
    private LoginService loginService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    @Transactional
    public JsonResult login(String username, String password) {
        LoginResult loginResult = loginService.login(username,password);
        if(loginResult.isLogin()){
            return JsonResult.succ();
        } else {
            return JsonResult.fail(StatusCode.PARAM_ERROR,"登录失败：" + loginResult.getResult());
        }
    }

    @GetMapping(value = "/logout")
    @ResponseBody
    public String logout() {
        return "退出";
    }

    @GetMapping("/403")
    @ResponseBody
    public String unauthorizedRole(){
        return "没有权限";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}