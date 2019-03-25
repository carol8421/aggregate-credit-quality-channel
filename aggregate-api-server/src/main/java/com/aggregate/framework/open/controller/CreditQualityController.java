package com.aggregate.framework.open.controller;

import com.aggregate.framework.entity.ResponseResult;
import com.aggregate.framework.gzt.bean.vo.UpstreamVO;
import com.aggregate.framework.gzt.entity.Upstream;
import com.aggregate.framework.gzt.service.UpstreamService;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.dto.RequestDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
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

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/open/api")
@Slf4j
public class CreditQualityController extends BaseController{

    @Autowired
    CreditQualityService creditQualityService;

    @Autowired
    UpstreamService upstreamService;

    @PostMapping(value =  "/personCreditQuality")
    @HystrixCommand(
            fallbackMethod = "personCreditQualityFallback",
            groupKey = "hystrixAnnotationGroup",
            commandKey = "hystrixAnnotationSyncKey",
            threadPoolProperties={
                    @HystrixProperty(name = "coreSize", value = "50")
            })
    public ResponseResult personCreditQuality(
            final HttpServletRequest request,
            @ApiParam(required = true, name = "requestDto", value = "查询请求dto")
            @RequestBody @Validated final RequestDto requestDto
    ) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                CreditQualityDto creditQualityDto = convert2CreditQualityDto(request,requestDto);
                DataResponseVO dataResponseVO = creditQualityService.queryCreditQuality(creditQualityDto);
                criteria.addSingleResult(dataResponseVO);
            }
        }.sendRequest(requestDto);

    }

    public ResponseResult personCreditQualityFallback(final HttpServletRequest request,@RequestBody @Validated final RequestDto requestDto){
        log.debug("降级策略");
        return ResponseResult.fail(500,"bad request");
    }
}