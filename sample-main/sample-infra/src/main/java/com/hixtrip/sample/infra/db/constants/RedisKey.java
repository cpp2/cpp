package com.hixtrip.sample.infra.db.constants;

/**
 * 缓存key常量类
 * @date 20240417
 * @author cpp
 */
public interface RedisKey {

    /**
     * 库存缓存
     */
    String INVENTORY_CODE = "inventory:%s";
    /**
     * 商品缓存
     */
    String COMMODITY_CODE="commodity:%s";
    /**
     * 用户缓存
     */
    String USER_CODE="user:%s";
    /**
     * 客户订单排行缓存
     */
    String ORDER_CUSTOMER_RANK_CODE="order:customer:rank:%s";
    /**
     * 商家订单排行缓存
     */
    String ORDER_SELLER_RANK_CODE="order:seller:rank:%s";


    /************ hash结构里的key start ************/
    /**
     * 可售库存
     */
    String INVENTORY_FIELD_SELLABLE_QUANTITY = "sellableQuantity";
    /**
     * 预占库存
     */
    String INVENTORY_FIELD_WITHHOLDING_QUANTITY = "withholdingQuantity";
    /**
     * 占用库存
     */
    String INVENTORY_FIELD_OCCUPIED_QUANTITY = "occupiedQuantity";
    /************ hash结构里的key end ************/

}
