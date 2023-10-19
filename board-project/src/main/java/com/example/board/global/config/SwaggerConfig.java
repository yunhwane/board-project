package com.example.board.global.config;


import springfox.documentation.service.Tag;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.cglib.beans.BeanGenerator.addProperties;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

    @Bean
    public Docket ShopApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("member API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.board"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.ShopApiInfo());

    }

    private ApiInfo ShopApiInfo() {
        return new ApiInfoBuilder()
                .title("member API")
                .description("회원 관리 API")
                //.termsOfServiceUrl("http://www.Core-security.com")
                .version("1.0")
                .build();
    }

}
