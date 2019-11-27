package com.study.coupon.common;

public class CouponConstants {
    /**
     * 1-未使用 2-已使用 3-已过期 4-锁定  99-异常
     */
    public static enum CouponDetailStatusEnum {
        // 1-未使用
        UnUsed(1),
        // 2-已使用
        Used(2),
        // 3-已过期
        Lock(3),
        // 4-锁定
        Exp(4),
        // 99-异常
        Exception(99);

        public Integer getStatus() {
            return status;
        }

        private final Integer status;

        private CouponDetailStatusEnum(Integer status) {
            this.status = status;
        }


    }
}
