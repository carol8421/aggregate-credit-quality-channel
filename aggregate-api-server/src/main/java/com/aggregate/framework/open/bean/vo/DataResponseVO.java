package com.aggregate.framework.open.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponseVO implements Serializable {

    private static final long serialVersionUID = 8170829250230985443L;
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

    /**
     * 评分结果(0-15)
     */
    private String score;

    /**
     * data
     */
    private String data;
}
