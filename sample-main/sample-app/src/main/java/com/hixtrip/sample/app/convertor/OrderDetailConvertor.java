package com.hixtrip.sample.app.convertor;

import com.hixtrip.sample.client.order.dto.CommandOderDetailDTO;
import com.hixtrip.sample.domain.order.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * DTO对像 -> 领域对象转换器
 * 转换器
 */
@Mapper
public interface OrderDetailConvertor {

    OrderDetailConvertor INSTANCE = Mappers.getMapper(OrderDetailConvertor.class);

    OrderDetail dtoToDomain(CommandOderDetailDTO commandOderDetailDTO);

}
