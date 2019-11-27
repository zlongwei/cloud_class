package com.study.order.mapper;

import com.study.order.bean.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    List<Order> getOrderByUserId(Integer userId);

    List<Order> getAllOrder();

    List<Order> getOrdersByPage(@Param("start") int start, @Param("size") Integer size, @Param("key") String key);

    Long getCountByKey(@Param("key") String key);
}