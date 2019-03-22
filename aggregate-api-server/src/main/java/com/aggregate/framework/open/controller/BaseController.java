package com.aggregate.framework.open.controller;

import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.dto.RequestDto;
import com.aggregate.framework.open.common.components.RedisHandler;
import com.aggregate.framework.open.common.components.SpringApplicationContext;
import com.aggregate.framework.open.common.constants.ClientConstant;
import com.aggregate.framework.open.common.constants.SecurityConstant;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class BaseController {

    protected CreditQualityDto convert2CreditQualityDto(HttpServletRequest request ,RequestDto requestDto){
        String clientId = request.getHeader(SecurityConstant.REQUEST_SECURITY_CLIENT_ID);

        String publicKey = this.loadPublicKey(clientId);

        return this.decodeSignAndDate(publicKey);
    }

    private String loadPublicKey(String clientId){

        String publicKey= StringUtils.EMPTY;
        RedisHandler redisHandler = SpringApplicationContext.getBean(RedisHandler.class);
        if(Objects.nonNull(redisHandler.getHashKey(clientId, ClientConstant.PUBLIC_KEY))){
            //TODO 从数据库获得PUBLIC_KEY 并保存到 redis hash中

        }
        return publicKey;
    }

    private CreditQualityDto decodeSignAndDate(String publicKey ){
        CreditQualityDto creditQualityDto = new CreditQualityDto();

        //TODO sign 验证，data解密

        //TODO jsonString 转 CreditQualityDto

        return creditQualityDto;
    }
}
