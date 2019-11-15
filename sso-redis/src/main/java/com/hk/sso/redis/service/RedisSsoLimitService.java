package com.hk.sso.redis.service;

import com.hk.sso.common.domain.RedisKey;
import com.hk.sso.common.service.SsoLimitService;
import com.hk.sso.redis.factory.JedisFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * @author heroking
 * @version 1.0
 * @date 2019/11/7 15:09
 */
public class RedisSsoLimitService implements SsoLimitService {

    private JedisFactory jedisFactory;


    /**
     * @param pin
     * @return 是否限流 true：是
     */
    @Override
    public boolean isLimited(String pin) {
        ShardedJedis jedis = jedisFactory.getJedis();
        String key = RedisKey.SSO_CLIENT_LIMIT + pin;
        String limit = jedis.get(key);
        if (limit == null || "".equals(limit)) {
            jedis.setex(key, 1, "1");
            return false;
        } else {
            int limitNum = Integer.parseInt(limit);
            if (limitNum <= 1000) {
                limitNum++;
                jedis.setex(key, 1, String.valueOf(limitNum));
                return false;
            } else {
                return true;
            }
        }
    }


    public JedisFactory getJedisFactory() {
        return jedisFactory;
    }

    public void setJedisFactory(JedisFactory jedisFactory) {
        this.jedisFactory = jedisFactory;
    }
}
