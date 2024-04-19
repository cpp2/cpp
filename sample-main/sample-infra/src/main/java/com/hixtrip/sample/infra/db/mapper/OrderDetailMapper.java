package com.hixtrip.sample.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hixtrip.sample.infra.db.dataobject.OrderDetailPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单详情信息mapper
 * @date 20240418
 * @author cpp
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetailPO> {

    /**
     * 批量保存订单详情信息
     * @param orderDetailList
     */
    void insertBatchOrderDetail(List<OrderDetailPO> orderDetailList);

    /**
     * 通过订单编号获取订单详情
     * @param orderNo
     * @return
     */
    List<OrderDetailPO> getByOrderNo(String orderNo);

}
