package com.study.security.id.impl;

import com.study.security.id.IGenerateIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 订单号-通过redis的Incr实现
 *
 * @author allen
 */
@Service("redisGenerateIdService")
public class RedisGenerateIdServiceImpl implements IGenerateIdService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getOrderId() {
        // key = 系统名：+ 模块：+ 功能 ：+ key
        String key = "163:study:course:id";
        return redisTemplate.opsForValue().increment(key, -1);
    }

    @Override
    public Long getId() {
        String key = "163:study:base:id";
        return redisTemplate.opsForValue().increment(key, -1);
    }

}
