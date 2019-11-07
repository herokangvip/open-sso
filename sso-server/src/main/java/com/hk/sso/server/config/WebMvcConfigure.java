package com.hk.sso.server.config;

import com.hk.sso.server.interceptor.SsoServerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    //cors跨域支持
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://ssoindex.heroking.com")
                .maxAge(3000L)
                .allowCredentials(true)
                //.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowedMethods("POST", "GET", "OPTIONS", "PUT", "DELETE");
    }

    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SsoServerInterceptor() {
        }).addPathPatterns("/**");
    }


}
