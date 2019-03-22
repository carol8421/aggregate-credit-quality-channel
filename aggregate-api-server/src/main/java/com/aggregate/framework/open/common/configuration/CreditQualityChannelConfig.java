package com.aggregate.framework.open.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CreditQualityChannelConfig {

    @Data
    @Component(value = "guoZhenConfig")
    @ConfigurationProperties(prefix = "channel.guozhen")
    public static class GuoZhenConfig{
        private String endpoint;
        private String desKey;
        private String account;
        private String accountPassword;
        private String desCharset;
        private String product;

    }
}
