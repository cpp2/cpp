package com.hixtrip.sample.client.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单详情的请求入参
 * @date 20240417
 * @author cpp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandOderDetailDTO {

    /**
     * SkuId
     */
    private String skuId;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;


}
