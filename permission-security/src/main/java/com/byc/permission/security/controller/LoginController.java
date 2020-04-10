package com.byc.permission.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
public class LoginController {


    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/logout")
    public String logout() {
        return "login";
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