package com.study.coupon.mapper;

import com.study.coupon.bean.TbCoupon;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TbCouponMapper {

    int insert(TbCoupon record);

    int deleteById(Long couponId);

    List<TbCoupon> selectByExample(TbCoupon example);

    int updateByExampleSelective(@Param("record") TbCoupon record, @Param("example") TbCoupon example);

    // 时间范围内，数量够
    @Update("update tb_coupon set coupon_sum = coupon_sum - 1 where  coupon_id=#{couponId}  and coupon_sum > 0 " +
            " and now() >= start_time and now() <= end_time")
    int acquire(Long couponId);
}