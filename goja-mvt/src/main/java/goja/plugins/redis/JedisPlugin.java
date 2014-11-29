/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package goja.plugins.redis;

import com.jfinal.plugin.IPlugin;
import goja.StringPool;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import static goja.GojaConfig.getProperty;
import static goja.GojaConfig.getPropertyToBoolean;
import static goja.GojaConfig.getPropertyToInt;
import static goja.GojaConfig.getPropertyToLong;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_ON_BORROW;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
import static org.apache.commons.pool2.impl.GenericObjectPoolConfig.DEFAULT_MAX_IDLE;
import static org.apache.commons.pool2.impl.GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
import static org.apache.commons.pool2.impl.GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

@SuppressWarnings("UnusedDeclaration")
public class JedisPlugin implements IPlugin {

    public static final String DEFAULT_HOST = StringPool.LOCAL_HOST;
    public static final int    DEFAULT_PORT = Protocol.DEFAULT_PORT;

    private final String host;
    private final int    port;
    private final int    timeout;

    public JedisPool pool;


    public JedisPlugin() {
        host = DEFAULT_HOST;
        port = Protocol.DEFAULT_PORT;
        timeout = Protocol.DEFAULT_TIMEOUT;
    }

    public JedisPlugin(String host) {
        this.host = host;
        port = Protocol.DEFAULT_PORT;
        timeout = Protocol.DEFAULT_TIMEOUT;
    }

    public JedisPlugin(String host, int port) {
        this.host = host;
        this.port = port;
        timeout = Protocol.DEFAULT_TIMEOUT;
    }

    public JedisPlugin(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }


    @Override
    public boolean start() {

        JedisShardInfo shardInfo = new JedisShardInfo(host, port, timeout);
        final String password = getProperty("redis.password");
        if (StringUtils.isNotBlank(password)) {
            shardInfo.setPassword(password);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(getPropertyToInt("redis.maxidle", DEFAULT_MAX_IDLE));
        poolConfig.setMaxTotal(getPropertyToInt("redis.maxtotal", DEFAULT_MAX_TOTAL));
        poolConfig.setMinEvictableIdleTimeMillis(getPropertyToLong("redis.minevictableidletimemillis", DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
        poolConfig.setMinIdle(getPropertyToInt("redis.minidle", DEFAULT_MIN_IDLE));
        poolConfig.setNumTestsPerEvictionRun(getPropertyToInt("redis.numtestsperevictionrun", DEFAULT_NUM_TESTS_PER_EVICTION_RUN));
        poolConfig.setSoftMinEvictableIdleTimeMillis(getPropertyToLong("redis.softminevictableidletimemillis", DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS));
        poolConfig.setTimeBetweenEvictionRunsMillis(getPropertyToLong("redis.timebetweenevictionrunsmillis", DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
        poolConfig.setTestWhileIdle(getPropertyToBoolean("redis.testwhileidle", DEFAULT_TEST_WHILE_IDLE));
        poolConfig.setTestOnReturn(getPropertyToBoolean("redis.testonreturn", DEFAULT_TEST_ON_RETURN));
        poolConfig.setTestOnBorrow(getPropertyToBoolean("redis.testonborrow", DEFAULT_TEST_ON_BORROW));

        pool = new JedisPool(poolConfig, shardInfo.getHost(), shardInfo.getPort(), shardInfo.getTimeout(), shardInfo.getPassword());
        JedisKit.init(pool);
        return true;
    }

    @Override
    public boolean stop() {
        try {
            pool.destroy();
        } catch (Exception ex) {
            System.err.println("Cannot properly close Jedis pool:" + ex);
        }
        pool = null;
        return true;
    }

}
