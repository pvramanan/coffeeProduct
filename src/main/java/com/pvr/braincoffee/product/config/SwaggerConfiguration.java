package com.pvr.braincoffee.product.config;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("product group").apiInfo(getApiInfo()).select()
				.paths(getPathSelector()).build();
	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder().contact(getContact()).title("Product API").description("API Description").build();
	}

	private Predicate<String> getPathSelector() {
		return Pattern.compile("/products").asPredicate();
	}

	private Contact getContact() {
		return new Contact("Prasanna Vengataramana", "www.wiredCoffee.com", "learnprasanna@gmail.com");
	}
}
