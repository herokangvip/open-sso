package com.hk.sso.common.enums;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 22:36
 */
public enum SsoServerType {
    RPC(1,"公司内部rpc调用如dubbo"),
    HTTP(2,"http(https)协议");
    public int code;
    public String desc;

    SsoServerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
