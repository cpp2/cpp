package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.OrderDetail;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.convertor.OrderDetailDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDetailPO;
import com.hixtrip.sample.infra.db.dataobject.OrderPO;
import com.hixtrip.sample.infra.db.mapper.OrderDetailMapper;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void saveOrderInfo(Order order) {
        OrderPO orderPO = OrderDOConvertor.INSTANCE.doToPO(order);
        // 保存订单表数据
        orderMapper.insert(orderPO);
        List<OrderDetail> orderItems = order.getOrderDetailList();
        List<OrderDetailPO> orderDetailPOList = new ArrayList<>();
        for (OrderDetail orderItem : orderItems) {
            OrderDetailPO orderDetailPO = OrderDetailDOConvertor.INSTANCE.doToPO(orderItem);
            orderDetailPOList.add(orderDetailPO);
        }
        // 保存订单详情表数据
        orderDetailMapper.insertBatchOrderDetail(orderDetailPOList);
    }

    @Override
    public void updatePaymentSuccess(String orderNo, Integer orderStatus, Integer payStatus) {
        orderMapper.updatePaymentSuccess(orderNo, orderStatus, payStatus);
    }

    @Override
    public void updatePaymentFailed(String orderNo, Integer payStatus) {
        orderMapper.updatePaymentFailed(orderNo, payStatus);
    }

    @Override
    public List<OrderDetail> getDetailByOrderNo(String orderNo) {
        List<OrderDetailPO> orderDetailPOList = orderDetailMapper.getByOrderNo(orderNo);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderDetailPO orderDetailPO : orderDetailPOList) {
            OrderDetail orderDetail = OrderDetailDOConvertor.INSTANCE.poToDO(orderDetailPO);
            orderDetailList.add(orderDetail);
        }
        return orderDetailList;
    }


}
