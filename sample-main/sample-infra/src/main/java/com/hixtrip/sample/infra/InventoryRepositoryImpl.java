package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.infra.db.constants.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public Integer getInventory(String skuId) {
        Integer quantity = null;
        String cacheKey = String.format(RedisKey.INVENTORY_CODE, skuId);
        // 从缓存中获取商品库存
        Map<Object, Object> resultMap = redisTemplate.opsForHash().entries(cacheKey);
        if (resultMap.size() > 0) {
            quantity = (Integer) resultMap.get(RedisKey.INVENTORY_FIELD_SELLABLE_QUANTITY);
            return quantity;
        }
        // 缓存获取不到，查询数据库后再放入缓存
        quantity = 10;
        redisTemplate.opsForHash().put(cacheKey, RedisKey.INVENTORY_FIELD_SELLABLE_QUANTITY, quantity);
        redisTemplate.opsForHash().put(cacheKey, RedisKey.INVENTORY_FIELD_WITHHOLDING_QUANTITY, 2);
        redisTemplate.opsForHash().put(cacheKey, RedisKey.INVENTORY_FIELD_OCCUPIED_QUANTITY, 20);
        return quantity;
    }

    @Override
    public Boolean changeInventory(String skuId, Integer sellableQuantity, Integer withholdingQuantity, Integer occupiedQuantity) {
        String cacheKey = String.format(RedisKey.INVENTORY_CODE, skuId);
        // 脚本里的KEYS参数
        List<String> keys = new ArrayList<>();
        keys.add(cacheKey);
        keys.add(RedisKey.INVENTORY_FIELD_SELLABLE_QUANTITY);
        keys.add(RedisKey.INVENTORY_FIELD_WITHHOLDING_QUANTITY);
        keys.add(RedisKey.INVENTORY_FIELD_OCCUPIED_QUANTITY);
        // 增/减库存的lua脚本
        String decreaseStockLua = this.decreaseStockLua();
        RedisScript script = new DefaultRedisScript<>(decreaseStockLua, Long.class);
        Long result = (Long)redisTemplate.execute(script, keys, sellableQuantity, withholdingQuantity, occupiedQuantity);
        // -3未放入缓存，-2库存不足，-1不限库存，>=0库存充足（未放入缓存的情况查库再放入缓存再扣库存，这边先不考虑）
        return result != -2;
    }

    /**
     * 增/减库存Lua脚本
     * 返回：-3未放入缓存，-2库存不足，-1不限库存，>=0库存充足
     * @return
     */
    private String decreaseStockLua() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('HEXISTS', KEYS[1], KEYS[2]) == 1) then");
        sb.append("    local sellableQuantity = tonumber(redis.call('HGET', KEYS[1], KEYS[2]));");
        sb.append("    local withholdingQuantity = tonumber(redis.call('HGET', KEYS[1], KEYS[3]));");
        sb.append("    local occupiedQuantity = tonumber(redis.call('HGET', KEYS[1], KEYS[4]));");
//         待变动的库存数量
        sb.append("    local changeSellableQuantity = tonumber(ARGV[1]);");
//         待变动的预占库存数量
        sb.append("    local changeWithholdingQuantity = tonumber(ARGV[2]);");
//         待变动的占用库存数量
        sb.append("    local changeOccupiedQuantity = tonumber(ARGV[3]);");

        sb.append("    if (sellableQuantity == -1) then");
        sb.append("        return -1;");
        sb.append("    end;");

//         待变动的库存数量，>0为要减的库存数，要判断库存是否充足，<0为要增加的库存数量，不用判断库存是否充足
        sb.append("    if (changeSellableQuantity <= 0 or sellableQuantity >= changeSellableQuantity) then");
        sb.append("        redis.call('HINCRBY', KEYS[1], KEYS[3], changeWithholdingQuantity);");
        sb.append("        redis.call('HINCRBY', KEYS[1], KEYS[4], changeOccupiedQuantity);");
        sb.append("        if (changeSellableQuantity == 0) then return 0 else");
        sb.append("        return redis.call('HINCRBY', KEYS[1], KEYS[2], -changeSellableQuantity)");
        sb.append("        end;");
        sb.append("    else");
        sb.append("        return -2");
        sb.append("    end;");

        sb.append("  else");
        sb.append("    return -3;");
        sb.append("  end;");
        return sb.toString();

    }

}
