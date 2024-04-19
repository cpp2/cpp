package com.hixtrip.sample.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hixtrip.sample.infra.db.dataobject.OrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * mapper示例
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {

    /**
     * 更新支付成功的情况
     * @param orderNo
     * @param orderStatus
     * @param payStatus
     */
    void updatePaymentSuccess(@Param(value = "orderNo") String orderNo,
                              @Param(value = "orderStatus") Integer orderStatus,
                              @Param(value = "payStatus") Integer payStatus);

    /**
     * 更新支付失败的情况
     * @param orderNo
     * @param payStatus
     */
    void updatePaymentFailed(@Param(value = "orderNo") String orderNo, @Param(value = "payStatus") Integer payStatus);

}
