package com.hixtrip.sample.domain.inventory.repository;

/**
 * 库存
 * @date 20240417
 * @author cpp
 */
public interface InventoryRepository {

    /**
     * 获取sku当前库存
     * @param skuId
     * @return
     */
    Integer getInventory(String skuId);

    /**
     * 修改库存
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    Boolean changeInventory(String skuId, Integer sellableQuantity, Integer withholdingQuantity, Integer occupiedQuantity);

}
