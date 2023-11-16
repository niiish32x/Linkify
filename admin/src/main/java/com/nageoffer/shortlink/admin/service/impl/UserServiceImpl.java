package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
//import org.springframework.beans.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.nageoffer.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;


/*
*   用户接口实现层
* */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    // 布隆过滤器
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    // 分布式锁
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);

        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        UserRespDTO result = new UserRespDTO();
        BeanUtil.copyProperties(userDO,result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        // 直接使用布隆过滤器 判断用户名是否存在啊
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        // 存在返回true
        // 表示用户名已经存在 不能再注册相同用户名 所以抛出异常
        if (hasUsername(requestParam.getUsername())){
            throw new ClientException(USER_NAME_EXIST);
        }


        // 如果不加 username 就成全局锁了
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());

        try {
            if (lock.tryLock()){
                // 如果成功拿到锁 则进行用户注册
                int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if(inserted < 1){
                    throw new ClientException(USER_SAVE_ERROR);
                }

                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }finally {
            // 最后释放锁
            lock.unlock();
        }

    }

    @Override
    public List<UserRespDTO> getAllUser() {
        List<UserDO> userRespDTOS = baseMapper.selectList(null);
        List<UserRespDTO> results = new ArrayList<>();
        for (UserDO userDO : userRespDTOS){
            if (userDO == null){
                continue;
            }
            UserRespDTO result = new UserRespDTO();
            BeanUtil.copyProperties(userDO,result);
            results.add(result);
        }
        return results;
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO 验证当前用户名是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam,UserDO.class),updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);

        UserDO userDO = baseMapper.selectOne(queryWrapper);

        // userDO == null 证明不存在 抛出异常
        if (userDO == null){
            throw new ClientException("用户不存在");
        }

        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + requestParam.getUsername());
        if (hasLogin != null && hasLogin){
            throw new ClientException("用户已登录");
        }

        /**
         * Hash
         * Key: login 用户名
         * Value:
         *  Key: token标识
         *  Val: Json 字符串(用户信息)_
         */

        String uuid = UUID.randomUUID().toString();
//        // 把UUID 作为key 设置30分钟的有效期
//        stringRedisTemplate.opsForValue().set(uuid, JSON.toJSONString(userDO),30L, TimeUnit.MINUTES);
//
//        Map<String, Object> userInfoMap = new HashMap<>();
//        userInfoMap.put("token",JSON.toJSONString(userDO));
        stringRedisTemplate.opsForHash().put("login_"+ requestParam.getUsername(),uuid,JSON.toJSONString(userDO));
        // 设置过期时间
        stringRedisTemplate.expire("login_"+requestParam.getUsername(),30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_" + username,token) != null;
    }

    @Override
    public void logout(String username, String token) {
        // 先验证是否登录
        if (checkLogin(username,token)){
            stringRedisTemplate.delete("login_" + username);
            return;
        }

        throw new ClientException("用户Token不存在或用户未登录");
    }
}







