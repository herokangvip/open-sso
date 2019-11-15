package com.hk.sso.common.utils;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.exception.OpenSsoException;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/26 17:28
 */
public class TicketUtils {
    public static AuthTicket decryptTicket(String token, String cookieEncryptKey) {
        //解密ticket
        try {
            String decrypt = AESUtils.decrypt(token, cookieEncryptKey);
            if ("".equals(decrypt)) {
                return null;
            }
            String[] split = decrypt.split(",");
            //加解密组合方式用户可以自定义，比如此处不允许用户pin等属性有逗号
            if (split.length != AuthTicket.class.getDeclaredFields().length -1) {
                return null;
            }
            String sid = split[0];
            String pin = split[1];
            Integer version = Integer.parseInt(split[2]);
            Long time = Long.parseLong(split[3]);
            Long expire = Long.parseLong(split[4]);
            String loginType = split[5];
            String sign = split[6];
            return new AuthTicket(sid, pin, version, time, expire,loginType, sign);
        } catch (Exception e) {
            throw new OpenSsoException("sso decryptTicket error");
        }
    }

    public static String encryptTicket(AuthTicket authTicket, String cookieEncryptKey) {
        //加密ticket
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(authTicket.getSid()).append(",")
                    .append(authTicket.getPin()).append(",")
                    .append(authTicket.getVersion()).append(",")
                    .append(authTicket.getTime()).append(",")
                    .append(authTicket.getExpire()).append(",")
                    .append(authTicket.getLoginType()).append(",")
                    .append(authTicket.getSign()).append(",");
            return AESUtils.encrypt(sb.toString(), cookieEncryptKey);
        } catch (Exception e) {
            throw new OpenSsoException("sso encryptTicket error" + e.getMessage());
        }
    }
}
