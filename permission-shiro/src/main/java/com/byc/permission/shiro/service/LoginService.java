package com.byc.permission.shiro.service;

import com.byc.permission.shiro.support.LoginResult;

public interface LoginService {
    LoginResult login(String username,String password);

    void logout();
}
