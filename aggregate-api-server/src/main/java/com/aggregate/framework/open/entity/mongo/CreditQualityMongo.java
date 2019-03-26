package com.aggregate.framework.open.entity.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "credit_quality")
@Builder
public class CreditQualityMongo {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("identity_id")
    private String identityId;

    @Field("quality_data")
    private String Data;

    @Field("strategy")
    private Long strategy;
}
