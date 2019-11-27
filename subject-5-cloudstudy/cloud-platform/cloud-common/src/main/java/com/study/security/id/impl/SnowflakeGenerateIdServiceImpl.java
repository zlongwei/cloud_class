package com.study.security.id.impl;

import com.study.security.id.IGenerateIdService;
import com.study.security.id.common.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单号-Twitter_Snowflake实现
 *
 * @author allen
 */
@Service("snowflakeGenerateIdService")
public class SnowflakeGenerateIdServiceImpl implements IGenerateIdService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Long getOrderId() {
        return snowflakeIdWorker.nextId();
    }

    @Override
    public Long getId() {
        return snowflakeIdWorker.nextId();
    }

}
