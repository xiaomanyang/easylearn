package com.bim.util;

public class HttpResult {
	private int statusCode;
	private Object data;
	private String message;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HttpResult(int statusCode, Object data, String message) {
		super();
		this.statusCode = statusCode;
		this.data = data;
		this.message = message;
	}
	
}
