package com.xbcode.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

//=== 服务降级 ================

    /**
     * 正常访问，肯定ok方法
     *
     * @param id
     * @return
     */
    public String paymentInfo_ok(Integer id) {
        return "线程池：" + Thread.currentThread().getName() +
                "paymentInfo_ok,id：" + id + "\t" + "o(n_n)o哈哈~";
    }

    /**
     * 故意耗费时间，模拟复杂场景
     * 制造两个异常： 1  int age = 10/0; 计算异常
     * 2  我们能接受3秒钟，它运行5秒钟，超时异常。
     * 如果当前服务不可用了，做服务降级，兜底的方案都是paymentInfo_TimeOutHandler
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        //int timeNumber = 5;
        //int age = 10/0;
        //暂停时间
        try {
            //TimeUnit.SECONDS.sleep(timeNumber);  //秒
            TimeUnit.MILLISECONDS.sleep(3000);  //MILLISECONDS毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() +
                ",id：" + id + "\t" + "o(n_n)o哈哈~" +
                "耗时(秒)：" /*+ timeNumber + "秒"*/;
    }

    /**
     * 兜底方法
     *
     * @param id
     * @return
     */
    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() +
                " 8001系统繁忙或运行报错，请稍后再试！,id：" + id + "\t" + "/(ㄒoㄒ)/~~";
    }


//=== 服务熔断 ==============


    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少后跳闸
    })

    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " + id;
    }
}
