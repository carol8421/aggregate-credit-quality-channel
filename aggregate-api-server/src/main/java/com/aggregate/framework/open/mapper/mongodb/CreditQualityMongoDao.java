package com.aggregate.framework.open.mapper.mongodb;

import com.aggregate.framework.open.entity.mongo.CreditQualityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditQualityMongoDao extends MongoRepository<CreditQualityMongo, String> {

}
