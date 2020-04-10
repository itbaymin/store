package com.byc.permission.shiro.controller.system;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.permission.shiro.mvc.vo.system.UserVO;
import com.byc.permission.shiro.service.system.UserService;
import com.byc.permission.shiro.support.result.JsonResult;
import com.byc.persisent.permission.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
/**用户管理*/
public class UserController {
    @Autowired
    UserService userService;

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
    public JsonResult list(QueryParam param){
        Page<SysUser> users = userService.findUsers(param);
        return JsonResult.succ(UserVO.page(users));
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
