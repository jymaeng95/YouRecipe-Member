package com.yourecipe.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())            //Swagger API 문서로 만들기 원하는 basePackage 경로
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("YouRecipe Member API")
                .description("YouRecipe Member API Reference for Developers")
                .termsOfServiceUrl("https://github.com/jymaeng95/YouRecipe-Member")
                .license("@Zayson License")
                .licenseUrl("http://you-recipe.com").version("1.0").build();
    }
}
