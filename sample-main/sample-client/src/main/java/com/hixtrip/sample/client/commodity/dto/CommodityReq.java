package com.hixtrip.sample.client.commodity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品入参
 * @date 20240417
 * @author cpp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommodityReq {

    private String skuId;
}
