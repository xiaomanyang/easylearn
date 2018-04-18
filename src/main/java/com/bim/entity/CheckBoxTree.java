package com.bim.entity;

import java.util.List;

/**
 * easyUI树
 *@Description: 
 *@date 2017年7月21日
 *@version V1.0
 *@author yang.y
 */
public class CheckBoxTree {
	
	private String id;
	
	private String text;
	
	private List<CheckBoxTree> children;
	
	private String state;//开启关闭
	
	private Boolean checked;
	
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<CheckBoxTree> getChildren() {
		return children;
	}

	public void setChildren(List<CheckBoxTree> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
