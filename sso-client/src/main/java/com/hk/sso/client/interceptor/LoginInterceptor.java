package com.hk.sso.client.interceptor;

import com.hk.sso.client.service.CookieValidate;
import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.enums.CookieSignType;
import com.hk.sso.common.enums.LoginStatus;
import com.hk.sso.common.enums.SsoServerType;
import com.hk.sso.common.service.SsoService;
import com.hk.sso.common.utils.AESUtils;
import com.hk.sso.common.utils.CookieUtils;

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
     *  是否需要重定向到sso登录页
     */
    public boolean needRedirect = true;
    /**
     * sso登录页面地址
     */
    public String ssoLoginUrl = "http://sso.hk.com/toLogin";
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
     * 是否启用远程校验服务签名验证
     */
    public boolean ssoSignValidate = true;




    /**
     * sso认证中心服务是rpc还是http/https；1:rpc; 2:http
     * 推荐使用rpc
     */
    public int ssoServiceType = SsoServerType.RPC.code;
    /**
     * rpc认证方式时远程服务bean
     */
    public SsoService ssoService;
    /**
     * http认证方式时远端地址
     */
    public String ssoServiceUrl = "http://sso.hk.com";



    protected boolean checkLoginTicket(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, cookieName, charset);
        if (token == null || token.trim().equals("")) {
            return false;
        }
        AuthTicket authTicket = parseToken(token);
        if (authTicket == null || authTicket.isExpired() || authTicket.isIllegal()) {
            return false;
        }
        if (cookieSignType == CookieSignType.CLIENT.code) {
            boolean cookieSignResult = CookieValidate.validate(authTicket);
            if (!cookieSignResult) {
                return false;
            }
        }
        int remote = checkFromRemote(authTicket);
        return remote == LoginStatus.SUCCESS.code;
    }

    private int checkFromRemote(AuthTicket authTicket) {
        // TODO: 2019/10/25
        if (ssoServiceType == SsoServerType.RPC.code) {
            //rpc远程认证
        } else if (ssoServiceType == SsoServerType.HTTP.code) {
            //http远程认证
        }
        return 200;
    }

    private AuthTicket parseToken(String token) {
        return null;
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        AuthTicket authTicket = new AuthTicket(1156486487, "毛毛球k2", 1, System.currentTimeMillis(), System.currentTimeMillis(), "");
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid()).reverse();
        authTicket.setSign(sb.toString());

        String encrypt = AESUtils.encrypt(authTicket.getUid() + "," + authTicket.getPin() + "," + authTicket.getVersion() + "," + authTicket.getTime()
                + "," + authTicket.getExpire() + "," + authTicket.getSign());

        System.out.println("===:" + encrypt);
        System.out.println("===:" + encrypt.length());
    }
}
