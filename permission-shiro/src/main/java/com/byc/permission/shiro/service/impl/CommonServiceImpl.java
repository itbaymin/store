package com.byc.permission.shiro.service.impl;

import com.byc.permission.shiro.mvc.vo.MenusVO;
import com.byc.permission.shiro.service.CommonService;
import com.byc.persisent.permission.entity.SysMenu;
import com.byc.persisent.permission.repository.SysMenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    SysMenuRepository menuRepository;


    @Override
    @Cacheable(value = "commons")
    public MenusVO getMenus() {
        List<SysMenu> sysMenus = menuRepository.findByType("01");
        return MenusVO.build(sysMenus);
    }
}
