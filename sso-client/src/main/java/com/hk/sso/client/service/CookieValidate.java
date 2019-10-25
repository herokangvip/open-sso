package com.hk.sso.client.service;

import com.hk.sso.common.domain.AuthTicket;
import com.hk.sso.common.utils.AESUtils;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/10/25 21:48
 */
public class CookieValidate {

    /**
     * 验证cookie签名
     * @param authTicket
     * @return true：合法；false：非法
     */
    public static boolean validate(AuthTicket authTicket) {
        if (authTicket == null || authTicket.getSign() == null || "".equals(authTicket.getSign().trim())) {
            return false;
        }
        String decrypt = AESUtils.decrypt(authTicket.getSign());
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getPin()).append(",").append(authTicket.getUid()).reverse();
        return decrypt.equals(sb.toString());
    }
}
