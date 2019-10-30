package com.hk.sso.client.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 测试配置文件加载
 */
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoInterceptorConfig {
    /**
     * 客户端cookie名称
     */
    public String cookieName;
    /**
     * 编码
     */
    public String charset;


    /**
     * 是否需要重定向到sso登录页
     */
    public Boolean needRedirect;
    /**
     * sso登录页面地址
     */
    public String ssoLoginUrl;
    /**
     * 登陆成功回调地址
     */
    public String redirectUrl;


    /**
     * 是否启用cookie签名验证
     */
    public Boolean cookieSignValidate;
    /**
     * cookie签名验证类型，1：客户端验证；2：服务端验证
     */
    public Integer cookieSignType;


    /**
     * cookie签名加密key
     */
    public String cookieEncryptKey;// = "fe86a8114a0cec75c4a05ca5263fa4ed";
    /**
     * cookie加密key
     */
    public String cookieSignKey;// "a706ce76159d94c987b7a7d465ffba7d";

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getNeedRedirect() {
        return needRedirect;
    }

    public void setNeedRedirect(Boolean needRedirect) {
        this.needRedirect = needRedirect;
    }

    public String getSsoLoginUrl() {
        return ssoLoginUrl;
    }

    public void setSsoLoginUrl(String ssoLoginUrl) {
        this.ssoLoginUrl = ssoLoginUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Boolean getCookieSignValidate() {
        return cookieSignValidate;
    }

    public void setCookieSignValidate(Boolean cookieSignValidate) {
        this.cookieSignValidate = cookieSignValidate;
    }

    public Integer getCookieSignType() {
        return cookieSignType;
    }

    public void setCookieSignType(Integer cookieSignType) {
        this.cookieSignType = cookieSignType;
    }

    public String getCookieEncryptKey() {
        return cookieEncryptKey;
    }

    public void setCookieEncryptKey(String cookieEncryptKey) {
        this.cookieEncryptKey = cookieEncryptKey;
    }

    public String getCookieSignKey() {
        return cookieSignKey;
    }

    public void setCookieSignKey(String cookieSignKey) {
        this.cookieSignKey = cookieSignKey;
    }
}
