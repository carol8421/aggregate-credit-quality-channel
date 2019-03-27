package com.aggregate.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestClientDto implements Serializable {

    /**
     * 服务名称
     */
    private String serverName;


    /**
     * 参数数据data
     */
    private Map<String,String> dataMap;

    /**
     * 第三方查询流水号
     */
    private String outerId;
}
