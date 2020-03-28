package com.byc.persisent.permission.repository;

import com.byc.persisent.BaseRepository;
import com.byc.persisent.permission.entity.SysUser;

public interface SysUserRepository extends BaseRepository<SysUser,Long> {
    SysUser findByUsername(String name);
}
