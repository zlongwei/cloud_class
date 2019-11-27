package com.study.coupon.controller;

import com.study.coupon.bean.TbCoupon;
import com.study.coupon.service.CouponService;
import com.study.security.common.context.BaseContextHandler;
import com.study.security.common.msg.BaseResponse;
import com.study.security.common.msg.TableResultResponse;
import com.study.security.lock.RedissLockConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 秒杀页面访问接口
 */
@Api(description = "优惠券接口")
@RestController
@RequestMapping("/coupon/op/")
@Slf4j
public class CouponController {
    @Autowired
    CouponService couponService;

    @Autowired
    RedissLockConfig redissLock;

    @ApiOperation(value = "领取优惠券",
            notes = "领取优惠券，简单规则：一个人同一张优惠券只能领取一次。如果并发很大，就提示稍后发放到您的账户(异步)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/acquire", method = RequestMethod.POST)
    public Object acquire(Long couponId) throws Exception {
        // http请求，后台就是一个thread线程 去 调用service方法
        String userId = BaseContextHandler.getUserID();
        // TODO 测试代码，固定用户ID
        // userId = "99999";

        boolean success = false;
        // 获取redisson分布式锁
        RLock lock = redissLock.getRedissonLock("couponLock");
        try {
            // 开始枷锁
            lock.lock();  // 3秒 < 5秒, 开启另一个线程：查看锁是否将要超时，它会向redis申请延长超时时间
            // 执行业务
            success = couponService.acquire(couponId, userId);
        } catch (Exception e) {
            log.error("领取优惠券服务的时候报错了，错误信息为:{}", e.getMessage());
        } finally {
            // 解锁
            lock.unlock();
        }

        if (!success) {
            return new BaseResponse(400, "没抢到优惠券");
        }
        return new BaseResponse(200, "操作成功");
    }

    @ApiOperation(value = "新增优惠", notes = "给运营系统的接口")
    @ApiImplicitParam(name = "tbCoupon", value = "优惠实体bean", required = true, dataType = "TbCoupon")
    @RequestMapping(value = "addCoupon", method = RequestMethod.POST)
    public Object addCoupon(@RequestBody TbCoupon tbCoupon) {
        couponService.addCoupon(tbCoupon);
        return new BaseResponse(200, "操作成功");
    }

    @ApiOperation(value = "移除优惠", notes = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId", value = "优惠券ID", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/removeCoupon", method = RequestMethod.POST)
    public Object removeCoupon(Long couponId) {
        couponService.removeCoupon(couponId);
        return new BaseResponse(200, "操作成功");
    }

    @ApiOperation(value = "查询优惠券列表", notes = "根据实际业务决定查询条件")
    @RequestMapping(value = "/releaseCoupon", method = RequestMethod.GET)
    public TableResultResponse queryCouponList() {
        List<TbCoupon> tbCoupons = couponService.queryCouponList();
        TableResultResponse<TbCoupon> tableResultResponse = new TableResultResponse(tbCoupons.size(), tbCoupons);
        tableResultResponse.setStatus(200);
        return tableResultResponse;
    }

}
