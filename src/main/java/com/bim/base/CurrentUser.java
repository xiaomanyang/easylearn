package com.bim.base;

import java.util.List;
import java.util.Map;

import com.bim.entity.BimProject;
import com.bim.entity.SysDepartment;
import com.bim.entity.SysMenu;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysUser;
import com.bim.entity.UserProMenuBO;

/**
 * 当前登录用户信息
 * @author wangc
 * @date 2017年8月2日
 * @description CurrentUser.java
 */
public class CurrentUser{
	private String token; 
	private SysUser user;
	private SysDepartment dept;
	private SysOrganization organization;
	private String currentRole;
	//用户角色列表
	private List<String> roles;
	//系统管理员后台管理菜单sign=9
	private List<SysMenu> menus;
	//用户项目数据管理菜单sign=1
	private List<UserProMenuBO> proDataMenus;
	//项目权限
	private List<BimProject> projects;
	/**
	 * 用户前台菜单 sign=1
	 */
	private List<UserProMenuBO> proMenus;

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public SysDepartment getDept() {
		return dept;
	}

	public void setDept(SysDepartment dept) {
		this.dept = dept;
	}

	public SysOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(SysOrganization organization) {
		this.organization = organization;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<SysMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}

	public List<UserProMenuBO> getProDataMenus() {
		return proDataMenus;
	}

	public void setProDataMenus(List<UserProMenuBO> proDataMenus) {
		this.proDataMenus = proDataMenus;
	}

	public List<UserProMenuBO> getProMenus() {
		return proMenus;
	}

	public void setProMenus(List<UserProMenuBO> proMenus) {
		this.proMenus = proMenus;
	}

	public List<BimProject> getProjects() {
		return projects;
	}

	public void setProjects(List<BimProject> projects) {
		this.projects = projects;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
