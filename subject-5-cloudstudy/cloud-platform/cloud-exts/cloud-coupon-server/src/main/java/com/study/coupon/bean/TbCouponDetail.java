package com.study.coupon.bean;

import lombok.Data;

import java.util.Date;

@Data
public class TbCouponDetail {

    private Long id;

    private Long couponId;

    private Integer couponDetailStatus;

    private Integer userId;

    private Long orderId;

    private Date startTime;

    private Date endTime;

}