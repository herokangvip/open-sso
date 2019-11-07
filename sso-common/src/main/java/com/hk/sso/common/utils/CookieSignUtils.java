package com.hk.sso.common.utils;

import com.hk.sso.common.domain.AuthTicket;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/25 21:48
 */
public class CookieSignUtils {

    /**
     * 验证cookie签名
     *
     * @param authTicket
     * @return true：合法；false：非法
     */
    public static boolean validateSign(AuthTicket authTicket, String key) {
        if (authTicket == null || authTicket.getSign() == null || "".equals(authTicket.getSign().trim())) {
            return false;
        }
        //加密比解密效率更高
        //String decrypt = AESUtils.decrypt(authTicket.getSign(),key);
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid());
        String encrypt = AESUtils.encrypt(sb.toString(), key);
        return authTicket.getSign().equals(encrypt);
    }

    /**
     * cookie签名
     *
     * @param authTicket
     * @return
     */
    public static void createSign(AuthTicket authTicket, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid());
        String encrypt = AESUtils.encrypt(sb.toString(), key);
        authTicket.setSign(encrypt);
    }
}
