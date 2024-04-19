package com.hixtrip.sample.domain.pay;

import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.OrderDetail;
import com.hixtrip.sample.domain.pay.enums.PayResultEnum;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.pay.strategy.PaymentResultFactory;
import com.hixtrip.sample.domain.pay.strategy.PaymentResultHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentFailure implements PaymentResultHandler, InitializingBean {

    @Autowired
    private OrderDomainService orderDomainService;
    @Autowired
    private InventoryDomainService inventoryDomainService;
    @Autowired
    private PayDomainService payDomainService;

    @Override
    public void afterPropertiesSet() throws Exception {
        PaymentResultFactory.register(PayResultEnum.PAYMENT_FAILED.getCode(), this);
    }

    @Override
    public String execute(CommandPay commandPay) {
        // 记录支付记录
        payDomainService.payRecord(commandPay);
        // 更新订单状态
        List<OrderDetail> orderDetails = orderDomainService.orderPayFail(commandPay);
        // 加回库存，减掉预占库存
        for (OrderDetail orderDetail : orderDetails) {
            inventoryDomainService.changeInventory(orderDetail.getSkuId(), -orderDetail.getQuantity(), -orderDetail.getQuantity(), 0);
        }
        return "支付失败";
    }

}
