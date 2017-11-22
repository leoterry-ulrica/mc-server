package com.dist.message.server.model;

import java.io.Serializable;
/**
 * 登录信息
 * @author weifj
 *
 */
public class LoginInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String userId;
	private String pwd;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	@Override
	public String toString() {
		return "LoginInfo [clientId=" + clientId + ", userId=" + userId + ", pwd=" + pwd + "]";
	}
}
