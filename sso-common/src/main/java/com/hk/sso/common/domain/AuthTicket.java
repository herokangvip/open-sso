package com.hk.sso.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/24 18:01.
 */
public class AuthTicket implements Serializable {


    private static final long serialVersionUID = -1L;
    /**
     * 用户pin
     */
    private String pin;
    /**
     * sid
     */
    private String sid;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Long time;

    /**
     * 过期时间
     */
    private Long expire;

    private String loginType;
    /**
     * 签名
     */
    private String sign;

    public AuthTicket() {
    }

    public AuthTicket(String sid, String pin, Integer version, Long time, Long expire,String loginType, String sign) {
        this.version = version;
        this.sid = sid;
        this.sign = sign;
        this.pin = pin;
        this.time = time;
        this.loginType = loginType;
        this.expire = expire;
    }




    /**
     * 是否过期
     *
     * @return true:过期；false:未过期
     */
    public boolean isExpired() {
        Long expire = this.expire;
        Long time = this.time;
        Long current = System.currentTimeMillis();
        return time > current || expire < current;
    }

    /**
     * 是否非法
     *
     * @return true:非法；false:合法
     */
    public boolean isIllegal() {
        String pin = this.pin;
        if (pin == null || "".equals(pin.trim())) {
            return true;
        }
        String loginType = this.loginType;
        if (loginType==null||"".equals(loginType)) {
            return true;
        }
        String sid = this.sid;
        if (sid==null||"".equals(sid)) {
            return true;
        }
        Integer version = this.version;
        if (version < 0) {
            return true;
        }
        return false;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AuthTicket that = (AuthTicket) object;

        if (!Objects.equals(pin, that.pin)) return false;
        if (!Objects.equals(sid, that.sid)) return false;
        if (!Objects.equals(version, that.version)) return false;
        if (!Objects.equals(time, that.time)) return false;
        if (!Objects.equals(expire, that.expire)) return false;
        if (!Objects.equals(loginType, that.loginType)) return false;
        return Objects.equals(sign, that.sign);
    }

    @Override
    public int hashCode() {
        int result = pin != null ? pin.hashCode() : 0;
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (expire != null ? expire.hashCode() : 0);
        result = 31 * result + (loginType != null ? loginType.hashCode() : 0);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        return result;
    }
}
