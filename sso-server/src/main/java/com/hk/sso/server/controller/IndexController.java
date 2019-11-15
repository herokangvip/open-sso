package com.hk.sso.server.controller;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.domain.WebResult;
import com.hk.sso.common.enums.LoginType;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.utils.CookieUtils;
import com.hk.sso.common.utils.TicketUtils;
import com.hk.sso.server.domain.User;
import com.hk.sso.server.utils.TicketRedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/26 19:31
 * 输入框使用password，请求采用post
 * 使用https
 * 密码前端也要加密
 * js文件混淆加密
 * 前端要做防止重复点击
 * 后端限制一段时间内重试的次数
 * cookie设置httpOnly
 * 防止xss
 * 防止非法重定向
 * 关键操作（付款修改密码等）要有二次验证，邮箱，短信等密保手段
 * <p>
 * 本框架是登录态验证框架，注册、登录、登出等功能只是简单的演示具体功能
 * 需要用户自己根据自己公司的需求完善
 */
@Controller
public class IndexController {
    /**
     * cookie加密key
     */
    private String cookieEncryptKey = "ac27c9101b3f22d0224f8d9d36ba5be7";
    /**
     * cookie签名加密key
     */
    private String cookieSignKey = "8f8c36c8071fc4d15e49888902f348c8";

    @PostMapping("api/login")
    @ResponseBody
    public WebResult<String> login(User user, String redirectUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: 2019/11/15 具体的业务逻辑用户自行处理
        String pass = DigestUtils.md5DigestAsHex("admin".getBytes(StandardCharsets.UTF_8));
        String loginType = request.getHeader("loginType");
        if (loginType != null && !"".equals(loginType)) {
            CookieUtils.setSessionCookie(request, response, "loginType", "1");
        } else {
            return new WebResult<>(400, "", "http://ssoclient.hk.com:8081/login");
        }
        if ("admin".equals(user.getName()) && pass.equals(user.getPassword())) {
            long now = System.currentTimeMillis();
            String sid = UUID.randomUUID().toString().replaceAll("-","");
            AuthTicket authTicket = new AuthTicket(sid, "pin", 1, now, now + 1000 * 60 * 30, LoginType.PC,"");
            CookieSignUtils.createSign(authTicket, cookieSignKey);
            String value = TicketUtils.encryptTicket(authTicket, cookieEncryptKey);
            //设置cookie和缓存数据
            CookieUtils.setSessionCookie(request, response, "pt_key", value);
            CookieUtils.setSessionCookie(request, response, "pin", URLDecoder.decode(authTicket.getPin(), "utf-8"));


            String s = TicketRedisUtils.setTicket(authTicket);
            return new WebResult<>(200, "", redirectUrl);
        }
        return new WebResult<>(400, "", "http://ssoclient.hk.com:8081/login");

    }

    @PostMapping("api/logout")
    @ResponseBody
    public WebResult<String> logout(String name, HttpServletRequest request, HttpServletResponse response) {
        try {
            //TODO 删除cookie和redis缓存数据
            return new WebResult<>(200, "", "");
        } catch (Exception e) {
            return new WebResult<>(500, "", "logout error");
        }
    }


}
