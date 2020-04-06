package com.byc.permission.shiro.controller.system;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.permission.shiro.support.result.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
/**用户管理*/
public class UserController {

    @GetMapping("index")
    public String index(Model model){
        model.addAttribute("title","用户管理");
        return "system/user/user-list";
    }

    @GetMapping("add")
    public String add(){
        return "system/user/user-add";
    }

    @GetMapping("edit")
    public String edit(){
        return "system/user/user-edit";
    }

    @ResponseBody
    @PostMapping("list")
    public JsonResult list(@RequestParam QueryParam param){
        System.out.println(param);
        return null;
    }

    @ResponseBody
    @PostMapping("add")
    public JsonResult doAdd(){
        return null;
    }

    @ResponseBody
    @PostMapping("edit")
    public JsonResult doEdit(){
        return null;
    }

    @ResponseBody
    @PostMapping("delete")
    public JsonResult doDel(){
        return null;
    }

}
