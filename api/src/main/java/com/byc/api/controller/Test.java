package com.byc.api.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2020/5/15/015 17:41
 * Description：
 */
@Data
@Component
public class Test {
    @Value("${spring.datasource.username}")
    String test;
}
