package com.dist.message.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
/**
 * websocket的一个ApplicationListener事件，SessionDisconnectEvent，订阅它，然后websocket通道链接成功之后，就广播这个事件。
 * 断开链接的监听
 * @author weifj
 *
 */
@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(SessionDisconnectedEventListener.class);

	/**
	 * afterConnectionClosed事件后触发
	 */
	@Override
	public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
		LOG.info(headerAccessor.toString());
		StompCommand command = headerAccessor.getCommand();
		if(command.name().equalsIgnoreCase("DISCONNECT")) {
			LOG.info(">>>已断开连接");
		}
	}
}
