package com.bim.entity;

import java.util.Date;

public class SysSMSLog {
    private Integer id;

    private String mobile;

    private String vcode;

    private String content;

    private Date sendTime;

    private Integer status;

    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getvcode() {
        return vcode;
    }

    public void setvcode(String vcode) {
        this.vcode = vcode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public SysSMSLog(){
    	
    }
    
    /**
     * 
     * @param mobile
     * @param vcode
     * @param content
     * @param sendTime
     * @param status
     * @param memo
     */
	public SysSMSLog(String mobile, String vcode, String content, Date sendTime, Integer status, String memo) {
		super();
		this.mobile = mobile;
		this.vcode = vcode;
		this.content = content;
		this.sendTime = sendTime;
		this.status = status;
		this.memo = memo;
	}
    
    
}