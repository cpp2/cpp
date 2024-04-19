package com.hixtrip.sample.domain.commodity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 商品领域服务
 */
@Component
public class CommodityDomainService {

    public BigDecimal getSkuPrice(String skuId) {
        // 商品信息放缓存，取不到再查数据库
        return new BigDecimal(200);
    }
}
