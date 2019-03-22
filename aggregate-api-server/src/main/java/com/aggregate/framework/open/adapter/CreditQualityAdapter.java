package com.aggregate.framework.open.adapter;

import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;

public interface CreditQualityAdapter {

    Boolean support(Object adapter);

    DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto);
}
