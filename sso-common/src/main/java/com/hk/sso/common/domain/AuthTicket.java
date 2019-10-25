package com.hk.sso.common.domain;

import java.util.Objects;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/24 18:01
 */
public class AuthTicket {

    /**
     * 用户pin
     */
    private String pin;
    /**
     * userId
     */
    private long uid;

    /**
     * 版本号
     */
    private int version;

    /**
     * 创建时间
     */
    private long time;

    /**
     * 过期时间
     */
    private long expire;

    /**
     * 签名
     */
    private String sign;

    public AuthTicket() {
    }

    public AuthTicket(long uid, String pin, int version, long time, long expire, String sign) {
        this.version = version;
        this.uid = uid;
        this.sign = sign;
        this.pin = pin;
        this.time = time;
        this.expire = expire;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AuthTicket that = (AuthTicket) object;

        if (uid != that.uid) return false;
        if (version != that.version) return false;
        if (time != that.time) return false;
        if (expire != that.expire) return false;
        return Objects.equals(pin, that.pin);
    }

    @Override
    public int hashCode() {
        int result = pin != null ? pin.hashCode() : 0;
        result = 31 * result + (int) (uid ^ (uid >>> 32));
        result = 31 * result + version;
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (int) (expire ^ (expire >>> 32));
        return result;
    }

    /**
     * 是否过期
     *
     * @return true:过期；false:未过期
     */
    public boolean isExpired() {
        long expire = this.expire;
        long time = this.time;
        long current = System.currentTimeMillis();
        return time > current || expire < current;
    }

    /**
     * 是否非法
     *
     * @return true:非法；false:合法
     */
    public boolean isIllegal() {
        String pin = this.pin;
        if (pin == null || pin.trim().equals("")) {
            return true;
        }
        long uid = this.uid;
        if (uid < 0) {
            return false;
        }
        int version = this.version;
        if (version < 0) {
            return false;
        }
        return true;
    }
}
