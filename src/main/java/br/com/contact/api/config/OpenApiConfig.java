package br.com.contact.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("REST API with Java 17 and Spring Boot 3")
				.version("v1")
				.description("API to manage contacts, implementing new features from Spring Security 6")
				.termsOfService("https://github.com/felipecampos94")
				.license(
					new License()
						.name("Apache 2.0")
						.url("https://github.com/felipecampos94")
					)
				);
	}

}
