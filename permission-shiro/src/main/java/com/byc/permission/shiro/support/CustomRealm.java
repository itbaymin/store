package com.byc.permission.shiro.support;

import com.byc.permission.shiro.service.SysUserService;
import com.byc.persisent.permission.entity.SysMenu;
import com.byc.persisent.permission.entity.SysRole;
import com.byc.persisent.permission.entity.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;

    @Override
    /**授权*/
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名去数据库查询用户信息
        SysUser user = userService.findByUsername(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (SysRole role : user.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRolename());
            //添加权限
            for (SysMenu menu : role.getMenus()) {
                simpleAuthorizationInfo.addStringPermission(menu.getPermission());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    /**认证*/
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        SysUser user = userService.findByUsername(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
            return simpleAuthenticationInfo;
        }
    }
}
