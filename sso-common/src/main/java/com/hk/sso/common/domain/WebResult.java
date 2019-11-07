package com.hk.sso.common.domain;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/26 18:58
 */
public class WebResult<T> {
    private int code;
    private String msg;
    private T data;

    public WebResult() {
    }

    public WebResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
