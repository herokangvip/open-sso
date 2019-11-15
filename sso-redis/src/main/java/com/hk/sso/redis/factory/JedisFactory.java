package com.hk.sso.redis.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/11/6 13:41
 */
public class JedisFactory {

    private static Logger logger = LoggerFactory.getLogger(JedisFactory.class);
    private static ShardedJedisPool jedisPool = null;

    @Value("${redis.url}")
    private String address;

    @Value("${redis.maxTotal:200}")
    private int maxTotal;
    @Value("${redis.maxIdle:8}")
    private int maxIdle;
    @Value("${redis.minIdle:2}")
    private int minIdle;
    @Value("${redis.maxWaitMillis:1000}")
    private int maxWaitMillis;
    @Value("${redis.timeBetweenEvictionRunsMillis:30000}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${redis.numTestsPerEvictionRun:10}")
    private int numTestsPerEvictionRun;
    @Value("${redis.minEvictableIdleTimeMillis:60000}")
    private int minEvictableIdleTimeMillis;


    @Value("${redis.testOnBorrow:true}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn:false}")
    private boolean testOnReturn;
    @Value("${redis.testWhileIdle:true}")
    private boolean testWhileIdle;




    @PostConstruct
    public void init() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxTotal);//最大连接数
            config.setMaxIdle(maxIdle);//最大空闲连接数,系统负载不是很大的话，默认值就可以
            config.setMinIdle(minIdle);//最小空闲连接数
            config.setMaxWaitMillis(maxWaitMillis);// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
            config.setTestOnBorrow(testOnBorrow);// 在获取连接的时候检查有效性, 默认false
            config.setTestOnReturn(testOnReturn);// 调用returnObject方法时，是否进行有效检查
            config.setTestWhileIdle(testWhileIdle);// Idle时进行连接扫描
            config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);     // 表示idle object evitor两次扫描之间要sleep的毫秒数
            config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);               // 表示idle object evitor每次扫描的最多的对象数
            config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);        // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义

            List<JedisShardInfo> shardInfoList = new LinkedList<>();
            String[] addressArr = address.split(",");
            for (String s : addressArr) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(s);
                shardInfoList.add(jedisShardInfo);
            }
            jedisPool = new ShardedJedisPool(config, shardInfoList);
            ShardedJedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.get("init");
            } finally {
                if (jedis != null){
                    jedis.close();
                }
            }
            logger.info("====jedis pool init success");
        } catch (Exception e) {
            logger.error("====jedis连接池初始化异常:{}", e);
            throw new RuntimeException("====jedis连接池初始化异常");
        }
    }

    public ShardedJedis getJedis() {
        return jedisPool.getResource();
    }
}
