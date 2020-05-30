package com.byc.im;

import com.byc.im.support.common.APPConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by baiyc
 * 2020/5/20/020 15:19
 * Description：
 */
@ComponentScan("com.byc")
@SpringBootApplication
@EnableConfigurationProperties(APPConfig.class)
public class IMApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMApplication.class,args);
    }
}
