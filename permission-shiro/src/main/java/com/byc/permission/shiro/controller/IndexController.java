package com.byc.permission.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
/**首页相关业务*/
public class IndexController {

    @GetMapping(value = "/index")
    @RequiresPermissions("主页")
    public String index(Model model) {
        model.addAttribute("title","主页");
        return "index";
    }

    @GetMapping(value = "/welcome")
    @RequiresPermissions("主页")
    public String welcome(Model model) {
        model.addAttribute("title","主页");
        return "welcome";
    }


}
