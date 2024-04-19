package com.hixtrip.sample.infra.db.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "order_info", autoResultMap = true)
@SuperBuilder(toBuilder = true)
public class OrderPO {

    /**
     * 订单号
     */
    @TableId
    private String id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 购买人
     */
    private String userId;
    /**
     * 卖家id
     */
    private String sellerId;
    /**
     * 订单状态（0-待支付，1-待发货，2-已发货，3-已完成，4-已关闭）
     */
    private Integer orderStatus;
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;
    /**
     * 运费
     */
    private BigDecimal shippingFee;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 支付方式（1-支付宝，2-微信）
     */
    private Integer payType;
    /**
     * 支付状态（0-未支付，1-支付成功，2-支付失败）
     */
    private Integer payStatus;
    /**
     * 下单时间
     */
    private LocalDateTime orderTime;
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private Long delFlag;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
