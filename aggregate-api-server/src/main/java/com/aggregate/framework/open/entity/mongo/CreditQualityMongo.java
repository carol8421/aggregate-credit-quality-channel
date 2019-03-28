package com.aggregate.framework.open.entity.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "credit_quality")
@Builder
public class CreditQualityMongo {

    @Id
    private String id;

    /**
     * 客户端id
     */
    @Field("client_id")
    private String clientId;

    /**
     * 查询数据
     */
    @Field("quality_data")
    private String qualityData;

    /**
     * 服务名称
     */
    @Field("server_name")
    private String serverName;

    /**
     * 失效时间
     */
    @Field("date_time")
    private Date dateTime;


}
