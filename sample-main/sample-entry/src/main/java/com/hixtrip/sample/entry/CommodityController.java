package com.hixtrip.sample.entry;

import com.hixtrip.sample.app.api.CommodityService;
import com.hixtrip.sample.client.commodity.dto.CommodityReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 商品
 */
@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    /**
     *
     * @param commodityReq 入参对象
     * @return 查询商品价格
     */
    @PostMapping(path = "/command/commodity/getSkuPrice")
    public BigDecimal order(@RequestBody CommodityReq commodityReq) {
        return commodityService.getSkuPrice(commodityReq);
    }


}
