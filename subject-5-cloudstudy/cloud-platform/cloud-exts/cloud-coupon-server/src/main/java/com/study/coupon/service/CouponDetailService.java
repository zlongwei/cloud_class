package com.study.coupon.service;

import com.study.coupon.bean.TbCouponDetail;
import com.study.coupon.mapper.TbCouponDetailMapper;
import com.study.coupon.mapper.TbCouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.study.coupon.common.CouponConstants.CouponDetailStatusEnum;

import java.util.List;

@Service
@Slf4j
public class CouponDetailService {

    @Autowired
    TbCouponDetailMapper tbCouponDetailMapper;

    @Autowired
    TbCouponMapper tbCouponMapper;

    /**
     * 将订单与该用户对应的优惠券绑定
     *
     * @param couponDetailId 用户领取的优惠券记录
     * @param orderId        订单号
     * @param userId         用户ID
     */
    public TbCouponDetail lockCouponDetail(Long couponDetailId, Long orderId, String userId) {
        TbCouponDetail example = new TbCouponDetail();
        example.setCouponId(couponDetailId);
        example.setCouponDetailStatus(CouponDetailStatusEnum.UnUsed.getStatus());
        example.setUserId(Integer.parseInt(userId));

        TbCouponDetail tbCouponDetailUpdate = new TbCouponDetail();
        tbCouponDetailUpdate.setOrderId(orderId);
        tbCouponDetailUpdate.setCouponDetailStatus(CouponDetailStatusEnum.Used.getStatus());
        int result = tbCouponDetailMapper.updateByExampleSelective(tbCouponDetailUpdate, example);
        if (result == 1) { // 等于1代表修改成功
            // 查询优惠详情
            return tbCouponDetailMapper.selectByPrimaryKey(couponDetailId);
        }
        return null;
    }

    /**
     * 将订单与该用户对应的优惠券解除绑定
     *
     * @param couponDetailId 用户领取的优惠券记录
     * @param orderId        订单号
     * @param userId         用户ID
     */
    public TbCouponDetail releaseCouponDetail(Long couponDetailId, Long orderId, String userId) {
        TbCouponDetail example = new TbCouponDetail();
        example.setId(couponDetailId);
        example.setOrderId(orderId);
        example.setCouponDetailStatus(CouponDetailStatusEnum.Used.getStatus());
        example.setUserId(Integer.parseInt(userId));

        TbCouponDetail tbCouponDetailUpdate = new TbCouponDetail();
        tbCouponDetailUpdate.setCouponDetailStatus(CouponDetailStatusEnum.UnUsed.getStatus());
        int result = tbCouponDetailMapper.updateByExampleSelective(tbCouponDetailUpdate, example);
        if (result == 1) { // 等于1代表修改成功
            // 查询优惠详情
            return tbCouponDetailMapper.selectByPrimaryKey(couponDetailId);
        }
        return null;
    }

    /**
     * 能正常使用的优惠券（具体规则根据产品需求决定）
     *
     * @param userId
     * @return
     */
    public List<TbCouponDetail> getUnUsedCouponDetailByUserId(String userId) {
        TbCouponDetail example = new TbCouponDetail();
        example.setCouponDetailStatus(CouponDetailStatusEnum.Used.getStatus());
        example.setUserId(Integer.parseInt(userId));
        List<TbCouponDetail> tbCouponDetails = tbCouponDetailMapper.selectByExample(example);
        return tbCouponDetails;
    }
}
