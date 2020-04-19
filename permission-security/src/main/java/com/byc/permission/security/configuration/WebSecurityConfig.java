package com.byc.permission.security.configuration;

import com.byc.permission.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.PrintWriter;

/**
 * Created by baiyc
 * 2020/4/10/010 19:31
 * Description：配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // 控制权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //校验用户
        auth.userDetailsService( userService ).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger**/**","/webjars/**","/v2/**","/css/**","/fonts/**","/css/**","/js/**","/images/**","/lib/**","/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/index").failureUrl("/error")
                .successHandler((req,resp,auth)->{
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("{\"status\":200,\"message\":\"登录成功\"}");
                    out.flush();
                    out.close();
                })
                .failureHandler((req,resp,auth)->{
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("{\"status\":403,\"message\":\"登录失败\"}");
                    out.flush();
                    out.close();
                })
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/index")
                .and()
                .exceptionHandling().accessDeniedPage("/error")
                .and().csrf().disable();
        http.logout().logoutSuccessUrl("/");
    }

}
