package com.aggregate.framework.open.controller;

import com.aggregate.framework.exception.BusinessException;
import com.aggregate.framework.exception.ExceptionCode;
import com.aggregate.framework.gzt.bean.vo.UpstreamVO;
import com.aggregate.framework.open.service.UpstreamService;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.dto.RequestDto;
import com.aggregate.framework.open.common.components.RedisHandler;
import com.aggregate.framework.open.common.components.SpringApplicationContext;
import com.aggregate.framework.open.common.constants.ClientConstant;
import com.aggregate.framework.open.common.constants.SecurityConstant;
import com.aggregate.framework.utils.JsonUtil;
import com.aggregate.framework.utils.codec.Base64Utils;
import com.aggregate.framework.utils.codec.RSAUtils;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {

    protected CreditQualityDto convert2CreditQualityDto(HttpServletRequest request ,RequestDto requestDto){
        String clientId = request.getHeader(SecurityConstant.REQUEST_SECURITY_CLIENT_ID);

        String publicKey = this.loadPublicKey(clientId);
        CreditQualityDto creditQualityDto = this.decodeSignAndDate(publicKey,requestDto);
        creditQualityDto.setClientId(clientId);
        return creditQualityDto;
    }

    private String loadPublicKey(String clientId){

        String publicKey;
        RedisHandler redisHandler = SpringApplicationContext.getBean(RedisHandler.class);
        if(!redisHandler.hashKey(clientId, ClientConstant.PUBLIC_KEY)){
            UpstreamService upstreamService = SpringApplicationContext.getBean(UpstreamService.class);

            UpstreamVO upstreamVO = upstreamService.selectByClientId(clientId);
            Map<String, String> map =Maps.newHashMap();
            map.put(ClientConstant.PUBLIC_KEY,upstreamVO.getPublicKey());
            map.put(ClientConstant.CLIENT_ID,upstreamVO.getClientId());
            map.put(ClientConstant.CLIENT_SECRET,upstreamVO.getClientSecret());

            redisHandler.putAll(clientId,map);

            publicKey = upstreamVO.getPublicKey();
        }else{
            publicKey = redisHandler.getHashKey(clientId, ClientConstant.PUBLIC_KEY).toString();
        }
        return publicKey;
    }

    private CreditQualityDto decodeSignAndDate(String publicKey ,RequestDto requestDto) throws BusinessException {
        try {
            //sign 验证
            String sign = requestDto.getSign();
            String encodedStr = requestDto.getData();

            byte[] decodeByte = Base64Utils.decode(encodedStr);
            boolean status = RSAUtils.verify(decodeByte, publicKey, sign);
            if(!status){
                throw new BusinessException(ExceptionCode.SECURITY_SIGN);
            }

            byte[] decodedData = RSAUtils.decryptByPublicKey(decodeByte, publicKey);
            String decodedDataStr = new String(decodedData);

            //jsonString 转 CreditQualityDto
            CreditQualityDto creditQualityDto = JsonUtil.parseObject(decodedDataStr,CreditQualityDto.class);
            return creditQualityDto;
        }catch (Exception e){
            throw new BusinessException(ExceptionCode.SECURITY_FAIL);
        }
    }
}
