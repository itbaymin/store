package com.byc.permission.shiro.service.impl;

import com.byc.permission.shiro.configuration.RedisCacheConfiguration;
import com.byc.permission.shiro.mvc.vo.MenusVO;
import com.byc.permission.shiro.service.CommonService;
import com.byc.permission.shiro.service.LoginService;
import com.byc.permission.shiro.service.SysUserService;
import com.byc.permission.shiro.support.LoginResult;
import com.byc.persisent.permission.entity.SysMenu;
import com.byc.persisent.permission.repository.SysMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    SysMenuRepository menuRepository;


    @Override
    @Cacheable(value = "menus")
    public MenusVO getMenus() {
        List<SysMenu> sysMenus = menuRepository.findByType("01");
        return MenusVO.build(sysMenus);
    }
}
