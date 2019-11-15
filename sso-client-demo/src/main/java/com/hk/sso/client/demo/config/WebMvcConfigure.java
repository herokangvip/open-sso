package com.hk.sso.client.demo.config;

import com.hk.sso.client.interceptor.mvc.MvcLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {


    @Autowired
    private MvcLoginInterceptor mvcLoginInterceptor;
    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mvcLoginInterceptor).addPathPatterns("/**");
    }


    //全局异常解析
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
                ModelAndView mv = new ModelAndView();
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                mv.setView(view);
                mv.addObject("code", 500);
                mv.addObject("msg", "try later");
                e.printStackTrace();
                return mv;
            }
        });
    }



}
