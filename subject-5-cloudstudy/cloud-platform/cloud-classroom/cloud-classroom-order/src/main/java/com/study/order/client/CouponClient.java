package com.study.order.client;

import com.study.security.common.msg.TableResultResponse;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 优惠卷 rpc
 */
@FeignClient(value = "cloud-coupon-server")
public interface CouponClient {

    /**
     * 锁定优惠
     */
    @GetMapping(value = "/coupon/detail/op/lockCoupon")
    public Object lockCoupon(@RequestParam Long couponDetailId, @RequestParam Long orderId, @RequestParam Long orderUserId);

    /**
     * 获取用户个人可使用的优惠券
     */
    @GetMapping(value = "/coupon/detail/op/getCouponDetail")
    public TableResultResponse getCouponDetailByUserId();

}


