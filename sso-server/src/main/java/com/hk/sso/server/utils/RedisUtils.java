package com.hk.sso.server.utils;

import com.hk.sso.server.factory.JedisFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/11/6 17:21
 */
public class RedisUtils {

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = JedisFactory.getJedis();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static String setEx(String key, String value, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = JedisFactory.getJedis();
            return jedis.setex(key, seconds, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = JedisFactory.getJedis();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Long delete(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = JedisFactory.getJedis();
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
