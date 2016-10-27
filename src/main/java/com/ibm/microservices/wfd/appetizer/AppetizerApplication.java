package com.ibm.microservices.wfd.appetizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
@EnableScheduling
public class AppetizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppetizerApplication.class, args);
		System.out.println("Running "+AppetizerApplication.class+" via Spring Boot!");
	}
	@Bean
	public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo())
              .select()
              .paths(regex("/appetizers"))
              .build();
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
					 .title("Appetizers API")
					 .description("What's for dinner appetizers microservice application API description using Swagger 2.0\n"
					 							+ "This microservice is part of the What's for dinner microservices reference application.")
					 .contact(new Contact("CASE Microservices Team","","almarazj@ie.ibm.com"))
					 .version("0.2")
					 .build();
	}
	
}
