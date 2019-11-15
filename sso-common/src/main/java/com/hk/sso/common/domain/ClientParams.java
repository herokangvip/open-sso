package com.hk.sso.common.domain;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/11/8 17:32
 */
public class ClientParams {
    /**
     * 1：pc 登录态
     * 2：app
     * 3：h5
     * 4：wq
     */
    private String loginType = "";

    private String pcCookieValue = "";

    private String appCookieValue = "";

    private String h5CookieValue = "";

    private String wqCookieValue = "";

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPcCookieValue() {
        return pcCookieValue;
    }

    public void setPcCookieValue(String pcCookieValue) {
        this.pcCookieValue = pcCookieValue;
    }

    public String getAppCookieValue() {
        return appCookieValue;
    }

    public void setAppCookieValue(String appCookieValue) {
        this.appCookieValue = appCookieValue;
    }


    public String getH5CookieValue() {
        return h5CookieValue;
    }

    public void setH5CookieValue(String h5CookieValue) {
        this.h5CookieValue = h5CookieValue;
    }

    public String getWqCookieValue() {
        return wqCookieValue;
    }

    public void setWqCookieValue(String wqCookieValue) {
        this.wqCookieValue = wqCookieValue;
    }
}
