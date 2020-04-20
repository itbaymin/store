package com.byc.permission.security.service;

import com.byc.persisent.permission.entity.SysUser;
import com.byc.persisent.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baiyc
 * 2020/4/10/010 19:34
 * Description：
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    SysUserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(name);
        if(user==null)
            throw new BadCredentialsException("用户名不存在");
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");

        // 自定义密码验证
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("密码错误，请重新输入");
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        user.getRoles().forEach(sysRole ->
            sysRole.getMenus().forEach(menu -> {
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(menu.getPermission()));
            })
        );
        return User.builder().username(name)
                        .password(user.getPassword()).authorities(simpleGrantedAuthorities).build();
    }

}
