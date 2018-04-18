package com.bim.entity;

import java.util.Date;
import java.util.UUID;

public class SysLoginLog {
    private Integer id;

    private Integer userId;

    private String account;

    private String mobile;

    private String loginMode;

    private String platform;

    private String ip;

    private String udid;

    private String token;

    private Date loginTime;

    private Date logoutTime;

    private Byte status;
    
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public SysLoginLog(){
    	
    }
    
	public SysLoginLog(SysAction sysAction){
    	this.token= sysAction.getToken();
		this.loginTime= sysAction.getLoginTime();
		this.userId=sysAction.getUserId();
		this.account=sysAction.getAccount();
		this.mobile=sysAction.getMobile();
		this.ip=sysAction.getIp();
		this.platform=sysAction.getPlatformType();
		this.loginMode=sysAction.getLoginMode();
		this.udid=sysAction.getUdid();
		this.status=new Byte("0");
		this.memo="登录成功！";
    }
}