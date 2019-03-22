package com.aggregate.framework.open.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditQualityDto extends BaseDto{

    /**
     * 第三方查询流水号
     */
    private String outerId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String identityId;
}
