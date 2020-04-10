package com.byc.permission.shiro.service.system.impl;

import com.byc.permission.shiro.mvc.param.QueryParam;
import com.byc.permission.shiro.service.system.UserService;
import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    SysUserRepository userRepository;


    @Override
    public SysUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<SysUser> findUsers(QueryParam param) {
        return userRepository.findAll((root, query, builder) -> {
            //条件
            Predicate predicate = builder.like(root.get("username").as(String.class), String.format("%s%s%s", "%", param.getCond(),"%"));
            if(param.getStart()!=null)
                predicate = builder.and(predicate,builder.ge(root.get("createTime").as(Long.class),param.getStart()));
            if(param.getEnd()!=null)
                predicate = builder.and(predicate,builder.le(root.get("createTime").as(Long.class),param.getEnd()));
            //排序
            if(param.asc())
                query.where(predicate).orderBy(builder.asc(root.get(param.getField())));
            else
                query.where(predicate).orderBy(builder.desc(root.get(param.getField())));
            return null;
        }, PageRequest.of(param.getPage(), param.getLimit()));
    }
}
