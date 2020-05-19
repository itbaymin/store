package com.byc.api.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by baiyc
 * 2020/5/19/019 17:41
 * Description：
 */
@Component
public class MyPusher implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3*1000)
    public void run() {
        context.publishEvent(new MyEvent(this,"123"));
    }
}
