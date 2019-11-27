package com.study.coupon.service;

import com.study.coupon.bean.TbCoupon;
import com.study.coupon.bean.TbCouponDetail;
import com.study.coupon.common.CouponConstants;
import com.study.coupon.mapper.TbCouponDetailMapper;
import com.study.coupon.mapper.TbCouponMapper;
import com.study.security.id.IGenerateIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    TbCouponMapper tbCouponMapper;

    @Autowired
    TbCouponDetailMapper tbCouponDetailMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DefaultRedisScript<Long> couponAcquireRedisScript;

    @Autowired
    IGenerateIdService redisGenerateIdService;

    /**
     * 秒杀具体实现
     *
     * @param couponId 优惠券ID
     * @param userId   用户ID
     * @return
     */
    public boolean acquire(Long couponId, final String userId) {
        // 0、 此用户不能重复领取（布隆过滤器）
        // 1、令牌桶策略，过滤无效用户

        Long value = stringRedisTemplate.execute(couponAcquireRedisScript,
                Arrays.asList("{" + couponId + "}.records", userId, "{" + couponId + "}.tokenlists"), "x");
        if (value == null || "".equals(value) || value == 0) {
            // 没拿到token，没有领取权限
            return false;
        }

        // TODO 具体数据库操作，可以用MQ异步处理，提示用户，稍后将发放到您的账户
        // 1. 数量减一
        int count = tbCouponMapper.acquire(couponId);
        if (count != 1) {
            return false;
        }
        // 2. 增加记录
        TbCouponDetail tbCouponDetail = new TbCouponDetail();
        tbCouponDetail.setId(redisGenerateIdService.getId());
        tbCouponDetail.setCouponId(couponId);
        tbCouponDetail.setUserId(Integer.valueOf(userId));
        tbCouponDetail.setCouponDetailStatus(CouponConstants.CouponDetailStatusEnum.UnUsed.getStatus()); // 0 - 未使用
        tbCouponDetailMapper.insert(tbCouponDetail);
        return true;
    }

    /**
     * 新增
     */
    public void addCoupon(TbCoupon tbCoupon) {
        tbCoupon.setId(redisGenerateIdService.getId());
        tbCouponMapper.insert(tbCoupon);
    }

    /**
     * 删除
     */
    public void removeCoupon(Long couponId) {
        tbCouponMapper.deleteById(couponId);
    }

    /**
     * 查询所有有效期内的 --- 无其他条件
     *
     * @return
     */
    public List<TbCoupon> queryCouponList() {
        TbCoupon example = new TbCoupon();
        //example.setStartTime(new Date());
        //example.setEndTime(new Date());
        List<TbCoupon> tbCoupons = tbCouponMapper.selectByExample(example);
        return tbCoupons;
    }

    /**
     * 查询优惠券内容
     * TODO 缓存呢？怎么加？
     *
     * @return 优惠券内容
     */
    public TbCoupon queryCouponById(Long couponId) {
        TbCoupon example = new TbCoupon();
        example.setId(couponId);
        List<TbCoupon> tbCoupons = tbCouponMapper.selectByExample(example);
        return tbCoupons == null ? null : tbCoupons.get(0);
    }
}
