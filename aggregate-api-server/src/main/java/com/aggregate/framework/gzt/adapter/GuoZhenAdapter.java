package com.aggregate.framework.gzt.adapter;

import cn.id5.gboss.GbossClient;
import cn.id5.gboss.GbossConfig;
import cn.id5.gboss.http.HttpResponseData;
import com.aggregate.framework.open.adapter.CreditQualityAdapter;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
import com.aggregate.framework.open.common.components.SpringApplicationContext;
import com.aggregate.framework.open.common.configuration.CreditQualityChannelConfig;
import org.apache.http.HttpStatus;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class GuoZhenAdapter implements CreditQualityAdapter {

    CreditQualityChannelConfig.GuoZhenConfig guoZhenConfig;
    GbossClient client;

    @Override
    public Boolean support(Object adapter) {
        if(adapter instanceof GuoZhenAdapter){
            guoZhenConfig = SpringApplicationContext.getBean(CreditQualityChannelConfig.GuoZhenConfig.class);
            this.convert2GbossClient();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto) {
        StringBuffer sb = new StringBuffer();
        sb.append(creditQualityDto.getOuterId())
                .append(",").append(creditQualityDto.getName())
                .append(",").append(creditQualityDto.getIdentityId());

        try {
            HttpResponseData httpData = client.invokeSingle(guoZhenConfig.getProduct(), sb.toString());
            if (httpData.getStatus() == HttpStatus.SC_OK) {
                return loadResponseDate(httpData.getData(),creditQualityDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private GbossClient convert2GbossClient(){
        GbossConfig config = new GbossConfig();
        config.setEndpoint(guoZhenConfig.getEndpoint());
        config.setDesKey(guoZhenConfig.getDesKey());
        config.setEncrypted(true);
        config.setAccount(guoZhenConfig.getAccount());
        config.setAccountpwd(guoZhenConfig.getAccountPassword());
        config.setDesCharset(guoZhenConfig.getDesCharset());
        config.setTimeout(15000);
        return new GbossClient(config);
    }

    private DataResponseVO loadResponseDate(String httpData,CreditQualityDto creditQualityDto){
        try {
            Element doc = DocumentHelper.parseText(httpData).getRootElement().element("attentionScores").element("attentionScore");
             String score = doc.element("score").getData().toString();

            DataResponseVO dataResponseVO = DataResponseVO.builder()
                    .identityId(creditQualityDto.getIdentityId())
                    .name(creditQualityDto.getName())
                    .outerId(creditQualityDto.getOuterId())
                    .score(score)
                    .build();
            return dataResponseVO;

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
