package com.hk.sso.common.enums;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 21:51
 */
public enum CookieSignType {
    CLIENT(1,"client校验"),
    SERVER(2,"server校验");
    public int code;
    public String desc;

    CookieSignType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
