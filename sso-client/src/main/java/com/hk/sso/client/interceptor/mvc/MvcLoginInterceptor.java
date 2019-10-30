package com.hk.sso.client.interceptor.mvc;

import com.hk.sso.client.interceptor.LoginInterceptor;
import com.hk.sso.common.exception.OpenSsoException;
import com.hk.sso.common.utils.CookieUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 17:45
 */
public class MvcLoginInterceptor extends LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        boolean checkResult = checkLoginTicket(request, response);
        if (!checkResult) {
            CookieUtils.deleteCookie(request, response, cookieName);
            if (needRedirect) {
                response.sendRedirect(ssoLoginUrl + "?redirect=" + redirectUrl);
            }
        }
        return checkResult;
    }
}
