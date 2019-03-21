package com.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;
@Component
public class ElasticacheTool {
    private static ShardedJedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("localhost", 6379);
        jedisShardInfo1.setPassword("your token");

        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);

        pool = new ShardedJedisPool(config, list);
    }
    public String getByKey() {
        ShardedJedis jedis = pool.getResource();
        String keys = "myname";
        String vaule = jedis.set(keys, "your name");
        System.out.println(vaule);
        return  jedis.get("myname");
    }

}
