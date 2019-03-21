package com.aggregate.framework.open.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto extends BaseDto {
    private String sign;
    private String data;
}
