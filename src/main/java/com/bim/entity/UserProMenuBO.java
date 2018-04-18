package com.bim.entity;

import java.util.List;

/**
 * 用户项目菜单业务对象
 * @author wangc
 * @date 2017年9月8日
 * @description
 */
public class UserProMenuBO {
	private Integer proId;
	private String proName;
	private List<SysMenu> menus;
	public Integer getProId() {
		return proId;
	}
	public void setProId(Integer proId) {
		this.proId = proId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public List<SysMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}
	
}


