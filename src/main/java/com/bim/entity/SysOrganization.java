package com.bim.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class SysOrganization {
    private Integer id;
    @NotBlank(message = "{name}")  
    private String name;
    
    @NotBlank(message = "{nameEn}")  
    private String nameEn;
    
    @NotBlank(message = "{short_name}")  
    private String shortName;

    private String region;

    private Integer status;

    private String memo;

    private Date createTime;

    private Date updateTime;

    private Integer createUser;

    private Integer updateUser;
    
    private List<SysDepartment> departments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

	public List<SysDepartment> getDepartments() {
		return departments;
	}

	public void setDepartments(List<SysDepartment> departments) {
		this.departments = departments;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
}