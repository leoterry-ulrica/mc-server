package com.dist.message.server.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Import(SwaggerConfig.class)
public class SpringConfig extends WebMvcConfigurerAdapter {

	@Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {

		 registry.addResourceHandler("swagger-ui.html")
	       .addResourceLocations("classpath:/META-INF/resources/");

	     registry.addResourceHandler("/webjars/**")
	       .addResourceLocations("classpath:/META-INF/resources/webjars/");
	 }
}
