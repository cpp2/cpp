package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.order.enums.OrderStatusEnum;
import com.hixtrip.sample.domain.order.enums.PayStatusEnum;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.OrderDetail;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单领域服务
 * todo 只需要实现创建订单即可
 */
@Component
public class OrderDomainService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 创建待付款订单
     */
    public String createOrder(Order order) {
        // 生成订单编号（按规则生成订单编号，后面几位取用户id后几位）
        String orderNo = order.generateOrderNo();
        List<OrderDetail> orderItems = order.getOrderDetailList();
        for (OrderDetail orderItem : orderItems) {
            // 这边id先用uuid
            orderItem.setId(UUID.randomUUID().toString().replace("-", ""));
            orderItem.setOrderNo(orderNo);
            orderItem.setCreateBy(order.getUserId());
            orderItem.setUpdateBy(order.getUserId());
        }

        // 组装订单基础信息
        order.setOrderNo(orderNo);
        order.setOrderStatus(OrderStatusEnum.UNPAID.getCode());
        order.setPayStatus(PayStatusEnum.UNPAID.getCode());
        order.setOrderTime(LocalDateTime.now());
        order.setCreateBy(order.getUserId());
        order.setUpdateBy(order.getUserId());
        // 充血模型设计
        order.setPaymentAmount(order.calcPaymentAmount());
        order.setTotalAmount(order.calcTotalAmount());
        // 保存订单信息，订单详情信息
        orderRepository.saveOrderInfo(order);

        // 保存商家订单信息(这边不写了)

        return orderNo;
    }

    /**
     * todo 需要实现
     * 待付款订单支付成功
     */
    public List<OrderDetail> orderPaySuccess(CommandPay commandPay) {
        orderRepository.updatePaymentSuccess(commandPay.getOrderNo(), OrderStatusEnum.PENDING_SHIPMENT.getCode(), PayStatusEnum.PAYMENT_SUCCESS.getCode());
        List<OrderDetail> orderDetailList = orderRepository.getDetailByOrderNo(commandPay.getOrderNo());
        return orderDetailList;
    }

    /**
     * todo 需要实现
     * 待付款订单支付失败
     */
    public List<OrderDetail> orderPayFail(CommandPay commandPay) {
        orderRepository.updatePaymentFailed(commandPay.getOrderNo(), PayStatusEnum.PAYMENT_FAILED.getCode());
        List<OrderDetail> orderDetailList = orderRepository.getDetailByOrderNo(commandPay.getOrderNo());
        return orderDetailList;
    }
}
