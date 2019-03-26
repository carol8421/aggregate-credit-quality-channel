package com.aggregate.framework.open.service;

import com.aggregate.framework.gzt.adapter.GuoZhenAdapter;
import com.aggregate.framework.open.adapter.CreditQualityAdapter;
import com.aggregate.framework.open.annotations.CreditQuality;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
import com.aggregate.framework.open.common.enums.CreditQualityStrategy;
import org.springframework.stereotype.Service;

@Service
public class CreditQualityService{

    @CreditQuality
    public DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto){
        return processLogin(creditQualityDto);
    }

    private DataResponseVO processLogin(CreditQualityDto creditQualityDto){
        try{
            CreditQualityAdapter adapter =  CreditQualityStrategy.getAdapter(creditQualityDto.getChannelNumber());

            if(adapter.support(adapter)){

                String responseData = adapter.queryCreditQuality(creditQualityDto);
                return adapter.loadResponseDate(responseData,creditQualityDto);

/*                DataResponseVO dataResponseVO = DataResponseVO.builder()
                        .identityId(creditQualityDto.getIdentityId())
                        .name(creditQualityDto.getName())
                        .outerId(creditQualityDto.getOuterId())
                        .score("0")
                        .build();


                return dataResponseVO;*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
