package com.dist.message.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import com.dist.message.server.manager.SessionManager;

public class WebSocketSessionHandlerDecorator extends WebSocketHandlerDecorator {

	private static final Logger LOG = LoggerFactory.getLogger(WebSocketSessionHandlerDecorator.class);
	// private static final List<WebSocketSession> users = new ArrayList<WebSocketSession>();
	
	public WebSocketSessionHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.info(">>>afterConnectionEstablished");
		LOG.info(">>>session id : " + session.getId());
		// users.add(session);  
		SessionManager.register(session);
        System.out.println(">>>当前在线用户的数量是:"+SessionManager.onlineCount()); 
		super.afterConnectionEstablished(session);
	}
	/**
	 * 所有消息都被拦截
	 * 	CONNECT/SEND/SUBSCRIBE/...消息体
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		LOG.info(">>>handleMessage");
		LOG.info(">>>消息内容：" + message.getPayload());
		//LOG.info(">>>开始广播消息");
		//TextMessage textMessage = new TextMessage(message.getPayload().toString());  
		//sendMessageToUsers(textMessage);
		super.handleMessage(session, message);
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		LOG.info(">>>handleTransportError");
		if(session.isOpen()){  
            session.close();  
        }  
		  LOG.debug(">>>连接出错，关闭链接......");
        // users.remove(session);  
		SessionManager.unRegister(session);
		super.handleTransportError(session, exception);
	}
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		LOG.info(">>>afterConnectionClosed");
		 // users.remove(session);  
		SessionManager.unRegister(session);
		LOG.info(">>>当前在线用户的数量是:"+SessionManager.onlineCount()); 
		super.afterConnectionClosed(session, closeStatus);
	}
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
}
