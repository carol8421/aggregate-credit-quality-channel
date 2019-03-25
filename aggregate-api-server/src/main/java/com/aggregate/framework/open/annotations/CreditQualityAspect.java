package com.aggregate.framework.open.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class CreditQualityAspect {

    @Pointcut("@annotation(com.aggregate.framework.open.annotations.CreditQuality)")
    private void creditQualityAspect() {
    }

    /**
     * 1.完成redis bitmap中接口访问频次数据录入
     * 2.日志记录访问时间，查询内容
     */
    @Before("creditQualityAspect()")
    public void before() {

    }

    /**
     * @param joinPoint
     * @throws Throwable
     *  在mongoDB缓存中查询是否存在对应数据，如果有直接返回
     */
    @Around("creditQualityAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  日志记录访问查询结果
     *  开启异步方法更新缓存数据
     */
    @After("creditQualityAspect()")
    public void after() {
    }
}
