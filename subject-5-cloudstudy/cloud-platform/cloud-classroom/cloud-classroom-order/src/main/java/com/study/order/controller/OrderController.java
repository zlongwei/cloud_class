package com.study.order.controller;

import com.study.order.bean.Order;
import com.study.order.bean.RespBean;
import com.study.order.common.JwtUtil;
import com.study.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cloud/classroom/order")
public class OrderController<add> {

    @Autowired
    private OrderService orderService;

    /**
     * 订单支付入口
     *
     * @param orderId
     * @return
     */
    @GetMapping(value = "/pay/order")
    public RespBean payOrder(Long orderId) {
        // int userId = Integer.valueOf(JwtUtil.getUserId());

        orderService.payOrder(orderId);

        return RespBean.ok("购买成功!");
    }

    /**
     * 订单支付，mq方式
     *
     * @param orderId
     * @return
     */
    @GetMapping(value = "/pay/order/mq")
    public RespBean payOrderUsingMq(Long orderId) {
        int userId = Integer.valueOf(JwtUtil.getUserId());
        if (orderService.payOrderUsingMq(orderId) == 1) {
            return RespBean.ok("购买成功!");
        }
        return RespBean.error("购买失败!");
    }

    /**
     * 生成订单 - 分布式事务 tcc、
     * 1、生成订单，把订单号传给优惠券service
     * 2、选择优惠券，锁定优惠券，需要订单id和优惠卷id
     * 3、下单成功调用支付
     * 4、未支付（取消订单或者超时），释放优惠券
     *
     * @param order
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public RespBean addOrder(@RequestBody Order order) {
        /* 1. 查看课程，从课程详情页面点击加入课程；传递课程id到订单生成页面
           2. 在订单生成页面选择使用优惠券来减扣课程价格（总价-优惠=实价）
           3. 生成订单（下单），同时要调用订单服务生成订单和优惠券服务锁定优惠券；
           4. 下单成功，调用支付接口支付。支付成功回调（修改订单状态，扣账号余额(如有库存就扣减库存)；同样也需要分布式事务）
           5. 查看个人订单/订单详情
        */
        if (orderService.addOrder(order) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    /**
     * 根据id获取订单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    /**
     * 订单列表，所有订单
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Order> getAllOrders() {
        List<Order> orders = orderService.getAllOrder();
        return orders;
    }

    /**
     * 订单列表，分页显示
     *
     * @param page
     * @param limit
     * @param key
     * @return
     */
    @RequestMapping(value = "/page/list", method = RequestMethod.GET)
    public Map<String, Object> getOrdersByPage(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer limit, String key) {
        key = Optional.ofNullable(key).orElse("");
        Long count = orderService.getCountByKey(key);
        List<Order> orders = null;
        orders = orderService.getOrdersByPage(page, limit, key);
        Map<String, Object> map = new HashMap<>();
        map.put("orders", orders);
        map.put("count", count);
        return map;
    }

    /**
     * 更新订单信息
     *
     * @param Order
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public RespBean updateOrder(Order orders) {
        if (orderService.updateOrder(orders) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**
     * 根据id删除订单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RespBean deleteOrder(@PathVariable String id) {
        if (orderService.deleteOrder(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    /**
     * 订购课程
     *
     * @param courseId
     * @return
     */
    @GetMapping(value = "/course/course")
    public RespBean orderCourse(Long courseId) {
        int userId = Integer.valueOf(JwtUtil.getUserId());
        if (orderService.orderCourse(userId, courseId) == 1) {
            return RespBean.ok("订购成功!");
        }
        return RespBean.error("订购失败!");
    }

    /**
     * 获取当前用户订单
     *
     * @return
     */
    @GetMapping(value = "/mine/course")
    public List<Order> getMyOrders() {
        int userId = Integer.valueOf(JwtUtil.getUserId());
        List<Order> orders = orderService.getOrderByUserId(userId);
        return orders;
    }

}
