package com.hk.sso.common.utils;

import com.hk.sso.common.domain.AuthTicket;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 21:48
 */
public class CookieSignUtils {

    /**
     * 验证cookie签名
     * @param authTicket
     * @return true：合法；false：非法
     */
    public static boolean validateSign(AuthTicket authTicket,String key) {
        if (authTicket == null || authTicket.getSign() == null || "".equals(authTicket.getSign().trim())) {
            return false;
        }
        String decrypt = AESUtils.decrypt(authTicket.getSign(),key);
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid()).reverse();
        return decrypt.equals(sb.toString());
    }
    /**
     * cookie签名
     * @param authTicket
     * @return
     */
    public static String createSign(AuthTicket authTicket,String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid()).reverse();
        authTicket.setSign(sb.toString());
        return AESUtils.encrypt(authTicket.getSign(), key);
    }
}
