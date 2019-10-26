package com.hk.sso.server.controller;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.domain.WebResult;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.utils.CookieUtils;
import com.hk.sso.common.utils.TicketUtils;
import com.hk.sso.server.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 19:31
 */
@Controller
public class IndexController {
    @RequestMapping("api/login")
    @ResponseBody
    public WebResult<String> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("admin".equals(user.getName()) && "admin".equals(user.getPassword())) {
            AuthTicket authTicket = new AuthTicket(1,"毛毛球k2",1,1572090022000L,1582090022000L,"");
            authTicket.setSign(CookieSignUtils.createSign(authTicket,"5JY9D5G8RET54HSD"));
            String value = TicketUtils.encryptTicket(authTicket, "5JY9D5G8RET54HSD");
            CookieUtils.setCookie(request, response, "token", value, 36000000, "UTF-8");
            //response.sendRedirect("http://xuan.jd.com");
            return new WebResult<>(200,"","http://ssoclient.hk.com:8081/login");

        }
        return new WebResult<>(400,"","http://ssoclient.hk.com:8081/login");

    }
}
