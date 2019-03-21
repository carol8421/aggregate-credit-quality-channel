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


    AtomicInteger  atomicInteger1 = new AtomicInteger();
    AtomicInteger  atomicInteger2 = new AtomicInteger();

    @ApiOperation(value = "查询个人征信信息", httpMethod = "POST", notes = "查询个人征信信息")
    @PostMapping(value = "/personCreditQuality")
    @HystrixCommand(
            fallbackMethod = "personCreditQualityFallback",
            threadPoolProperties = {  //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20")},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //若干10s一个窗口内失败三次, 则达到触发熔断的最少请求量
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") //断路30s后尝试执行, 默认为5s
            })
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

    @ApiOperation(value = "测试", httpMethod = "GET", notes = "测试")
    @GetMapping(value = "/test")
/*    @HystrixCommand(
            fallbackMethod = "testFallback",
            threadPoolProperties = {  //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                    @HystrixProperty(name = "coreSize", value = "20"),//并发执行的最大线程数，默认10
                    @HystrixProperty(name = "maximumSize", value = "20"),//能正常运行command的最大支付并发数
                    @HystrixProperty(name = "maxQueueSize", value = "-1"),
                    //HystrixProperty(name = "queueSizeRejectionThreshold", value = "70")
                    },
            commandProperties = {
*//*                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"), //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), //若干10s一个窗口内失败三次, 则达到触发熔断的最少请求量
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") //断路30s后尝试执行, 默认为5s*//*
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),//#command的执行的超时时间 默认是1000
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//用来跟踪熔断器的健康性，如果未达标则让request短路 默认true
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000"),//断路1s后尝试执行, 默认为5s
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10")
            })*/
    @HystrixCommand(
            fallbackMethod = "testFallback",
            threadPoolProperties={
                    @HystrixProperty(name = "coreSize", value = "20")
            },commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),//并发策略选择，信号量
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "50")//信号量储备数
    })
    public ResponseResult test(
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {

                atomicInteger2.incrementAndGet();
                log.debug("执行完毕");
                //criteria.addSingleResult(null);
            }
        }.sendRequest();

    }

    public ResponseResult testFallback(){
        atomicInteger1.incrementAndGet();
        log.debug("降级策略");
        return ResponseResult.fail(500,"bad request");
    }

    @ApiOperation(value = "测试", httpMethod = "GET", notes = "测试")
    @GetMapping(value = "/test1")
    public ResponseResult test1(
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(atomicInteger1.get());
            }
        }.sendRequest();

    }

    @ApiOperation(value = "测试", httpMethod = "GET", notes = "测试")
    @GetMapping(value = "/test2")
    public ResponseResult test2(
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(atomicInteger2.get());
            }
        }.sendRequest();

    }

}