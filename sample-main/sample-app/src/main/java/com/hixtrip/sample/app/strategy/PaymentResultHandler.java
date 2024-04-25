package com.hixtrip.sample.app.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 * 支付回调处理类
 * @date 20240417
 * @author cpp
 */
public interface PaymentResultHandler {

    /**
     * 要执行的方法
     * @return
     */
    Integer getStatus();

    /**
     * 要执行的方法
     * @param commandPay
     * @return
     */
    String execute(CommandPay commandPay);

}
