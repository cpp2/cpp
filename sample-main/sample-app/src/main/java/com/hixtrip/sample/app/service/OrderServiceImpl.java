package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.app.convertor.OrderDetailConvertor;
import com.hixtrip.sample.app.convertor.PayConvertor;
import com.hixtrip.sample.app.strategy.PaymentResultFactory;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandOderDetailDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.model.OrderDetail;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDomainService orderDomainService;
    @Autowired
    private InventoryDomainService inventoryDomainService;
    @Autowired
    private CommodityDomainService commodityDomainService;

    @Override
    public String createOrder(CommandOderCreateDTO commandOderCreateDTO) {
        List<CommandOderDetailDTO> orderDetailDTOList = commandOderCreateDTO.getOrderDetailList();
        if (CollectionUtils.isEmpty(orderDetailDTOList)) {
            throw new RuntimeException("订单商品不能为空");
        }
        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 1.校验订单商品库存是否充足，查询商品单价
        for (CommandOderDetailDTO orderItem : orderDetailDTOList) {
            OrderDetail orderDetail = OrderDetailConvertor.INSTANCE.dtoToDomain(orderItem);
            String skuId = orderItem.getSkuId();
            Integer quantity = orderItem.getQuantity();
            // 获取商品库存,判断库存
            Integer inventory = inventoryDomainService.getInventory(skuId);
            if (inventory < quantity) {
                throw new RuntimeException("商品库存不足");
            }
            // 查询商品价格
            BigDecimal skuPrice = commodityDomainService.getSkuPrice(skuId);
            orderDetail.setUnitPrice(skuPrice);
            orderDetailList.add(orderDetail);
        }
        // 2.扣减库存（lua脚本中还有判断商品库存是否充足）
        for (CommandOderDetailDTO orderItem : orderDetailDTOList) {
            String skuId = orderItem.getSkuId();
            Integer quantity = orderItem.getQuantity();
            // 扣减库存，减可售库存、增预占库存
            Boolean changeInventoryFlag = inventoryDomainService.changeInventory(skuId, quantity, quantity, 0);
            if (!changeInventoryFlag) {
                throw new RuntimeException("未成功扣减库存");
            }
        }
        // 3.保存订单和订单详情信息
        Order order = OrderConvertor.INSTANCE.dtoToDomain(commandOderCreateDTO);
        order.setOrderDetailList(orderDetailList);
        String orderNo = orderDomainService.createOrder(order);
        // 4.买家和卖家订单排名放入redis缓存rset结构（这边不写了）
        return orderNo;
    }

    @Override
    public String payCallback(CommandPayDTO commandPayDTO) {
        CommandPay commandPay = PayConvertor.INSTANCE.dtoToDomain(commandPayDTO);
        return PaymentResultFactory.getByStatus(commandPayDTO.getPayStatus()).execute(commandPay);
    }

}
