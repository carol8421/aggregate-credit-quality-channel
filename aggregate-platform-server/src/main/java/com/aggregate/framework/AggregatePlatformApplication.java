package com.aggregate.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationPid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication
public class AggregatePlatformApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(AggregatePlatformApplication.class)
				.main(AggregatePlatformApplication.class)
				.run(args);
		log.info("----AggregatePlatformApplication Start PID={}----", new ApplicationPid().toString());
		context.registerShutdownHook();
	}
}