package com.hk.sso.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.hk.sso.common.domain.AuthTicket;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/11/6 17:32
 */
public class TicketRedisUtils {

    public static String prefix = "sso:ticket:";

    /**
     * 这里使用json+string存储，需要说明一下
     * 一般业务这个方案已经可以满足需求了，使用Hash存储的话如果对象里面好多属性都需要查询使用，可能效果还不如存整个json，
     * 因为hmget的复杂度是O(n),当然这个也不是绝对的，还要看系统瓶颈是不是在这里
     * 如果用户量和并发量很大的话，可以使用msgpack、kryo等序列化数据体积更小、速度更快
     * 一般来说redis的内存不会成为瓶颈，反倒是出流量并发量热key问题更应该关注
     *
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
        return RedisUtils.setEx(prefix + authTicket.getPin(), JSONObject.toJSONString(authTicket), ex);
    }

    public static AuthTicket getTicket(String pin) {
        String res = RedisUtils.get(prefix + pin);
        if (res == null || "".equals(res)) {
            return null;
        }
        return JSONObject.parseObject(res, AuthTicket.class);
    }
    public static Long delTicket(String pin) {
        return RedisUtils.delete(pin);
    }
}
