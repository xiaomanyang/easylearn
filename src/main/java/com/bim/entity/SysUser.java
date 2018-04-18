package com.bim.entity;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class SysUser {
	
    private Integer id;

    private String account;
    
    private String password;
    @NotBlank(message = "{real_name}")  
    private String realName;
    @NotBlank(message = "{mobile_zone}")  
    private String mobileZone;
    @NotBlank(message = "{mobile}")  
    @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$", message="{mobile_}")
    private String mobile;
    
    private String completeMobile;
    @NotBlank(message = "{email}")  
    @Pattern(regexp="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message="{email_}")
    private String email;

    private Integer status;
    @NotBlank(message = "{user_level}")  
    private String userLevel;
    
    private String renderLevel;
    
    private String userLevelName;

    private String memo;

    private Integer sysOrgId;
    
    private String sysOrgName;

    private Integer sysDepId;
    
    private String sysDepName;

    private Integer userType;

    private Date createTime;

    private Date updateTime;

    private Integer createUser;

    private Integer updateUser;
    @NotBlank(message = "{sur_names}")  
    private String surNames;
    
    private Integer voice;
    private Integer deviceCount;
    
    private boolean editable = false;
    
	public String getSurNames() {
		return surNames;
	}

	public void setSurNames(String surNames) {
		this.surNames = surNames;
	}

	public String getCompleteMobile() {
		return completeMobile;
	}

	public String getSysOrgName() {
		return sysOrgName;
	}

	public void setSysOrgName(String sysOrgName) {
		this.sysOrgName = sysOrgName;
	}

	public void setCompleteMobile(String completeMobile) {
		this.completeMobile = completeMobile;
	}

	public String getUserLevelName() {
		return userLevelName;
	}

	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}

	public String getSysDepName() {
		return sysDepName;
	}

	public void setSysDepName(String sysDepName) {
		this.sysDepName = sysDepName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobileZone() {
        return mobileZone;
    }

    public void setMobileZone(String mobileZone) {
        this.mobileZone = mobileZone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getRenderLevel() {
		return renderLevel;
	}

	public void setRenderLevel(String renderLevel) {
		this.renderLevel = renderLevel;
	}

	public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getSysOrgId() {
        return sysOrgId;
    }

    public void setSysOrgId(Integer sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public Integer getSysDepId() {
        return sysDepId;
    }

    public void setSysDepId(Integer sysDepId) {
        this.sysDepId = sysDepId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public Integer getVoice() {
		return voice;
	}

	public void setVoice(Integer voice) {
		this.voice = voice;
	}

	public Integer getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(Integer deviceCount) {
		this.deviceCount = deviceCount;
	}
}