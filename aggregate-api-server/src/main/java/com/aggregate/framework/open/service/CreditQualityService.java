package com.aggregate.framework.open.service;

import com.aggregate.framework.gzt.adapter.GuoZhenAdapter;
import com.aggregate.framework.open.adapter.CreditQualityAdapter;
import com.aggregate.framework.open.annotations.CreditQuality;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
import org.springframework.stereotype.Service;

@Service
public class CreditQualityService{

    @CreditQuality
    public DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto){
        return processLogin(creditQualityDto, GuoZhenAdapter .class);
    }

    private DataResponseVO processLogin(CreditQualityDto creditQualityDto,Class<? extends CreditQualityAdapter> clazz){
        try{
            CreditQualityAdapter adapter = clazz.newInstance();

            if(adapter.support(adapter)){
                return adapter.queryCreditQuality(creditQualityDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
