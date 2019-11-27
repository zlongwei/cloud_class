package com.study.coupon.mapper;

import com.study.coupon.bean.TbCouponDetail;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TbCouponDetailMapper {

    int insert(TbCouponDetail record);

    int deleteByTbCouponDetail(TbCouponDetail entity);

    List<TbCouponDetail> selectByExample(TbCouponDetail example);

    TbCouponDetail selectByPrimaryKey(Long couponDetailId);

    int updateByExampleSelective(@Param("record") TbCouponDetail record, @Param("example") TbCouponDetail example);

    int updateByPrimaryKeySelective(TbCouponDetail record);

    int updateByPrimaryKey(TbCouponDetail record);
}