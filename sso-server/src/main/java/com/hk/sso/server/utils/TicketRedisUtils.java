package com.hk.sso.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.hk.sso.common.domain.AuthTicket;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/11/6 17:32
 */
public class TicketRedisUtils {

    public static String prefix = "sso:ticket:";

    /**
     * 这里用的string也可根据情况使用hash
     * @param authTicket
     * @return
     */
    public static String setTicket(AuthTicket authTicket) {
        if (authTicket == null) {
            return "";
        }
        long timeMillis = System.currentTimeMillis();
        long expire = authTicket.getExpire();
        long time = expire - timeMillis;
        if (time <= 0) {
            return "";
        }
        int ex = (int) time / 1000;
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getSid()).append(":").append(authTicket.getPin()).append(":").append(authTicket.getLoginType());
        return RedisUtils.setEx(sb.toString(), JSONObject.toJSONString(authTicket), ex);
    }

    public static AuthTicket getTicket(AuthTicket authTicket) {
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getSid()).append(":").append(authTicket.getPin()).append(":").append(authTicket.getLoginType());
        String res = RedisUtils.get(sb.toString());
        if (res == null || "".equals(res)) {
            return null;
        }
        return JSONObject.parseObject(res, AuthTicket.class);
    }

    public static Long delTicket(AuthTicket authTicket) {
        StringBuilder sb = new StringBuilder();
        sb.append(authTicket.getSid()).append(":").append(authTicket.getPin()).append(":").append(authTicket.getLoginType());
        return RedisUtils.delete(sb.toString());
    }
}
