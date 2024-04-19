package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;

/**
 * 订单的service层
 */
public interface OrderService {


    /**
     * 创建订单
     * @param commandOderCreateDTO
     * @return
     */
    String createOrder(CommandOderCreateDTO commandOderCreateDTO);

    /**
     * 支付回调
     * @param commandPayDTO
     * @return
     */
    String payCallback(CommandPayDTO commandPayDTO);

}
