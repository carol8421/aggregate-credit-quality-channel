package com.aggregate.framework.open.controller;

import com.aggregate.framework.entity.ResponseResult;
import com.aggregate.framework.open.dto.RequestDto;
import com.aggregate.framework.web.common.WebResCallback;
import com.aggregate.framework.web.common.WebResCriteria;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/open/api/")
public class CreditQualityController {


    @ApiOperation(value = "查询个人征信信息", httpMethod = "POST", notes = "查询个人征信信息")
    @PostMapping(value = "/personCreditQuality")
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

}