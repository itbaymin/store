package com.byc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by baiyc
 * 2020/5/15/015 15:49
 * Description：
 */
@RestController
@RequestMapping("wx")
public class WxAccountController {

    @Autowired
    Test test;

    @RequestMapping("test")
    public String test(){
        return "sdafasdfasdf";
    }
}
