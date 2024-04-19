package com.hixtrip.sample.domain.pay;

import com.hixtrip.sample.domain.pay.enums.PayResultEnum;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.pay.strategy.PaymentResultFactory;
import com.hixtrip.sample.domain.pay.strategy.PaymentResultHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PaymentRepeat implements PaymentResultHandler, InitializingBean {

    @Autowired
    private PayDomainService payDomainService;

    @Override
    public void afterPropertiesSet() throws Exception {
        PaymentResultFactory.register(PayResultEnum.PAYMENT_REPEAT.getCode(), this);
    }

    @Override
    public String execute(CommandPay commandPay) {
        // 记录支付记录
        payDomainService.payRecord(commandPay);
        return "重复支付";
    }

}
