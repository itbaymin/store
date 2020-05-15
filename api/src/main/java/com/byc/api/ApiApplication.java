package com.byc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by baiyc
 * 2020/5/15/015 15:43
 * Description：api启动类
 */
@SpringBootApplication
@ComponentScan(value = "com.byc")
@EnableJpaRepositories(value = "com.byc.persisent")
@EntityScan(value = "com.byc.persisent")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class,args);
    }
}
