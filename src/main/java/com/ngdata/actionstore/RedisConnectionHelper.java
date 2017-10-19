package com.ngdata.actionstore;

import java.util.logging.Level;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * <ul>
 * <li>Title:RedisConnectionHelper</li>
 * <li>Description: To provide a single Jedis instance at any point of time.</li>
 * <li>Created: Oct 12, 2017 by: Anand N</li>
 * <li>Edited By Anand N.</li>
 * </ul>
 */
public class RedisConnectionHelper {

    /**
     * The COPYRIGHT.
     */
    public static final String COPYRIGHT = "@Copyright NGDATA NV";

    /**
     * The LOGGER.
     */
    private final Logger LOGGER = Logger.getLogger(RedisConnectionHelper.class.getName());



    /**
     * Maximum active connections to Redis instance The maxTotal
     */
    int maxTotal = 1000;

    /**
     * Number of connections to Redis that just sit there and do nothing! If maxIdle is set too low on heavily loaded systems it is possible you will see objects being destroyed and almost immediately new objects being created. This is a result of the
     * active threads momentarily returning objects faster than they are requesting them them, causing the number of idle objects to rise above maxIdle. The maxIdle
     */
    int maxIdle = 1000;

    /**
     * Minimum number of idle connections to Redis - these can be seen as always open and ready to serve The minIdle
     */
    int minIdle = 10;

    /**
     * Action to take when trying to acquire a connection and all connections are taken Fail-fast behaviour, if the value is false. Default behaviour, block the caller until a resource becomes available, depends on maxWait time.
     */
    boolean blockWhenExhausted = false;

    /**
     * The JedisPoolConfig instance.
     */
    final JedisPoolConfig poolConfig = buildPoolConfig();

    /**
     * The JedisPool instance. (Will be destroyed during application stoppage, check ApplicationThreadsManager)
     */
    static JedisPool jedisPool;

    /**
     * The databaseId.
     */
    private static int DATABASE_ID = 0;

    /**
     * Constructs a new <code>RedisConnectionHelper</code> instance.
     */
    public RedisConnectionHelper() {
    }

    /**
     * Returns a Jedis connection from the pool.
     * @return {@link Jedis}
     */
    public synchronized Jedis getJedisConnection() {
        try {
            if (jedisPool == null) {
                int port = Integer.parseInt(ApplicationProperties.getProperty("JEDIS_PORT"));
                DATABASE_ID = Integer.parseInt(ApplicationProperties.getProperty("DATABASE_ID")); 
                String jedisHost = ApplicationProperties.getProperty("JEDIS_HOST"); // get port number from properties file;
                jedisPool = new JedisPool(poolConfig, jedisHost, port);
            }
            Jedis jedis = jedisPool.getResource();
            jedis.select(DATABASE_ID);
            return jedis;
        } catch (JedisConnectionException e) {
            LOGGER.severe("Could not establish Redis connection." + e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * Simple method to parse int and if not available assign default value.
     * @param port int
     * @param property {@linkplain String}
     */
    private int getIntFromProperties(int defaultValue, String propertyName) {
    	int retVal = defaultValue;
        try {
        	retVal = Integer.parseInt(ApplicationProperties.getProperty(propertyName));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Could not read the integer value of the property '" + propertyName + "' from the config file.Setting default value " + retVal);
        }
        return retVal;
    }


    /**
     * Builds the config pool.
     * @return {@linkplain JedisPoolConfig}
     */
    private JedisPoolConfig buildPoolConfig() {
    	//get values of maxTotal, maxIdle, etc. from properties file
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(getIntFromProperties(maxTotal, "MAX_TOTAL"));
        poolConfig.setMaxIdle(getIntFromProperties(maxIdle, "MAX_IDLE"));
        poolConfig.setMinIdle(getIntFromProperties(minIdle, "MIN_IDLE") );
        poolConfig.setTestOnBorrow(new Boolean(ApplicationProperties.getProperty("TEST_ON_BORROW")));
        poolConfig.setTestOnReturn(new Boolean(ApplicationProperties.getProperty("TEST_ON_RETURN")));
        poolConfig.setTestWhileIdle(new Boolean(ApplicationProperties.getProperty("TEST_WHILE_IDLE")));
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        return poolConfig;
    }
}
