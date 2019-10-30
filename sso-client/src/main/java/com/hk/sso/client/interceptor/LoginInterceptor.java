package com.hk.sso.client.interceptor;

import com.hk.sso.client.service.HttpSsoRemoteService;
import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.CookieSignType;
import com.hk.sso.common.enums.LoginStatus;
import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.utils.CookieSignUtils;
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
    public Boolean needRedirect = true;
    /**
     * sso登录页面地址
     */
    public String ssoLoginUrl = "http://ssoindex.heroking.com";
    /**
     * 登陆成功回调地址
     */
    public String redirectUrl = "http://ssoclient.heroking.com:8081/test";


    /**
     * 是否启用cookie签名验证
     */
    public Boolean cookieSignValidate = true;
    /**
     * cookie签名验证类型，1：客户端验证；2：服务端验证
     */
    public Integer cookieSignType = CookieSignType.CLIENT.code;


    /**
     * cookie加密key
     */
    public String cookieEncryptKey = "ac27c9101b3f22d0224f8d9d36ba5be7";
    /**
     * cookie签名加密key
     */
    public String cookieSignKey = "8f8c36c8071fc4d15e49888902f348c8";


    /**
     * rpc认证方式时远程服务bean
     * http/https:      HttpSsoRemoteService
     * rpc:             RpcSsoRemoteService
     */
    public SsoRemoteService ssoRemoteService = new HttpSsoRemoteService();


    protected boolean checkLoginTicket(HttpServletRequest request, HttpServletResponse response) {
        //获取登录态cookie
        String token = CookieUtils.getCookieValue(request, cookieName, charset);
        if (token == null || "".equals(token.trim())) {
            return false;
        }

        //解析登录态，转换ticket
        AuthTicket authTicket = TicketUtils.decryptTicket(token,cookieEncryptKey);


        if (authTicket == null || authTicket.isExpired() || authTicket.isIllegal()) {
            return false;
        }

        //客户端验证登录态cookie的签名
        if (cookieSignType == CookieSignType.CLIENT.code) {
            boolean cookieSignResult = CookieSignUtils.validateSign(authTicket, cookieSignKey);
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
