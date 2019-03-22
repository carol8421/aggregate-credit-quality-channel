package com.aggregate.framework.open.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto extends BaseDto{

    @ApiModelProperty(value = "签名")
    @NotBlank(message = "签名不能为空")
    private String sign;

    @ApiModelProperty(value = "查询数据")
    @NotBlank(message = "查询数据不能为空")
    private String data;
}
