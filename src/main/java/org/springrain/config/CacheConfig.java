package org.springrain.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存的配置,自定义 cacheManager 用于实现替换.
 * 
 * @author caomei
 *
 */
@Configuration("configuration-CacheConfig")
public class CacheConfig {

    //reids的IP和端口,如果是集群,使用逗号隔开,例如 redis://127.0.0.1:6379,redis://127.0.0.1:6378 
    @Value("${springrain.redis.hostport:redis://127.0.0.1:6379}")
    private String redisHostPort;
    
    @Value("${springrain.redis.timeout:0}")
    private Long cacheTimeOut=0L;

    //最大连接数
    @Value("${springrain.redis.pool.max-active:1024}")
    private Integer maxActive = 1024;

    //最小空闲数
    @Value("${springrain.redis.pool.min-idle:200}")
    private Integer minIdle = 200;
    
    //密码 默认 "" 
    @Value("${springrain.redis.password:}")
    private String password=null;
    

    /**
     * 基于内存的cacheManager
     * 
     * @return
     * @throws IOException
     */

    @Bean("cacheManager")
    public CacheManager cacheManager() {
        CacheManager cacheManager = new ConcurrentMapCacheManager();

        return cacheManager;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // --------基于redis的cacheManager 开始--------//

    /**
     * 基于redis的cacheManager,使用redisson客户端
     * 
     * @return
     * @throws IOException
     */
    /*
    @Bean("cacheManager")
    public CacheManager cacheManager() {
        
        FrameRedissonSpringCacheManager cacheManager= new FrameRedissonSpringCacheManager(redissonClient());
        cacheManager.setCacheTimeOut(cacheTimeOut);
        
        return cacheManager;
    }

    @Bean("redissonClient")
    public RedissonClient redissonClient()  {

        // 连接超时时间
        int connectTimeOut = 10000;
        // 重试次数
        int retryAttempts = 6;

        if (StringUtils.isBlank(redisHostPort)) {
            return null;
        }
        redisHostPort=redisHostPort.trim();
        
        Config config = new Config();
        String[] ipports = redisHostPort.split(",");

        if (ipports.length <= 1) {// 单机redis模式
            
            SingleServerConfig useSingleServer = config.useSingleServer();
            
            useSingleServer.setAddress(redisHostPort).setConnectTimeout(connectTimeOut)
                    .setRetryAttempts(retryAttempts).setConnectionPoolSize(maxActive)
                    .setConnectionMinimumIdleSize(minIdle);
            
            if(StringUtils.isNotBlank(password)) {
                useSingleServer.setPassword(password);
            }
          
            
            
        } else {// redis 集群.默认读slave
            ClusterServersConfig useClusterServers = config.useClusterServers();
            
            useClusterServers.addNodeAddress(ipports).setConnectTimeout(connectTimeOut)
                    .setRetryAttempts(retryAttempts).setMasterConnectionPoolSize(maxActive)
                    .setSlaveConnectionPoolSize(maxActive).setMasterConnectionMinimumIdleSize(minIdle)
                    .setSlaveConnectionMinimumIdleSize(minIdle).setReadMode(ReadMode.SLAVE).setScanInterval(3000);
            
            if(StringUtils.isNotBlank(password)) {
                useClusterServers.setPassword(password);
            }
            
            
        }
        // JDK的序列化
        // config.setCodec(new SerializationCodec());
        
        //fst序列化
        config.setCodec(new FstCodec());

        return Redisson.create(config);
    }
    
    
    //redis的操作,声明注入,避免出现依赖错误
    @Bean("redisOperation")
    public RedisOperation redisOperation() {
        RedisOperation redisOperation=new RedisOperation();
        redisOperation.setRedissonClient(redissonClient());
        return redisOperation;
    }
    */
    // --------基于redis的cacheManager 结束--------//
    
    
    
    
    
    
    
    
    

}
