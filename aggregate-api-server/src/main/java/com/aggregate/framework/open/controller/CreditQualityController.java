package com.aggregate.framework.open.controller;

import com.aggregate.framework.entity.ResponseResult;
import com.aggregate.framework.open.dto.RequestDto;
import com.aggregate.framework.web.common.WebResCallback;
import com.aggregate.framework.web.common.WebResCriteria;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/open/api")
@Slf4j
public class CreditQualityController {

    @PostMapping(value =  "/personCreditQuality")
    @HystrixCommand(
            fallbackMethod = "personCreditQualityFallback",
            groupKey = "hystrixAnnotationGroup",
            commandKey = "hystrixAnnotationSyncKey",
            threadPoolProperties={
                    @HystrixProperty(name = "coreSize", value = "50")
            }
           /* ,commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),//并发策略选择，信号量
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "50"),//信号量储备数
            @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "10")//信号量降级最大并发数
    }*/)
    public ResponseResult personCreditQuality(
            @ApiParam(required = true, name = "requestDto", value = "查询请求dto")
            @RequestBody @Validated final RequestDto requestDto
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(null);
            }
        }.sendRequest(requestDto);

    }

    public ResponseResult personCreditQualityFallback(@RequestBody @Validated final RequestDto requestDto){
        log.debug("降级策略");
        return ResponseResult.fail(500,"bad request");
    }


    @GetMapping(value = "/test")
    @HystrixCommand(
            fallbackMethod = "testFallback",
            groupKey = "hystrixAnnotationGroup",
            commandKey = "hystrixAnnotationSyncKey",
            threadPoolProperties={
                    @HystrixProperty(name = "coreSize", value = "500")
            }/*
             ,commandProperties = {
             @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),

             @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
             @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
     }*/)
    public ResponseResult test() {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(null);
            }
        }.sendRequest();

    }

    public ResponseResult testFallback(){
        log.debug("降级策略");
        return ResponseResult.fail(500,"bad request");
    }
}