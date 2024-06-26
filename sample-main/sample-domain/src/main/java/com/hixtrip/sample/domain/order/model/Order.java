package com.hixtrip.sample.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 订单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class Order {

    /**
     * 订单号
     */
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
    private Long delFlag;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 订单详情
     */
    List<OrderDetail> orderDetailList;

    /**
     * 订单总价=订单详情每个商品的价格之和
     */
    public BigDecimal calcTotalAmount(){
        return orderDetailList.stream().map(OrderDetail::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 支付金额=订单总额+运费-优惠金额
     * @return
     */
    public BigDecimal calcPaymentAmount(){
        BigDecimal amount = calcTotalAmount();
        BigDecimal payAmount = amount;
        if (Objects.nonNull(this.discountAmount)) {
            payAmount = amount.add(shippingFee);
        }
        if(Objects.nonNull(this.discountAmount)){
            payAmount = payAmount.subtract(discountAmount);
        }
        return payAmount;
    }

    /**
     * 生成订单编号
     * @return
     */
    public String generateOrderNo() {
        // 按规则生成订单编号（这边先写死），后面几位取用户id后几位
        if (userId == null) {
            throw new RuntimeException("用户登录已过期，请重新登录");
        }
        String lastUserId = userId.substring(userId.length() - 6);
        return "1111111111111" + lastUserId;
    }

}
