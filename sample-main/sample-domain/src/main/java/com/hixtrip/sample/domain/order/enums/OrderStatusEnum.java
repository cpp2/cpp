package com.hixtrip.sample.domain.order.enums;

/**
 * 订单状态枚举
 * @date 20240418
 * @author cpp
 */
public enum OrderStatusEnum {

    UNPAID(1, "待支付"),
    PENDING_SHIPMENT(2, "待发货"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成"),
    CLOSED(5, "已关闭"),
    ;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
