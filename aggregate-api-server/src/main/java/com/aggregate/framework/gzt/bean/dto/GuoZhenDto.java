package com.aggregate.framework.gzt.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuoZhenDto<T> implements Serializable {

    /**
     * 第三方查询流水号
     */
    private String outerId;

    /**
     * 用户上游clientId
     */
    private String clientId;

    /**
     * 服务名称
     */
    private String serverName;


    /**
     * 参数数据data
     */
    private T t;

}
