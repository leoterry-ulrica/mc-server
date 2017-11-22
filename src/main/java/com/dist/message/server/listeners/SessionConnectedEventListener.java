package com.dist.message.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.dist.message.server.conf.WSConstants;
import com.dist.message.server.manager.SessionManager;
/**
 * websocket的一个ApplicationListener事件，SessionConnectedEvent，订阅它，然后websocket通道链接成功之后，就广播这个事件。
 * 通过这个事件，我们可以把用户的登录授权信息保存起来，放到ServletContext中，以便于我们在推送消息的时候，拿到用户信息。
 * 这里你也可以把用户的登录授权信息保存到其他的缓存策略中。
 * @author weifj
 *
 */
@Component
public class SessionConnectedEventListener implements ApplicationListener<SessionConnectEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(SessionConnectedEventListener.class);

	/**
	 * afterConnectionEstablished事件之后触发
	 */
	@Override
	public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
		LOG.info(">>>headerAccessor : " + headerAccessor.toString());
		StompCommand command = headerAccessor.getCommand();
		if(command.name().equalsIgnoreCase("CONNECT")) {
			LOG.info(">>>session id : " + headerAccessor.getSessionId());
			String userId = headerAccessor.getNativeHeader(WSConstants.SESSION_USERNAME).get(0);
			//String.valueOf(headerAccessor.getSessionAttributes().get(WSConstants.SESSION_USERNAME));
			LOG.info(">>>user id：" + userId);
			SessionManager.register(userId, headerAccessor.getSessionId());
		}
	}
}
