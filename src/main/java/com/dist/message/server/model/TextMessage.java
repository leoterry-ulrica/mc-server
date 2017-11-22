package com.dist.message.server.model;

import java.io.Serializable;

public class TextMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long code;
	private String message;
	public TextMessage(Long time, String message) {
		this.code = time;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	
}
