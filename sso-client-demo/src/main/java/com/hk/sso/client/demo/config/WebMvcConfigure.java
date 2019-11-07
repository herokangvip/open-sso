package com.hk.sso.client.demo.config;

import com.hk.sso.client.interceptor.mvc.MvcLoginInterceptor;
import com.hk.sso.common.service.SsoRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    @Resource
    private SsoInterceptorConfig ssoInterceptorConfig;


    @Autowired
    private SsoRemoteService ssoRemoteService;
    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        MvcLoginInterceptor mvcLoginInterceptor = configMvcLoginInterceptor(ssoInterceptorConfig);
        //2019/10/29 注入认证服务
        mvcLoginInterceptor.ssoRemoteService = ssoRemoteService;
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
                return mv;
            }
        });
    }


    private MvcLoginInterceptor configMvcLoginInterceptor(SsoInterceptorConfig ssoInterceptorConfig) {
        MvcLoginInterceptor mvcLoginInterceptor = new MvcLoginInterceptor();
        String cookieName = ssoInterceptorConfig.getCookieName();
        if (cookieName != null && !"".equals(cookieName.trim())) {
            mvcLoginInterceptor.cookieName = cookieName;
        }
        Integer cookieSignType = ssoInterceptorConfig.getCookieSignType();
        if (cookieSignType != null && (cookieSignType == 1 || cookieSignType == 2)) {
            mvcLoginInterceptor.cookieSignType = cookieSignType;
        }
        String cookieEncryptKey = ssoInterceptorConfig.getCookieEncryptKey();
        if (cookieEncryptKey != null && !"".equals(cookieEncryptKey.trim())) {
            mvcLoginInterceptor.cookieEncryptKey = cookieEncryptKey;
        }
        String charset = ssoInterceptorConfig.getCharset();
        if (charset != null && !"".equals(charset.trim())) {
            mvcLoginInterceptor.charset = charset;
        }
        String redirectUrl = ssoInterceptorConfig.getRedirectUrl();
        if (redirectUrl != null && !"".equals(redirectUrl.trim())) {
            mvcLoginInterceptor.redirectUrl = redirectUrl;
        }
        String ssoLoginUrl = ssoInterceptorConfig.getSsoLoginUrl();
        if (ssoLoginUrl != null && !"".equals(ssoLoginUrl.trim())) {
            mvcLoginInterceptor.ssoLoginUrl = ssoLoginUrl;
        }
        Boolean needRedirect = ssoInterceptorConfig.getNeedRedirect();
        if (ssoLoginUrl != null) {
            mvcLoginInterceptor.needRedirect = needRedirect;
        }
        Boolean cookieSignValidate = ssoInterceptorConfig.getCookieSignValidate();
        if (ssoLoginUrl != null) {
            mvcLoginInterceptor.cookieSignValidate = cookieSignValidate;
        }
        return mvcLoginInterceptor;
    }
}
