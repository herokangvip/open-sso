package com.hk.sso.server.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/29 15:25
 * 处理URL跳转漏洞与重定向劫持
 */
public class SsoServerInterceptor implements HandlerInterceptor {
    private String defaultRedirectUrl = "http://ssoclient.heroking.com:8081/test";
    //顶级域名
    private String TopDomainName = ".heroking.com";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        //防止重定向漏洞与劫持
        String redirectUrl = request.getParameter("redirectUrl");
        if (redirectUrl == null || "".equals(redirectUrl.trim())) {
            redirectUrl = defaultRedirectUrl;
        }
        String remoteHost = request.getRequestURL().toString();
        boolean remoteHostIllegal = isIllegalUrl(remoteHost);
        if (remoteHostIllegal) {
            String data = "{\"code\":403,\"message\":\"illegal source host\"}";
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(data.getBytes(StandardCharsets.UTF_8));
                out.flush();
            } finally {
                if (out != null)
                    out.close();
            }
            return false;
        }
        boolean illegal = isIllegalUrl(redirectUrl);
        if (illegal) {
            String data = "{\"code\":404,\"message\":\"illegal dest url\"}";
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(data.getBytes(StandardCharsets.UTF_8));
                out.flush();
            } finally {
                if (out != null)
                    out.close();
            }
            return false;
        }
        return true;
    }

    /**
     * 是否非法url
     *
     * @param redirectUrl
     * @return true:非法
     */
    private boolean isIllegalUrl(String redirectUrl) {
        if (!redirectUrl.startsWith("http://") && !redirectUrl.startsWith("https://")) {
            return true;
        }
        redirectUrl = redirectUrl.replaceAll("[\\\\#]", "/");
        String host = null;
        try {
            host = new URL(redirectUrl).getHost();
            return !host.endsWith(TopDomainName);
        } catch (MalformedURLException e) {
            //非法url
            return true;
        }
    }


}
