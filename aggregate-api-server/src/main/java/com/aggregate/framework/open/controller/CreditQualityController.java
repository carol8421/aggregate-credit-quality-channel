package com.aggregate.framework.open.controller;

import com.aggregate.framework.entity.ResponseResult;
import com.aggregate.framework.open.bean.dto.RequestDto;
import com.aggregate.framework.open.service.CreditQualityService;
import com.aggregate.framework.web.common.WebResCallback;
import com.aggregate.framework.web.common.WebResCriteria;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/api")
@Slf4j
public class CreditQualityController {

    @Autowired
    CreditQualityService creditQualityService;

    @PostMapping(value =  "/personCreditQuality")
    @HystrixCommand(
            fallbackMethod = "personCreditQualityFallback",
            groupKey = "hystrixAnnotationGroup",
            commandKey = "hystrixAnnotationSyncKey",
            threadPoolProperties={
                    @HystrixProperty(name = "coreSize", value = "50")
            })
    public ResponseResult personCreditQuality(
            @ApiParam(required = true, name = "requestDto", value = "查询请求dto")
            @RequestBody @Validated final RequestDto requestDto
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(creditQualityService.queryCreditQuality(null));
            }
        }.sendRequest(requestDto);

    }

    public ResponseResult personCreditQualityFallback(@RequestBody @Validated final RequestDto requestDto){
        log.debug("降级策略");
        return ResponseResult.fail(500,"bad request");
    }
}