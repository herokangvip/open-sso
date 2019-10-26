package com.hk.sso.client.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 测试配置文件加载
 */
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoInterceptorConfig {

}
