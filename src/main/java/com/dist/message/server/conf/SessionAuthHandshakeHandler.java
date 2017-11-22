package com.dist.message.server.conf;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.dist.message.server.model.MyPrincipal;
import com.dist.message.server.model.User;
/**
 * 
 * @author weifj
 *
 */
public class SessionAuthHandshakeHandler extends DefaultHandshakeHandler {

	/**
	 * 该方法可以用来为用户添加标识，返回principal
	 */
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		return new MyPrincipal((User) attributes.get("user"));
	}
}
