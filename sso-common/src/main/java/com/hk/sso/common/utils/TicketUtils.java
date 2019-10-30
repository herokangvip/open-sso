package com.hk.sso.common.utils;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.exception.OpenSsoException;

/**
 * @author heroking
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
            if (split.length != AuthTicket.class.getDeclaredFields().length) {
                return null;
            }
            Long uid = Long.parseLong(split[0]);
            String pin = split[1];
            Integer version = Integer.parseInt(split[2]);
            Long time = Long.parseLong(split[3]);
            Long expire = Long.parseLong(split[4]);
            String sign = split[5];
            return new AuthTicket(uid, pin, version, time, expire, sign);
        } catch (Exception e) {
            throw new OpenSsoException("sso decryptTicket error");
        }
    }

    public static String encryptTicket(AuthTicket authTicket, String cookieEncryptKey) {
        //加密ticket
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(authTicket.getUid()).append(",")
                    .append(authTicket.getPin()).append(",")
                    .append(authTicket.getVersion()).append(",")
                    .append(authTicket.getTime()).append(",")
                    .append(authTicket.getExpire()).append(",")
                    .append(authTicket.getSign()).append(",");
            return AESUtils.encrypt(sb.toString(), cookieEncryptKey);
        } catch (Exception e) {
            throw new OpenSsoException("sso encryptTicket error" + e.getMessage());
        }
    }
}
