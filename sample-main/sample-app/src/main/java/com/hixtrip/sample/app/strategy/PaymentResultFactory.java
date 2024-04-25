package com.hixtrip.sample.app.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付回调策略工厂
 * @date 20240417
 * @author cpp
 */
@Component
public class PaymentResultFactory implements ApplicationContextAware {

    private static Map<Integer, PaymentResultHandler> map = new ConcurrentHashMap<>();

    public static PaymentResultHandler getByStatus(Integer status) {
        return map.get(status);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PaymentResultHandler> payCallbackTypeHandlerMap = applicationContext.getBeansOfType(PaymentResultHandler.class);
        payCallbackTypeHandlerMap.values().forEach(payCallbackTypeHandler -> {
            map.put(payCallbackTypeHandler.getStatus(), payCallbackTypeHandler);
        });
    }
}
