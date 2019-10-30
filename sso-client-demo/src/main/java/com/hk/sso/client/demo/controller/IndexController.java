package com.hk.sso.client.demo.controller;

import com.hk.sso.common.domain.WebResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/26 18:03
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    @ResponseBody
    public WebResult<String> index() {
        return new WebResult<String>(200,"success","index");
    }

    @RequestMapping("/test")
    @ResponseBody
    public WebResult<String> test() {
        return new WebResult<String>(200,"success","登陆成功后跳转地址");
    }



}
