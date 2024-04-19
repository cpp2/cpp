package com.hixtrip.sample.domain.order.enums;

/**
 * 支付状态枚举
 * @date 20240418
 * @author cpp
 */
public enum PayStatusEnum {

    UNPAID(1, "未支付"),
    PAYMENT_SUCCESS(2, "支付成功"),
    PAYMENT_FAILED(3, "支付失败"),
    ;

    PayStatusEnum(Integer code, String desc) {
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
