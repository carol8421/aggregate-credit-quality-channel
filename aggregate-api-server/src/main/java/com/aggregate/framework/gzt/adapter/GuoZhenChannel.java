package com.aggregate.framework.gzt.adapter;

import cn.id5.gboss.GbossClient;
import cn.id5.gboss.GbossConfig;
import cn.id5.gboss.http.HttpResponseData;
import com.aggregate.framework.gzt.bean.dto.GuoZhenDto;
import com.aggregate.framework.gzt.bean.dto.QueryCreditDto;
import com.aggregate.framework.open.annotations.ServiceChannel;
import com.aggregate.framework.open.annotations.ServiceMethod;
import com.aggregate.framework.open.bean.dto.CreditQualityDto;
import com.aggregate.framework.open.bean.vo.DataResponseVO;
import com.aggregate.framework.open.common.components.SpringApplicationContext;
import com.aggregate.framework.open.common.configuration.CreditQualityChannelConfig;
import com.aggregate.framework.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;

@Slf4j
@ServiceChannel(channelName = "guozhen")
public class GuoZhenChannel {

    CreditQualityChannelConfig.GuoZhenConfig guoZhenConfig;
    GbossClient client;


    @ServiceMethod(methodName = "queryCredit")
    public DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto) {
        initConfig ();
        GuoZhenDto<QueryCreditDto> guoZhenDto = convert2GuoZhenDto(creditQualityDto);

        StringBuffer sb = new StringBuffer();
        sb.append(guoZhenDto.getOuterId())
                .append(",").append(guoZhenDto.getT().getName())
                .append(",").append(guoZhenDto.getT().getIdentityId());

        try {
            HttpResponseData httpData = client.invokeSingle(guoZhenConfig.getProduct(), sb.toString());
            log.debug("[GuoZhenChannel] get HttpResponseData is : [{}]", httpData.getData());
            if (httpData.getStatus() == HttpStatus.SC_OK) {
                return loadResponseDate(httpData.getData(), guoZhenDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private GbossClient convert2GbossClient() {
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

    private GuoZhenDto<QueryCreditDto> convert2GuoZhenDto(CreditQualityDto creditQualityDto) {
        String dateStr = creditQualityDto.getData();

        QueryCreditDto queryCreditDto = JsonUtil.parseObject(dateStr, QueryCreditDto.class);

        GuoZhenDto guoZhenDto = new GuoZhenDto();
        BeanUtils.copyProperties(creditQualityDto, guoZhenDto);
        guoZhenDto.setT(queryCreditDto);
        return guoZhenDto;
    }


    public DataResponseVO loadResponseDate(String data,GuoZhenDto<QueryCreditDto> guoZhenDto) {
        try {
            Element doc = DocumentHelper.parseText(data).getRootElement().element("attentionScores").element("attentionScore");
            String score = doc.element("score").getData().toString();

            DataResponseVO dataResponseVO = DataResponseVO.builder()
                    .identityId(guoZhenDto.getT().getIdentityId())
                    .name(guoZhenDto.getT().getName())
                    .outerId(guoZhenDto.getOuterId())
                    .score(score)
                    .data(data)
                    .build();
            return dataResponseVO;

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    private GuoZhenChannel() {
        if (LazyHolder.LAZY != null) {
            throw new RuntimeException("不允许创建多个实例");
        }
    }


    private void initConfig (){
        if(guoZhenConfig == null ){
            guoZhenConfig = SpringApplicationContext.getBean(CreditQualityChannelConfig.GuoZhenConfig.class);
            client = convert2GbossClient();
        }
    }
    public static final GuoZhenChannel getInstance() {
        //guoZhenConfig = SpringApplicationContext.getBean(CreditQualityChannelConfig.GuoZhenConfig.class);
        //client = convert2GbossClient();
        //在返回结果以前，一定会先加载内部类
        return LazyHolder.LAZY;
    }

    private static class LazyHolder {
        private static final GuoZhenChannel LAZY = new GuoZhenChannel();
    }
}
