package com.daicy.crawler.extension;

import com.daicy.crawler.common.utils.JsonUtils;
import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.proxy.Proxy;
import com.daicy.crawler.core.proxy.ProxyProvider;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.extension
 * @date:8/22/20
 */
public class RedisProxyProvider implements ProxyProvider {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected JedisPool pool;

    private List<Proxy> proxies;

    public RedisProxyProvider(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {
        //Donothing
    }

    @Override
    public Proxy getProxy(Task task) {
        String redisKey = "scrapy_redis:proxy";
        logger.debug("start getProxy ");
        Jedis jedis = null;
        List<String> hostList;
        try {
            jedis = pool.getResource();
            hostList = jedis.lrange(redisKey, 0, 10);
            while (CollectionUtils.isEmpty(hostList)) {
                try {
                    logger.warn("getProxy sleep:{}", task.getSite().getDomain());
                    Thread.sleep(1000);
                    hostList = jedis.lrange(redisKey, 0, 10);
                } catch (InterruptedException e) {
                    logger.error("getProxy sleep error", e);
                }
            }
        } finally {
            jedis.close();
        }

        proxies = hostList.stream().map(httpProxy -> {
            String host = httpProxy;
            int port = 0;
            int index = httpProxy.indexOf(":");
            if (index != -1) {
                host = httpProxy.substring(0, index);
                port = Integer.parseInt(httpProxy.substring(index + 1));
            }
            return new Proxy(host, port);
        }).collect(Collectors.toList());
        logger.debug("finish getProxy ");
        return proxies.get(ThreadLocalRandom.current().nextInt(proxies.size()));
    }

}

