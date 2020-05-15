package com.byc.api.controller;

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

    @RequestMapping("test")
    public String test(){
        return "hello";
    }
}
