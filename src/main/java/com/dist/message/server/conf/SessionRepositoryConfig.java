package com.dist.message.server.conf;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Arrays;
/**
 * Spring Session store configured
 * @author weifj
 *
 */
@Configuration
public class SessionRepositoryConfig {

	@Bean
	public SessionRepository<ExpiringSession> inMemorySessionRepository() {
		return new MapSessionRepository();
	}

	@Bean
	public FilterRegistrationBean sessionRepositoryFilterRegistration() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new DelegatingFilterProxy(new SessionRepositoryFilter<>(inMemorySessionRepository())));
		filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
		return filterRegistrationBean;
	}
}
