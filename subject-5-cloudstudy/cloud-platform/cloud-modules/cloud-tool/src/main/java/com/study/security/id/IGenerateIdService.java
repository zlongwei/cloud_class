package com.study.security.id;

/**
 * 订单号和id生成器
 *
 * @author allen
 */
public interface IGenerateIdService {
    /**
     * 获取订单号
     */
    public Long getOrderId();

    /**
     * 获取id
     *
     * @return
     */
    public Long getId();
}
