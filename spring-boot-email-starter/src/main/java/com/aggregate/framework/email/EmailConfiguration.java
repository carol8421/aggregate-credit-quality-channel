package com.aggregate.framework.email;

import com.aggregate.framework.email.config.EmailConfig;
import com.aggregate.framework.email.service.MailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(EmailConfig.class)
public class EmailConfiguration {

    @Bean
    @ConditionalOnBean(EmailConfig.class)
    public MailService mailService() {
        return new MailService();
    }

}
