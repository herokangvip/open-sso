package com.hk.sso.client.interceptor.mvc;

import com.hk.sso.client.interceptor.LoginInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 17:45
 */
public class MvcLoginInterceptor extends LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            boolean checkResult = checkLoginTicket(request, response);
            if (needRedirect) {
                response.sendRedirect(ssoLoginUrl+"?redirect"+redirectUrl);
            }
            return checkResult;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
