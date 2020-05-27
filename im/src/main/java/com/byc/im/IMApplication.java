package com.byc.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by baiyc
 * 2020/5/20/020 15:19
 * Description：
 */
@ComponentScan("com.byc")
@SpringBootApplication
public class IMApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class,args);
    }
}
