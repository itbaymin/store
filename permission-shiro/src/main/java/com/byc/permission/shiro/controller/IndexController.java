package com.byc.permission.shiro.controller;

import com.byc.permission.shiro.mvc.vo.MenusVO;
import com.byc.permission.shiro.service.CommonService;
import com.byc.permission.shiro.support.result.JsonResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiresPermissions("index")
/**首页相关业务*/
public class IndexController {

    @Autowired
    CommonService commonService;

    @GetMapping(value = "/index")
    public String index(Model model) {
        MenusVO menus = commonService.getMenus();
        model.addAttribute("menus",menus);
        model.addAttribute("title","主页");
        return "index";
    }

    @GetMapping(value = "/welcome")
    public String welcome(Model model) {
        model.addAttribute("title","主页");
        return "welcome";
    }

    @PostMapping(value = "/menus")
    @ResponseBody
    /**菜单数据*/
    public JsonResult menus(){
        return JsonResult.succ(commonService.getMenus());
    }

}
