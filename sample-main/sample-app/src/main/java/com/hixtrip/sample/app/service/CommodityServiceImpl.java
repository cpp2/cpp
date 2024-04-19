package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.CommodityService;
import com.hixtrip.sample.client.commodity.dto.CommodityReq;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 商品应用层
 * @date 20240417
 * @author cpp
 */
@Component
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommodityDomainService commodityDomainService;

    @Override
    public BigDecimal getSkuPrice(CommodityReq commodityReq) {
        return commodityDomainService.getSkuPrice(commodityReq.getSkuId());
    }

}
