package com.hixtrip.sample.domain.pay.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付回调策略工厂
 * @date 20240417
 * @author cpp
 */
public class PaymentResultFactory {

    private static Map<Integer, PaymentResultHandler> map = new ConcurrentHashMap<>();

    public static PaymentResultHandler getByStatus(Integer status) {
        return map.get(status);
    }

    public static void register(Integer status, PaymentResultHandler paymentCallbackStrategy) {
        map.put(status, paymentCallbackStrategy);
    }

}
