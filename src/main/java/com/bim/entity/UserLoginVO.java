package com.bim.entity;

public class UserLoginVO {
	private Integer userId;
	private String mobile;
	private String vcode;
	private String ip;
	private String udid;
	
	/**
	 * 系统类型/平台  (windows,android,ios)
	 */
	private String platform;
	
	/**
	 * 设备类型 (pc,pad,phone)
	 */
	private String deviceType;
	
	/**
	 * 登录方式 (account,sms,qrcode)
	 */
	private String loginMode;
	
	private String appVersion;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}
	public UserLoginVO(){
		
	}
	
	/**
	 * 
	 * @param userId
	 * @param mobile
	 * @param vcode
	 * @param ip
	 * @param udid
	 * @param platform
	 * @param deviceType
	 * @param loginMode
	 */
	public UserLoginVO(Integer userId, String mobile, String vcode, String ip, String udid, String platform,
			String deviceType, String loginMode,String appVersion) {
		super();
		this.userId = userId;
		this.mobile = mobile;
		this.vcode = vcode;
		this.ip = ip;
		this.udid = udid;
		this.platform = platform;
		this.deviceType = deviceType;
		this.loginMode = loginMode;
		this.appVersion=appVersion;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	
}
