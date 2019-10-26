package com.hk.sso.client.demo.config;

import com.hk.sso.client.interceptor.mvc.MvcLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {


    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MvcLoginInterceptor() {
        }).addPathPatterns("/*");
    }

    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    //cors跨域支持
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .maxAge(3000L)
                .allowCredentials(true)
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowedMethods("POST", "GET","OPTIONS","PUT");
    }


    //全局异常解析
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
                ModelAndView mv = new ModelAndView();
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                mv.setView(view);
                if (e instanceof NumberFormatException) {
                    mv.addObject("status", "500");
                    mv.addObject("message", "NumberFormatException");
                    return mv;
                }
                mv.addObject("status", "500");
                mv.addObject("message", "系统错误");
                mv.addObject("error", e);
                return mv;
            }
        });
    }


}
