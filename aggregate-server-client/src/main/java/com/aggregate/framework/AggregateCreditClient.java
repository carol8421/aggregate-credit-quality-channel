package com.aggregate.framework;

import com.aggregate.framework.constants.ClientConstant;
import com.aggregate.framework.dto.CreditQualityDto;
import com.aggregate.framework.dto.RequestClientDto;
import com.aggregate.framework.dto.RequestDto;
import com.aggregate.framework.manager.FileManager;
import com.aggregate.framework.utils.HttpClientUtil;
import com.aggregate.framework.utils.JsonUtil;
import com.aggregate.framework.utils.codec.Base64Utils;
import com.aggregate.framework.utils.codec.DesUtils;
import com.aggregate.framework.utils.codec.RSAUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class AggregateCreditClient {
    private static final AggregateCreditClient client;

    private static Boolean isLoad = Boolean.FALSE;


    private static String ip;
    private static String port;
    private static String privateKey;
    private static String clientId;
    private static String clientSecret;

    private static String requestUrl;

    static {
        client = new AggregateCreditClient();
    }

    private AggregateCreditClient(){}

    public static AggregateCreditClient getInstance(String activeProfile){

        if(!isLoad){
            loadProperties(activeProfile);
        }

        return  client;
    }

    private static void loadProperties(String activeProfile){
        String propertiesName = FileManager.PROPERTIES_NAME;

        if(StringUtils.isNotBlank(activeProfile)){

           String[] nameArray =propertiesName.split("\\.");

            StringBuffer sb  = new StringBuffer();
            sb.append(nameArray[0])
                    .append("-")
                    .append(activeProfile)
                    .append(".")
                    .append(nameArray[1]);

            propertiesName = sb.toString();
        }
        FileManager.loadProperties(propertiesName);

        ip = FileManager.getMessage(ClientConstant.OPEN_API_IP);
        port = FileManager.getMessage(ClientConstant.OPEN_API_PORT);
        privateKey = FileManager.getMessage(ClientConstant.OPEN_API_PRIVATE_KEY);
        clientId = FileManager.getMessage(ClientConstant.OPEN_API_CLIENT_ID);
        clientSecret = FileManager.getMessage(ClientConstant.OPEN_API_CLIENT_SECRET);

        requestUrl = constructRequestUrl(ip,port);
    }

    private static String  constructRequestUrl( String ip,String port){
        String url = HttpClientUtil.REQUEST_URL;
        url = url.replace("{ip}",ip).replace("{port}",port);
        return url;
    }


    public String sendHttpRequest(RequestClientDto requestClientDto){
        try {
            String requestDtoJson = constructRequestDtoJsonString(requestClientDto);
            Map<String,String> headerMasp = constructHeaderMasp();
            HttpResponse response = HttpClientUtil.requestPost(requestUrl,requestDtoJson,headerMasp);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    return EntityUtils.toString(resEntity,"utf-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String constructRequestDtoJsonString(RequestClientDto requestClientDto) throws Exception{
        CreditQualityDto creditQualityDto = CreditQualityDto.builder()
                .clientId(clientId)
                .outerId(requestClientDto.getOuterId())
                .serverName(requestClientDto.getServerName())
                .data(JSON.toJSONString(requestClientDto.getDataMap()))
                .build();

        String creditQualityStr = JsonUtil.toString(creditQualityDto);

        byte[] encodedData = RSAUtils.encryptByPrivateKey(creditQualityStr.getBytes(), privateKey);
        String encodedStr = Base64Utils.encode(encodedData);
        String sign = RSAUtils.sign(encodedData, privateKey);

        RequestDto requestDto =new RequestDto();
        requestDto.setData(encodedStr);
        requestDto.setSign(sign);

        return JsonUtil.toString(requestDto);
    }

    private Map<String,String> constructHeaderMasp(){
        String decodeSecret = DesUtils.decode(clientId,clientSecret);

        Map<String,String> headerMap = new HashMap<>(2);
        headerMap.put(ClientConstant.HEADER_CLIENT_ID,clientId);
        headerMap.put(ClientConstant.HEADER_CLIENT_SECRET,decodeSecret);
        return headerMap;
    }
}
