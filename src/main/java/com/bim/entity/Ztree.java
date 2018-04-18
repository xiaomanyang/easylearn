package com.bim.entity;

import java.util.List;

import com.bim.util.Constant;
/**
 * Ztree数据结构
 *@Description: 
 *@date 2017年8月25日
 *@version V1.0
 *@author yang.y
 */
public class Ztree {
	
	private Integer id;
	
	private String name;
	
	private String icon = Constant.ICON_PARENT;
	
	private List<?> children;
	
	private Integer projectId;
	
	private String scanTime;
	
	public String getScanTime() {
		return scanTime;
	}

	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

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

	public List<?> getChildren() {
		return children;
	}

	public void setChildren(List<?> children) {
		this.children = children;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
