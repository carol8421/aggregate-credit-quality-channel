package com.aggregate.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationPid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication
@ServletComponentScan
public class AggregateAPIApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(AggregateAPIApplication.class)
				.main(AggregateAPIApplication.class)
				.run(args);
		log.info("----AggregateAPIApplication Start PID={}----", new ApplicationPid().toString());
		context.registerShutdownHook();
	}
}