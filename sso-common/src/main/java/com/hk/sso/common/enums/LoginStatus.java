package com.hk.sso.common.enums;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/25 18:31
 */
public enum LoginStatus {
    SUCCESS(1,"success"),
    EXPIRED(2,"登录已过期"),
    PASSWORD_CHANGED(3,"密码已修改请重新登录"),
    BLACK_USER(4,"黑名单用户"),
    ILLEGAL_USER(5,"用户非法"),
    SIGN_ERROR(6,"cookie签名错误"),
    SERVER_ERROR(7,"服务器错误"),
    LOGOUT(8,"退出登录、没有登录态"),
    LIMITED(9,"限流");
    public int code;
    public String desc;

    LoginStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}


