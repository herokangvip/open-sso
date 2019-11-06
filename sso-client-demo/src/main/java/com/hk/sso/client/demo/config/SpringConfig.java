package com.hk.sso.client.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/11/5 14:19
 */
@Configuration
@ImportResource(locations = {"classpath:spring/spring-dubbo.xml"})
public class SpringConfig {
}
