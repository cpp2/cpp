package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.OrderDetail;

import java.util.List;

/**
 *
 */
public interface OrderRepository {

    /**
     * 保存订单信息
     * @param order
     */
    void saveOrderInfo(Order order);

    /**
     * 通过订单编号更新订单支付成功的信息
     * @param orderNo
     * @param orderStatus
     * @param payStatus
     * @return
     */
    void updatePaymentSuccess(String orderNo, Integer orderStatus, Integer payStatus);

    /**
     * 通过订单编号更新订单支付成功的信息
     * @param orderNo
     * @param payStatus
     * @return
     */
    void updatePaymentFailed(String orderNo, Integer payStatus);

    /**
     * 通过订单编号获取订单详情
     * @param orderNo
     * @return
     */
    List<OrderDetail> getDetailByOrderNo(String orderNo);

}
