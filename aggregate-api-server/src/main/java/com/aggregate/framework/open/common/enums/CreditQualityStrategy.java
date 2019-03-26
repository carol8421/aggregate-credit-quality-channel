/*
package com.aggregate.framework.open.common.enums;

import com.aggregate.framework.gzt.interfaces.GuoZhenChannel;
import com.aggregate.framework.open.interfaces.CreditQualityAdapter;

public enum CreditQualityStrategy {


    GUO_ZHEN(0,new GuoZhenChannel());

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
*/
