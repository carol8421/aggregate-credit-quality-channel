package com.aggregate.framework.open.controller;

import com.aggregate.framework.open.common.components.RedisHandler;
import com.aggregate.framework.open.entity.mongo.CreditQualityMongo;
import com.aggregate.framework.open.mapper.mongodb.CreditQualityMongoDao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    RedisHandler redisHandler;

    @Autowired
    CreditQualityMongoDao creditQualityMongoDao;

    @GetMapping("add")
    public void add (){
        Map<String,Object> map = new HashMap<>();
        map.put("queryCredit",1);
        map.put("selectCredit",1);
        map.put("account_balance",100);

        JSONObject jsonObject = (JSONObject) JSON.toJSON(map);
        jsonObject.toString();
        System.out.println(jsonObject.toString());
          redisHandler.put("up_0001","guozhen",jsonObject.toString());
    }

    @GetMapping("md")
    public void md (){
        CreditQualityMongo creditQualityMongo = CreditQualityMongo.builder()
                .clientId("up_0001")
                .dateTime(new Date())
                .serverName("guozhen.queryCredit")
                .build();
        creditQualityMongoDao.save(creditQualityMongo);
    }


    @GetMapping("se")
    public Object se (){
        List<CreditQualityMongo> list = creditQualityMongoDao.findByClientIdAndServerNames("up_0001","guozhen.queryCredit");
        return  list;

    }



}
