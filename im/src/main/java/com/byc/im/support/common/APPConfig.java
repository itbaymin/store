package com.byc.im.support.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by baiyc
 * 2020/5/30/030 14:13
 * Description：工程配置
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "config",ignoreInvalidFields = true)
public class APPConfig {
    private Websocket websocket;

    @Data
    public static class Websocket{
        private String addr;
        private int port;
    }
}
