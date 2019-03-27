package com.aggregate.framework.manager;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

public class FileManager {

    public static int MAP_MIN_SIZE = 16;

    public static final String PROPERTIES_NAME ="aggregate-credit.properties";

    private static Properties creditProperties = null;

    /**
     * 读取配置文件
     * @param propertiesName
     * @return
     */
    public static InputStream loadProperties(String propertiesName){
        InputStream inputStreamServer = null;
        // 加载属性文件
        try {
            inputStreamServer = FileManager.class.getClassLoader().getResourceAsStream(propertiesName);

            creditProperties = new Properties();
            creditProperties.load(inputStreamServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStreamServer;
    }

    /**
     * 根据KEY获得读取的配置文件的value
     * @param key
     * @return
     */
    public static String getMessage(String key){
        String message = "";

        if (StringUtils.isEmpty(key)) {
            return "";
        }

        if(creditProperties != null ){
            // 信息内容
            message = creditProperties.getProperty(key);

            if (message == null) {
                // 将KEY设置成值
                message = key;
            }
        }
        return message;
    }

}