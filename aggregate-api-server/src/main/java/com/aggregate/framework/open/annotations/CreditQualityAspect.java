package com.aggregate.framework.open.annotations;

import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
import com.aggregate.framework.open.common.components.RedisHandler;
import com.aggregate.framework.open.common.enums.CreditQualityStrategy;
import com.aggregate.framework.open.entity.mongo.CreditQualityMongo;
import com.aggregate.framework.open.mapper.mongodb.CreditQualityMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;


@Aspect
@Component
@Slf4j
public class CreditQualityAspect {


    @Autowired
    RedisHandler redisHandler;

    private final static String JEDIS_STR = "jedis";

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private CreditQualityMongoDao creditQualityMongoDao;

    @Pointcut("@annotation(com.aggregate.framework.open.annotations.CreditQuality)")
    private void creditQualityAspect() {
    }

    /**
     * 1.完成redis bitmap中接口访问频次数据录入
     * 2.日志记录访问时间，查询内容
     */
    @Before("creditQualityAspect()")
    public void before(JoinPoint point) {
        //Jedis jedis = getJedis();
        //添加到bitmap 中
        //jedis.setbit("test","up_001".hashCode(),true);
        //TODO  调用次数统计

        System.out.println("@Before：模拟权限检查...");
        System.out.println("@Before：目标方法为：" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        System.out.println("@Before：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@Before：被织入的目标对象为：" + point.getTarget());


    }

    /**
     * @param joinPoint
     * @throws Throwable
     *  在mongoDB缓存中查询是否存在对应数据，如果有直接返回
     */
    @Around("creditQualityAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        //检查 mongoDB中是否已经存在
        //todo 时间检索
        CreditQualityDto dto  = (CreditQualityDto)joinPoint.getArgs()[0];
        CreditQualityMongo creditQualityMongo = creditQualityMongoDao.findOne(dto.getClientId());
        if(Objects.nonNull(creditQualityMongo)){
            String strDate = creditQualityMongo.getData();
            //根据策略转换成DataResponseVO
            DataResponseVO dataResponseVO = CreditQualityStrategy.getAdapter(creditQualityMongo.getStrategy()).loadResponseDate(strDate,dto);
            return dataResponseVO;
        }

        try {
            DataResponseVO dataResponseVO = (DataResponseVO)joinPoint.proceed();
            saveDataIntoMongo(dataResponseVO,dto);

            return dataResponseVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  日志记录访问查询结果
     *  开启异步方法更新缓存数据
     */
    @AfterReturning(pointcut ="creditQualityAspect()",returning="returnValue")
    public void after(JoinPoint point, Object returnValue) {
        System.out.println("@AfterReturning：模拟日志记录功能...");
        System.out.println("@AfterReturning：目标方法为：" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        System.out.println("@AfterReturning：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@AfterReturning：返回值为：" + returnValue);
        System.out.println("@AfterReturning：被织入的目标对象为：" + point.getTarget());
    }

    private Jedis getJedis(){
        Field jdField = ReflectionUtils.findField(JedisConnection.class, JEDIS_STR);
        ReflectionUtils.makeAccessible(jdField);
        System.out.println(connectionFactory.getConnection());
        Jedis jedis = (Jedis) ReflectionUtils.getField(jdField, connectionFactory.getConnection());
        return jedis;
    }

    private void saveDataIntoMongo(DataResponseVO dataResponseVO,CreditQualityDto dto){

        CreditQualityMongo creditQualityMongo = CreditQualityMongo.builder()
                .Data(dataResponseVO.getData())
                .identityId(dataResponseVO.getIdentityId())
                .name(dataResponseVO.getName())
                .strategy(Long.valueOf(dto.getChannelNumber()))
                .id(dto.getClientId() + System.currentTimeMillis())
                .build();

        creditQualityMongoDao.save(creditQualityMongo);
    }
}
