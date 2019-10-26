package com.hk.sso.client.interceptor;

import com.hk.sso.client.service.HttpSsoRemoteService;
import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.CookieSignType;
import com.hk.sso.common.enums.LoginStatus;
import com.hk.sso.common.utils.AESUtils;
import com.hk.sso.common.utils.CookieUtils;
import com.hk.sso.common.utils.TicketUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/24 17:54
 */
public class LoginInterceptor {

    /**
     * 客户端cookie名称
     */
    public String cookieName = "token";
    /**
     * 编码
     */
    public String charset = "UTF-8";


    /**
     * 是否需要重定向到sso登录页
     */
    public boolean needRedirect = true;
    /**
     * sso登录页面地址
     */
    public String ssoLoginUrl = "http://ssoindex.hk.com";
    /**
     * 登陆成功回调地址
     */
    public String redirectUrl = "http://baidu.com";


    /**
     * 是否启用cookie签名验证
     */
    public boolean cookieSignValidate = true;
    /**
     * cookie签名验证类型，1：客户端验证；2：服务端验证
     */
    public int cookieSignType = CookieSignType.CLIENT.code;


    /**
     * cookie签名客户端验证的加密key
     */
    public String cookieEncryptKey = "5JY9D5G8RET54HSD";


    /**
     * rpc认证方式时远程服务bean
     * http/https:      HttpSsoRemoteService
     * rpc:             RpcSsoRemoteService
     */
    public SsoRemoteService ssoRemoteService = new HttpSsoRemoteService();


    protected boolean checkLoginTicket(HttpServletRequest request, HttpServletResponse response) {
        //获取登录态cookie
        String token = CookieUtils.getCookieValue(request, cookieName, charset);
        if (token == null || token.trim().equals("")) {
            return false;
        }

        //解析登录态，转换ticket
        AuthTicket authTicket = TicketUtils.decryptTicket(token,cookieEncryptKey);
        if (authTicket == null || authTicket.isExpired() || authTicket.isIllegal()) {
            return false;
        }

        //客户端验证登录态cookie的签名
        if (cookieSignType == CookieSignType.CLIENT.code) {
            boolean cookieSignResult = CookieSignUtils.validateSign(authTicket, cookieEncryptKey);
            if (!cookieSignResult) {
                return false;
            }
        }

        //远程服务校验登录态
        LoginStatus loginStatus = checkFromRemote(authTicket);
        return LoginStatus.SUCCESS.equals(loginStatus);
    }

    private LoginStatus checkFromRemote(AuthTicket authTicket) {
        // TODO: 2019/10/25
        boolean validateSign = false;
        if (cookieSignValidate && cookieSignType == CookieSignType.SERVER.code) {
            validateSign = true;
        }
        return ssoRemoteService.checkFromRemote(authTicket, validateSign);
    }



}
