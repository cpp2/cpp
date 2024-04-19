package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.commodity.dto.CommodityReq;

import java.math.BigDecimal;

/**
 * 商品应用层
 * @date 20240417
 * @author cpp
 */
public interface CommodityService {


    /**
     * 查询sku价格
     * @param commodityReq
     * @return
     */
    BigDecimal getSkuPrice(CommodityReq commodityReq);

}
