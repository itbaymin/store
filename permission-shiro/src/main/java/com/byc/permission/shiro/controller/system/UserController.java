package com.byc.permission.shiro.controller.system;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.permission.shiro.mvc.vo.system.UserVO;
import com.byc.permission.shiro.support.result.JsonResult;
import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
/**用户管理*/
public class UserController {
    @Autowired
    SysUserRepository userRepository;

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
       SysUser all = userRepository.findByUsername("admin");
        return JsonResult.succ(UserVO.build(all));
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
