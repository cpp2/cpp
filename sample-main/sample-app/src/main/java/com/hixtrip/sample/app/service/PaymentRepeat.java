package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.strategy.PaymentResultHandler;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.enums.PayResultEnum;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PaymentRepeat implements PaymentResultHandler {

    @Autowired
    private PayDomainService payDomainService;

    @Override
    public Integer getStatus() {
        return PayResultEnum.PAYMENT_REPEAT.getCode();
    }

    @Override
    public String execute(CommandPay commandPay) {
        // 记录支付记录
        payDomainService.payRecord(commandPay);
        return "重复支付";
    }

}
