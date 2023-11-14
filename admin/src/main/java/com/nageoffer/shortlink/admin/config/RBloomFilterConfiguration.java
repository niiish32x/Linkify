package com.nageoffer.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 */
@Configuration
public class RBloomFilterConfiguration {

    /**
     * 防止用户注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        // 自定义布隆过滤器的名称
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        // 初始化布隆过滤器
        // expectedInsertions：预估布隆过滤器存储的元素长度。
        // falseProbability：运行的误判率。
        cachePenetrationBloomFilter.tryInit(0, 0);
        return cachePenetrationBloomFilter;
    }
}
