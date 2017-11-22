package com.dist.message.server.conf;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.dist.message.server.model.jwt.Audience;
import com.dist.message.server.utils.JWTUtil;

/**
 * 握手拦截器
 * 
 * @author weifj
 *
 */
@Component
public class SessionAuthHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	private static Logger LOG = LoggerFactory.getLogger(SessionAuthHandshakeInterceptor.class);
    @Autowired
    private Audience audience;
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		LOG.info(">>>握手前......");
		HttpSession session = getSession(request);
		String token = getTokenFromRequest(request);
		if(StringUtils.isEmpty(token)) {
			LOG.info(">>>token不存在，拒绝访问");
			return false;
		}
		if(JWTUtil.isValid(token, audience.getBase64Secret())) {
			LOG.info(">>>token验证通过");
			String userId = JWTUtil.getName(token, audience.getBase64Secret());
			attributes.put(WSConstants.SESSION_USERNAME, userId);
			return true;
		} else {
			LOG.info(">>>token验证不通过，拒绝访问");
			return false;
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		LOG.info(">>>握手后......");
		super.afterHandshake(request, response, wsHandler, ex);
	}

	private HttpSession getSession(ServerHttpRequest request) {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			return serverRequest.getServletRequest().getSession();
		}
		return null;
	}

	/**
	 * 从请求信息中获取token值
	 * 
	 * @param request
	 * @return token值
	 */
	private String getTokenFromRequest(ServerHttpRequest request) {
		String token = "";
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			// 默认从header里获取token值
			token = serverRequest.getServletRequest().getHeader(WSConstants.TOKEN);
			if (StringUtils.isEmpty(token)) {
				// 从请求信息中获取token值
				token = serverRequest.getServletRequest().getParameter(WSConstants.TOKEN);
			}
		}
		return token;
	}
}
