package com.hk.sso.client.interceptor;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.domain.ClientParams;
import com.hk.sso.common.enums.CookieSignType;
import com.hk.sso.common.enums.LoginStatus;
import com.hk.sso.common.enums.LoginType;
import com.hk.sso.common.exception.OpenSsoException;
import com.hk.sso.common.service.SsoRemoteService;
import com.hk.sso.common.utils.CookieSignUtils;
import com.hk.sso.common.utils.CookieUtils;
import com.hk.sso.common.utils.TicketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/24 17:54.
 */
public class LoginInterceptor {
    /**
     * 1：pc 登录态
     * 2：app
     * 3：h5
     * 4：wq
     */
    private String loginTypeCookieName = "loginType";

    /**
     * pc客户端cookie名称
     */
    @Value("${sso.pcCookieName:pt_key}")
    public String pcCookieName;
    /**
     * app客户端cookie名称
     */
    @Value("${sso.appCookieName.app:at_key}")
    public String appCookieName;
    /**
     * H5 cookie名称
     */
    @Value("${sso.h5CookieName:ht_key}")
    public String h5CookieName;
    /**
     * WQ cookie名称
     */
    @Value("${sso.wqCookieName:wqt_key}")
    public String wqCookieName;
    /**
     * 编码
     */
    @Value("${sso.charset:UTF-8}")
    private String charset;


    /**
     * 是否需要重定向到sso登录页
     */
    @Value("${sso.needRedirect:true}")
    public Boolean needRedirect;
    /**
     * sso登录页面地址
     */
    @Value("${sso.ssoLoginUrl:http://ssoindex.heroking.com}")
    public String ssoLoginUrl;
    /**
     * 登陆成功回调地址
     */
    @Value("${sso.redirectUrl:http://ssoclient.heroking.com:8081/test}")
    public String redirectUrl;


    /**
     * 是否启用cookie签名验证
     */
    @Value("${sso.cookieSignValidate:true}")
    private Boolean cookieSignValidate = true;
    /**
     * cookie签名验证类型，1：客户端验证；2：服务端验证
     */
    @Value("${sso.cookieSignType:1}")
    private Integer cookieSignType = CookieSignType.CLIENT.code;


    /**
     * cookie加密key
     */
    @Value("${sso.cookieEncryptKey:ac27c9101b3f22d0224f8d9d36ba5be7}")
    private String cookieEncryptKey;
    /**
     * cookie签名加密key
     */
    @Value("${sso.cookieSignKey:8f8c36c8071fc4d15e49888902f348c8}")
    private String cookieSignKey;


    /**
     * rpc认证方式时远程服务bean
     * rpc:             RpcSsoRemoteService
     */
    @Autowired
    private SsoRemoteService ssoRemoteService;


    protected LoginStatus checkLoginTicket(HttpServletRequest request, HttpServletResponse response, ClientParams clientParams) {
        try {
            //获取登录态cookie
            String token = getTokenFromClientParams(clientParams);
            if (token == null || "".equals(token.trim())) {
                return LoginStatus.LOGOUT;
            }

            //解析登录态，转换ticket
            AuthTicket authTicket = TicketUtils.decryptTicket(token, cookieEncryptKey);
            if (authTicket == null || authTicket.isExpired() || authTicket.isIllegal()) {
                return LoginStatus.EXPIRED;
            }

            //客户端验证登录态cookie的签名
            if (cookieSignValidate && cookieSignType == CookieSignType.CLIENT.code) {
                boolean cookieSignResult = CookieSignUtils.validateSign(authTicket, cookieSignKey);
                if (!cookieSignResult) {
                    return LoginStatus.SIGN_ERROR;
                }
            }

            //远程服务校验登录态
            int loginStatus = checkFromRemote(authTicket);
            if (LoginStatus.SUCCESS.code == loginStatus) {
                return LoginStatus.SUCCESS;
            }
        } catch (OpenSsoException e) {
            e.printStackTrace();
        }
        return LoginStatus.SERVER_ERROR;
    }

    protected ClientParams getClientParamsFromRequest(HttpServletRequest request) {
        ClientParams clientCookies = new ClientParams();

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return clientCookies;
        }
        try {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                //String loginTypeParam = request.getParameter(loginTypeCookieName);
                //clientCookies.setLoginType(loginTypeParam);
                if (loginTypeCookieName.equals(cookieName)) {
                    clientCookies.setLoginType(URLDecoder.decode(cookie.getValue(), charset));
                }
                if (appCookieName.equals(cookieName)) {
                    clientCookies.setAppCookieValue(URLDecoder.decode(cookie.getValue(), charset));
                }
                if (pcCookieName.equals(cookieName)) {
                    clientCookies.setPcCookieValue(URLDecoder.decode(cookie.getValue(), charset));
                }
                if (h5CookieName.equals(cookieName)) {
                    clientCookies.setH5CookieValue(URLDecoder.decode(cookie.getValue(), charset));
                }
                if (wqCookieName.equals(cookieName)) {
                    clientCookies.setWqCookieValue(URLDecoder.decode(cookie.getValue(), charset));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return clientCookies;
    }


    protected boolean deleteCookies(ClientParams clientCookies, HttpServletRequest request, HttpServletResponse response) {
        try {
            String loginType = clientCookies.getLoginType();
            if (loginType != null && !"".equals(loginType)) {
                String cookieName = null;
                if (LoginType.PC.equals(loginType)) {
                    cookieName = pcCookieName;
                } else if (LoginType.APP.equals(loginType)) {
                    cookieName = appCookieName;
                } else if (LoginType.H5.equals(loginType)) {
                    cookieName = h5CookieName;
                } else if (LoginType.WQ.equals(loginType)) {
                    cookieName = wqCookieName;
                } else {
                    cookieName = "";
                }
                if (!"".equals(cookieName)) {
                    CookieUtils.deleteCookie(request, response, cookieName);
                }
                CookieUtils.deleteCookie(request, response, "pin");
                CookieUtils.deleteCookie(request, response, "loginType");
                return true;
            } else {
                CookieUtils.deleteCookie(request, response, pcCookieName);
                CookieUtils.deleteCookie(request, response, appCookieName);
                CookieUtils.deleteCookie(request, response, h5CookieName);
                CookieUtils.deleteCookie(request, response, wqCookieName);
                CookieUtils.deleteCookie(request, response, "pin");
                CookieUtils.deleteCookie(request, response, "loginType");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


    private String getTokenFromClientParams(ClientParams clientCookies) {
        String loginType = clientCookies.getLoginType();
        if (loginType == null || "".equals(loginType)) {
            return "";
        }
        if (LoginType.PC.equals(loginType)) {
            return clientCookies.getPcCookieValue();
        } else if (LoginType.APP.equals(loginType)) {
            return clientCookies.getAppCookieValue();

        } else if (LoginType.H5.equals(loginType)) {
            return clientCookies.getH5CookieValue();

        } else if (LoginType.WQ.equals(loginType)) {
            return clientCookies.getWqCookieValue();
        } else {
            return "";
        }
    }

    private int checkFromRemote(AuthTicket authTicket) {
        try {
            boolean validateSign = false;
            if (cookieSignValidate && cookieSignType == CookieSignType.SERVER.code) {
                validateSign = true;
            }
            return ssoRemoteService.checkFromRemote(authTicket, validateSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LoginStatus.SERVER_ERROR.code;
    }


}
