package com.dist.message.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * 此配置已注入SimpMessagingTemplate
 * 
 * @author weifj
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	private static Logger LOG = LoggerFactory.getLogger(WebSocketConfig.class);

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 设置服务器广播消息的基础路径
		config.enableSimpleBroker("/topic");
		// 设置客户端订阅消息的基础路径，配置到 @MessageMapping Controller的前缀
		config.setApplicationDestinationPrefixes("/app");
		// 给指定用户发送一对一的主题前缀是/user
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		// 为java stomp client提供链接
		registry.addEndpoint("/stomp")
				// 拦截器
				// .setHandshakeHandler(new DefaultHandshakeHandler(new
				// TomcatRequestUpgradeStrategy()))
				.setAllowedOrigins("*").setHandshakeHandler(new SessionAuthHandshakeHandler())
				.addInterceptors(new SessionAuthHandshakeInterceptor());

		// 为sockjs客户端提供链接
		registry.addEndpoint("/sockjs").setAllowedOrigins("*").setHandshakeHandler(new SessionAuthHandshakeHandler())
				.addInterceptors(new SessionAuthHandshakeInterceptor()).withSockJS();
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
			@Override
			public WebSocketHandler decorate(WebSocketHandler webSocketHandler) {
				return new WebSocketSessionHandlerDecorator(webSocketHandler);
			}
		});
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new ChannelInterceptorAdapter() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				LOG.info("recv : " + message);
				return super.preSend(message, channel);
			}
		});
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new ChannelInterceptorAdapter() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				LOG.info(">>>send: " + message);
				return super.preSend(message, channel);
			}
		});
	}
}
