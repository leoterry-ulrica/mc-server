package com.dist.message.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * springboot启动类
 *  @SpringBootApplication等价于@Configuration，@EnableAutoConfiguration和@ComponentScan
 * @author weifj
 *
 */
@SpringBootApplication(scanBasePackages = { "com.dist.message.server.*" })
public class Application extends WebMvcConfigurerAdapter {

	/**
	 * 利用fastjson替换掉默认的jackson，且解决中文乱码问题，在pom.xml引入fastjson的jar包
	 * @param converters
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		 // 定义一个convert转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		// 添加fastjson的配置信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat, 
				SerializerFeature.WriteMapNullValue,             // 是否输出值为null的字段,默认为false 
				SerializerFeature.WriteNullNumberAsZero,   // 数值字段如果为null,输出为0,而非null 
				SerializerFeature.WriteNullBooleanAsFalse,  // Boolean字段如果为null,输出为false,而非null
				SerializerFeature.WriteNullStringAsEmpty,   // 字符类型字段如果为null,输出为”“,而非null 
				SerializerFeature.WriteNullListAsEmpty);      // List字段如果为null,输出为[],而非null
		
		// 处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastMediaTypes.add(MediaType.APPLICATION_XML);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		// 在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		// 将convert添加到converters中
		converters.add(fastConverter);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
