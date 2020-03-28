package com.byc.permission.shiro.support;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult {
    private boolean isLogin;
    private String result;

    public static LoginResult succ(){
        return new LoginResult(true,"成功");
    }

    public static LoginResult fail(String msg){
        return new LoginResult(false,msg);
    }


}
