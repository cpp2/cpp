package com.hixtrip.sample.infra.db.convertor;

import com.hixtrip.sample.domain.order.model.OrderDetail;
import com.hixtrip.sample.infra.db.dataobject.OrderDetailPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * DO对像 -> 领域对象转换器
 */
@Mapper
public interface OrderDetailDOConvertor {
    OrderDetailDOConvertor INSTANCE = Mappers.getMapper(OrderDetailDOConvertor.class);

    OrderDetailPO doToPO(OrderDetail order);

    OrderDetail poToDO(OrderDetailPO order);

}
