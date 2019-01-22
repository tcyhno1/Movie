package com.yuhao.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 这个是通过哨兵连接远程百度云
 */
public class RedisUtilSentinel {

    private static final JedisSentinelPool pool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);

        //java通过对接redis哨兵sentinels，来连接主redis.这是因为代码不知道哪个redis是master但是哨兵知道。
        Set<String> sentinels = new HashSet<>(Arrays.asList(
                "182.61.59.59:26379",
                "182.61.59.59:26380",
                "182.61.59.59:26381"
        ));
        pool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig, "yuhao123456");

    }

    public static void main(String[] args) throws Exception {

        try (Jedis jedis = pool.getResource()) {
            jedis.set("key1", "111");
            System.out.println(jedis.get("key1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
