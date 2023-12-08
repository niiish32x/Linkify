package com.nageoffer.shortlink.admin.common.constant;

/**
 * 短链接项目
 * Redis 缓存常量类
 */
public class RedisCacheConstant {
    // 冒号: 用于层次分离
    public static final String LOCK_USER_REGISTER_KEY = "short-link:user-register:";

    /**
     * 分组创建分布式锁
     */
    public static final String LOCK_GROUP_CREATE_KEY = "short-link:lock_group-create:%s";

}
