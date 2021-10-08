package com.xbcode.service;

import org.springframework.stereotype.Component;

@Component //必须加 //必须加 //必须加
public class PaymentFallbackService implements PaymentHystrixService{

    @Override
    public String paymentInfo_Ok(Integer id) {
        return "-----PaymentFallbackService fall back-paymentInfoOk ,o(╥﹏╥)o";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "-----PaymentFallbackService fall back-paymentInfoTimeOut ,o(╥﹏╥)o";
    }
}
