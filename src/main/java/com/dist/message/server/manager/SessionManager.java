package com.dist.message.server.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;
/**
 * session管理器
 * @author weifj
 *
 */
public class SessionManager {

	private static Logger LOG = LoggerFactory.getLogger(SessionManager.class);
	
	private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/**
	 * session id 和session映射
	 */
	private static Map<String, WebSocketSession> ID_SESSION = new ConcurrentHashMap<String, WebSocketSession>();
	/**
	 * 用户id和session id映射
	 */
	private static Map<String, String> USER_ID_SESSION_ID= new ConcurrentHashMap<String, String>();
	/**
	 * session id和用户id映射
	 */
	private static Map<String, String> SESSION_ID_USER_ID= new ConcurrentHashMap<String, String>();
	/**
	 * 注册session
	 * @param session
	 */
	public static void register(WebSocketSession session) {
		readWriteLock.writeLock().lock();
		try {
			ID_SESSION.put(session.getId(), session);
		} catch(Exception ex) {
			LOG.error(">>>注册失败，详情：" + ex.getMessage());
		} finally {
			// 释放写锁
			readWriteLock.writeLock().unlock();
		}
	}
	/**
	 * 注册用户和sessionid 关联
	 * @param userId
	 * @param sessionId
	 */
	public static void register(String userId, String sessionId) {
		readWriteLock.writeLock().lock();
		try {
			USER_ID_SESSION_ID.put(userId, sessionId);
		} catch(Exception ex) {
			LOG.error(">>>注册失败，详情：" + ex.getMessage());
		} finally {
			// 释放写锁
			readWriteLock.writeLock().unlock();
		}
	}
	/**
	 * 反注册session
	 * @param session
	 */
	public static void unRegister(WebSocketSession session) {
		readWriteLock.writeLock().lock();
		try {
			ID_SESSION.remove(session.getId());
			String userId = SESSION_ID_USER_ID.get(session.getId());
			if(!StringUtils.isEmpty(userId)) {
				USER_ID_SESSION_ID.remove(userId);
			}
			SESSION_ID_USER_ID.remove(session.getId());
		} catch(Exception ex) {
			LOG.error(">>>反注册失败，详情：" + ex.getMessage());
		} finally {
			// 释放写锁
			readWriteLock.writeLock().unlock();
		}
	}
	/**
	 * 在线个数
	 * @return
	 */
	public static int onlineCount() {
		return ID_SESSION.size();
	}
}
