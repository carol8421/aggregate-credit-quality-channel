package com.aggregate.framework.open.mapper.mongodb;

import com.aggregate.framework.open.entity.mongo.CreditQualityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditQualityMongoDao extends MongoRepository<CreditQualityMongo, String> {


    /**
     * 通过client Id he serverName 查询数据
     * @param clientId 客服端id
     * @param serverName 服务名称
     * @return
     */
    List<CreditQualityMongo> findByClientIdAndServerNameOrderByDateTimeDesc(String clientId,String serverName);


    @Query(value = "{'client_id': {'$eq':?0 },'server_name':{'$eq' : '?1'}}")
    List<CreditQualityMongo> findByClientIdAndServerNames(String clientId,String serverName);
}
