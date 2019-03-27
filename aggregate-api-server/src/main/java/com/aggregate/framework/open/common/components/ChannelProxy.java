package com.aggregate.framework.open.common.components;

import com.aggregate.framework.exception.BusinessException;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.entity.mongo.CreditQualityMongo;
import com.aggregate.framework.open.exception.ExceptionChannelCode;
import com.aggregate.framework.open.mapper.mongodb.CreditQualityMongoDao;
import com.aggregate.framework.open.utils.ObjectUtils;
import com.aggregate.framework.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ChannelProxy  implements MethodInterceptor {

    public Object getInstance(Object obj){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Autowired
    private RedisHandler redisHandler;

    @Autowired
    private CreditQualityMongoDao creditQualityMongoDao;


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 是否扣款
        Boolean isCallThird = Boolean.FALSE;
        CreditQualityDto dto =  (CreditQualityDto)objects[0];
        // 检查通道
        Object obj = before(dto);
        if(Objects.isNull(obj)){
            obj = methodProxy.invokeSuper(o,objects);
            isCallThird = Boolean.TRUE;
        }
        // 进行扣款
        if(isCallThird){
            after(dto);
        }
        return obj;
    }

    private Object before(CreditQualityDto dto){
        log.info("[ChannelProxy].[before] before method:{}");
        Boolean openFlag = this.channelIsOpen(dto);
        if(!openFlag){
            throw new BusinessException(ExceptionChannelCode.CHANNEL_NOT_OPEN);
        }
        Boolean reqFlag = this.checkCacheData(dto);
        if(!reqFlag){
            return null;
        }
        return null;
    }

    private void after(CreditQualityDto dto){
        log.info("[ChannelProxy].[after] after method:{}",dto.toString());
        //1.确认调用后扣款
        String[] channelName = this.getChannelName(dto.getServerName());
        Object object = redisHandler.getHashKey(dto.getClientId(),channelName[0]);
        JSONObject jsonObject = JSON.parseObject(object.toString());
        Map<String, Object> maps = JSONObject.toJavaObject(jsonObject, Map.class);
        // 调用次数 +1
        redisHandler.increment(channelName[1],Long.parseLong( maps.get(channelName[1]).toString())+1);
        // 扣款
        Double account = Double.valueOf( maps.get("account_balance").toString());
        redisHandler.increment("account_balance",account - 0.1);
        // mondo 入库
        /*CreditQualityMongo creditQualityMongo = CreditQualityMongo.builder()
                .clientId(dto.getClientId())
                .dateTime(new Date())
                .serverName(dto.getServerName())
                .Data(DateUtil.getNow(DateUtil.longFormat))
                .build();
        creditQualityMongoDao.save(creditQualityMongo);*/
    }

    /**
     * 判断是否开启通道
     * @param dto
     * @return
     */
    private Boolean channelIsOpen(CreditQualityDto dto){
        String[] channelName = this.getChannelName(dto.getServerName());
        String cName = channelName[0];
        String methodName = channelName[1];
        // 获取指定key 下的hash key
        Object object = redisHandler.getHashKey(dto.getClientId(),cName);
        JSONObject jsonObject = JSON.parseObject(object.toString());
        Map<String, Object> maps = JSONObject.toJavaObject(jsonObject, Map.class);
        //检查用户是否开通通道接口
        if(maps.containsKey(methodName)){
            // 检查用户金额是否满足本次操作
            BigDecimal balance = BigDecimal.valueOf(Double.valueOf(maps.get("account_balance").toString()));
            if(BigDecimal.ZERO.compareTo(balance) == 0){
                throw new BusinessException(ExceptionChannelCode.BALANCE_ENOUGH);
            }
            return Boolean.TRUE;
        }else{
            throw new BusinessException(ExceptionChannelCode.CHANNEL_NOT_OPEN);
        }
    }

    /**
     * cache数据检查
     * @return
     */
    public Boolean checkCacheData(CreditQualityDto dto){
        List<CreditQualityMongo> list = null;
        try {
            list = creditQualityMongoDao.findByClientIdAndServerNameOrderByDateTimeDesc(dto.getClientId(),dto.getServerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(list)){
            CreditQualityMongo creditQualityMongo = list.get(0);
            Date dataTime = creditQualityMongo.getDateTime();
            Long day = DateUtil.getDiffDays(new Date(),dataTime);
            if(day > 2){
                return Boolean.FALSE;
            }else{
                return Boolean.TRUE;
            }
        }else{
            return  Boolean.FALSE;
        }
    }

    /**
     *  获取通道名称和服务名称
     * @param str
     * @return
     */
    public String[] getChannelName(String str){
        String[] channelName = null;
        if(!StringUtils.isEmpty(str)){
            if(str.indexOf(".") != -1){
                channelName = str.split(Pattern.quote("."));
            }
        }
        return channelName;
    }



}
