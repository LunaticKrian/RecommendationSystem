package com.mislab.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

// Swagger 配置类
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Bean
    public Docket adminApiConfig() {    // Docket 文档对象
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("接口文档")    // API 分组管理
                .apiInfo(adminApiInfo())
                .select()               // 设置分组过滤器
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.mislab.core.systemcore.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    // API文档信息配置：
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("推荐系统API文档")
                .description("本文档描述资金交易平台后台系统的各个模块的接口调用方式")
                .version("1.0")
                .contact(new Contact("爱吃糖的范同学", "http://csbox.wiki", "2793260947@qq.com"))  // 联系人
                .build();
    }
}
