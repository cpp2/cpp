package com.hixtrip.sample.client.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单的请求 入参
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandOderCreateDTO {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 运费
     */
    private BigDecimal shippingFee;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 订单详情
     */
    List<CommandOderDetailDTO> orderDetailList;


}
