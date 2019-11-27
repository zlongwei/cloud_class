package com.study.order.service;

import com.alibaba.fastjson.JSONObject;
import com.study.order.bean.Course;
import com.study.order.bean.Order;
import com.study.order.bean.UserStudy;
import com.study.order.client.CouponClient;
import com.study.order.client.CourseClient;
import com.study.order.common.OrderStatusEnum;
import com.study.order.mapper.OrderMapper;
import com.study.security.common.msg.TableResultResponse;
import com.study.security.id.IGenerateIdService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by andy on 2019/1/24.
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    CourseClient courseService;
    // 优惠券client
    @Autowired
    CouponClient couponClient;

    @Autowired
    IGenerateIdService redisGenerateIdService;

    @Autowired
    private MsgSender sender;

    /**
     * 生成订单，需要调用订单服务和优惠券服务
     *
     * @param order
     * @return
     */
    public int addOrder(Order order) {
        final Long orderId = redisGenerateIdService.getId();
        order.setId(orderId);
        order.setState(OrderStatusEnum.NOT_PAY.getCode()); // 设置状态为未支付...

        // 查询个人优惠券
        TableResultResponse result = couponClient.getCouponDetailByUserId();
        // 选择使用那张优惠券，就是得到优惠券id

        final int rows = orderMapper.insert(order);
        if (rows > 0) {
            // 使用优惠券
            couponClient.lockCoupon(1L, orderId, 1L);
            // 发起支付
            payOrder(orderId);
        }
        return rows;
    }

    public List<Order> getAllOrder() {
        return orderMapper.getAllOrder();
    }

    public List<Order> getOrdersByPage(Integer page, Integer size, String name) {
        int start = (page - 1) * size;
        return orderMapper.getOrdersByPage(start, size, name);
    }

    public Long getCountByKey(String name) {
        return orderMapper.getCountByKey(name);
    }

    public Order getOrder(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public int updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public int deleteOrder(String id) {
        return orderMapper.deleteByPrimaryKey(id);
    }

    public int orderCourse(int userId, Long courseId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderTime(new Date(System.currentTimeMillis()));
        Course course = courseService.getCourse(courseId);
        order.setState(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotal(course.getPrice());
        order.setCourseId(courseId);
        return this.addOrder(order);
    }

    public List<Order> getOrderByUserId(int userId) {
        List<Order> orders = orderMapper.getOrderByUserId(userId);
        orders.stream().forEach(item -> {
            Course course = courseService.getCourse(item.getCourseId());
            if (course != null) {
                item.setCourseName(course.getName());
            }
        });
        return orders;
    }

    /**
     * 订单支付
     * @param orderId
     * @return
     */
    @Hmily(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void payOrder(Long orderId) {
        Order orderInDb = this.getOrder(orderId);
        orderInDb.setState(OrderStatusEnum.PAYING.getCode()); // 设置状态为支付中...
        orderMapper.updateByPrimaryKeySelective(orderInDb);

        // TCC -> Try
        // 支付回调结果: 1:支付成功; 2:支付失败
        if ("支付回调结果".equals("支付")) {
            throw new HmilyRuntimeException("支付失败！");
        }
    }

    public int payOrderUsingMq(Long orderId) {
        Order newOrder = new Order();
        newOrder.setId(orderId);
        newOrder.setState(OrderStatusEnum.PAY_SUCCESS.getCode());
        this.updateOrder(newOrder);
        Order orderInDb = this.getOrder(orderId);
        UserStudy userStudy = new UserStudy();
        userStudy.setCourseId(orderInDb.getCourseId());
        userStudy.setUserId(orderInDb.getUserId());
        userStudy.setStartTime(new Date(System.currentTimeMillis()));
        String userStudyJsonStr = JSONObject.toJSONString(userStudy);
        sender.topicSend(userStudyJsonStr);
//        courseService.addUserStudy(userStudy);
        return 1;
    }

    public void confirmOrderStatus(Long orderId) {
        Order orderInDb = this.getOrder(orderId);
        orderInDb.setState(OrderStatusEnum.PAY_SUCCESS.getCode()); // 设置状态为支付中...
        orderMapper.updateByPrimaryKeySelective(orderInDb);

        UserStudy userStudy = new UserStudy();
        userStudy.setCourseId(orderInDb.getCourseId());
        userStudy.setUserId(orderInDb.getUserId());
        userStudy.setStartTime(new Date(System.currentTimeMillis()));
        courseService.addUserStudy(userStudy);

        // 课后作业
        log.info("=========进行订单confirm操作完成================");
    }

    public void cancelOrderStatus(Long orderId) {
        Order orderInDb = this.getOrder(orderId);
        orderInDb.setState(OrderStatusEnum.PAY_FAIL.getCode()); // 设置状态为支付失败...
        orderMapper.updateByPrimaryKeySelective(orderInDb);

        // 课后作业
        log.info("=========进行订单cancel操作完成================");
    }

}
