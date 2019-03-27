package com.aggregate.framework.gzt.channel;

import cn.id5.gboss.GbossClient;
import cn.id5.gboss.GbossConfig;
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
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;

import java.util.StringJoiner;

@Slf4j
@ServiceChannel(channelName = "guozhen")
public class GuoZhenChannel{

    CreditQualityChannelConfig.GuoZhenConfig guoZhenConfig;
    GbossClient client;

    @ServiceMethod(methodName = "queryCredit")
    public DataResponseVO queryCreditQuality(CreditQualityDto creditQualityDto) {
        initConfig ();
        GuoZhenDto<QueryCreditDto> guoZhenDto = convert2GuoZhenDto(creditQualityDto);

        StringJoiner sj =new StringJoiner(",", "", "");
        sj.add(guoZhenDto.getOuterId()).add(guoZhenDto.getT().getName()).add(guoZhenDto.getT().getIdentityId());
        log.debug("[StringJoiner] is [{}]",sj.toString());
        try {
/*            HttpResponseData httpData = client.invokeSingle(guoZhenConfig.getProduct(), sj.toString());
            log.debug("[GuoZhenChannel] get HttpResponseData is : [{}]", httpData.getData());
            if (httpData.getStatus() == HttpStatus.SC_OK) {
                return loadResponseDate(httpData.getData(), guoZhenDto);
            }*/
            DataResponseVO dataResponseVO = DataResponseVO.builder()
                    .identityId(guoZhenDto.getT().getIdentityId())
                    .name(guoZhenDto.getT().getName())
                    .outerId(guoZhenDto.getOuterId())
                    .score("0")
                    .data("<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\r\\n<data>\\r\\n  <message>\\r\\n    <status>0</status>\\r\\n    <value>处理成功</value>\\r\\n  </message>\\r\\n  <attentionScores>\\r\\n    <attentionScore>\\r\\n      <code>1</code>\\r\\n      <message>评分成功</message>\\r\\n      <wybs>ttt111112</wybs>\\r\\n      <inputXm>马涛</inputXm>\\r\\n      <inputZjhm>610429199009085178</inputZjhm>\\r\\n      <score>0</score>\\r\\n    </attentionScore>\\r\\n  </attentionScores>\\r\\n</data>\\r\\n\\r\\n")
                    .build();
            return dataResponseVO;


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


    private DataResponseVO loadResponseDate(String data,GuoZhenDto<QueryCreditDto> guoZhenDto) {
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


    public GuoZhenChannel() {

    }


    public void initConfig (){
        if(guoZhenConfig == null ){
            guoZhenConfig = SpringApplicationContext.getBean(CreditQualityChannelConfig.GuoZhenConfig.class);
            client = convert2GbossClient();
        }
    }
}
