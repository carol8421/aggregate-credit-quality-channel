package com.aggregate.framework.open.common.enums;

import com.aggregate.framework.gzt.adapter.GuoZhenAdapter;
import com.aggregate.framework.open.adapter.CreditQualityAdapter;

public enum CreditQualityStrategy {


    GUO_ZHEN(0,new GuoZhenAdapter());

    private long strategyNumber;
    private CreditQualityAdapter creditQualityAdapter;

    CreditQualityStrategy(long number,CreditQualityAdapter creditQualityAdapter) {
        this.strategyNumber = strategyNumber;
        this.creditQualityAdapter = creditQualityAdapter;
    }

    public static CreditQualityAdapter getAdapter(long number) {
        for (CreditQualityStrategy strategy :CreditQualityStrategy.values()) {
            if(number == strategy.strategyNumber){
                return strategy.creditQualityAdapter;
            }
        }
        return GUO_ZHEN.creditQualityAdapter;
    }
}
