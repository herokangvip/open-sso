package com.hk.sso.common.utils;

import com.hk.sso.common.exception.OpenSsoException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/24 17:50
 */
public final class CookieUtils {

    /**
     * 得到Cookie的值,
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookie.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new OpenSsoException("get cookie UnsupportedEncodingException");
        }
        return retValue;
    }


    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     * cookieMaxAge：-1：session级别，0：删除，>0：cookie生效的秒数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge) throws UnsupportedEncodingException {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge);
    }

    /**
     * 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) throws UnsupportedEncodingException {
        doSetCookie(request, response, cookieName, "", 0);
    }


    /**
     * 设置Cookie的值，并使其在指定时间内生效
     *
     * @param cookieMaxAge cookie生效的最大秒数
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge) throws UnsupportedEncodingException {
        if (cookieValue == null) {
            cookieValue = "";
        } else {
            cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
        }
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(cookieMaxAge);
        if (null != request) {// 设置域名的cookie
            String domainName = getDomainName(request);
            //此处设置父级cookie：.xx.xx和版本有关，此处使用springboot2不需要写.，不然会报错可以添加config解决
            cookie.setDomain(domainName.substring(1));
        }
        cookie.setPath("/");
        //XSS防护防止js读取cookie
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 得到cookie的域名
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }
}
