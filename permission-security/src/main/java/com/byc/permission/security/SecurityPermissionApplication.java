package com.byc.permission.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by baiyc
 * 2020/4/10/010 15:39
 * Description：启动类
 */
@SpringBootApplication
@ComponentScan(value = "com.byc")
@EnableJpaRepositories(value = "com.byc.persisent")
@EntityScan(value = "com.byc.persisent")
public class SecurityPermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityPermissionApplication.class);
    }
}
