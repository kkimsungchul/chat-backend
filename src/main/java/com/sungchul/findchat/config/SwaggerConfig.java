package com.sungchul.findchat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(
                        new Tag("ChatController","Chat Manager TestController")
                )
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sungchul.findchat"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Find Chat Project Swagger",
                "그냥 이것저것",
                "1.0",
                "http://github.com/kkimsungchul",
                new Contact("kimsungchul", "http://github.com/kkimsungchul", "kimsc1218@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }


}
