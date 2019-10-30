package com.hk.sso.server.controller;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.domain.WebResult;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.utils.CookieUtils;
import com.hk.sso.common.utils.TicketUtils;
import com.hk.sso.server.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 19:31
 * 输入框使用password，请求采用post
 * 使用https
 * 前端加密
 * js文件混淆加密
 * 限制一段时间内重试的次数
 * 关键操作（付款修改密码等）要有二次验证，邮箱，短信等密保手段
 * cookie设置httpOnly
 * 防止xss共计
 * 防止非法重定向
 */
@Controller
public class IndexController {
    /**
     * cookie加密key
     */
    public String cookieEncryptKey = "ac27c9101b3f22d0224f8d9d36ba5be7";
    /**
     * cookie签名加密key
     */
    public String cookieSignKey = "8f8c36c8071fc4d15e49888902f348c8";

    @PostMapping("api/login")
    @ResponseBody
    public WebResult<String> login(User user, String redirectUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = CookieUtils.getCookieValue(request, "token", "utf-8");
        System.out.println("跨站请求伪造=====:"+token);
        String s = DigestUtils.md5DigestAsHex("admin".getBytes(StandardCharsets.UTF_8));
        if ("admin".equals(user.getName()) && s.equals(user.getPassword())) {
            AuthTicket authTicket = new AuthTicket(1L, "毛毛", 1, 1572090022000L, 1582090022000L, "");

            CookieSignUtils.createSign(authTicket, cookieSignKey);
            String value = TicketUtils.encryptTicket(authTicket, cookieEncryptKey);

            CookieUtils.setCookie(request, response, "token", value, 36000000);
            return new WebResult<>(200,"",redirectUrl);
        }
        return new WebResult<>(400,"","http://ssoclient.hk.com:8081/login");

    }


}
