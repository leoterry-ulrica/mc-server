package com.dist.message.server.swagger;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig{

	 @Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .groupName("consumer-api") //  组名
	          .select()  
	          .apis(RequestHandlerSelectors.basePackage("com.dist.message.server.controller"))       
	          .paths(PathSelectors.any())      // PathSelectors.any() | PathSelectors.ant("/rest/sysservice/*")  
	                                                        // 不做限制，否则带有{}的参数接口识别不了 
	          .build()
	          .apiInfo(apiInfo());                                           
	    }
	 
	 private ApiInfo apiInfo() {
		 ApiInfo apiInfo = new ApiInfo(
				    "数慧统一消息平台API 导航",//大标题
	                "API Document管理",//小标题
	                "1.0",//版本
	                "NO terms of service",
	                new Contact("weifj", "", "weifj@dist.com.cn"),//作者
	                "上海数慧OpenAPI",//链接显示文字
	                "http://www.dist.com.cn/"//网站链接
	        );
		 return apiInfo;
		}
}
