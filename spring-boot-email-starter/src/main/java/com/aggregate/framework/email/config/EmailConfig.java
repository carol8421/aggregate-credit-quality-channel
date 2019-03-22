package com.aggregate.framework.email.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "mail")
public class EmailConfig {
        private String protocol ;
        private String host ;
        private String port ;
        private String from ;
        private String username ;
        private String password ;
        private String auth ;
        private String debug ;
}
