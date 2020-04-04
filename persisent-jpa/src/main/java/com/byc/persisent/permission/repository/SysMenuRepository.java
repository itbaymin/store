package com.byc.persisent.permission.repository;

import com.byc.persisent.BaseRepository;
import com.byc.persisent.permission.entity.SysMenu;

import java.util.List;

public interface SysMenuRepository extends BaseRepository<SysMenu,Long> {
    List<SysMenu> findByType(String type);
}
