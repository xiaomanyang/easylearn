package com.bim.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bim.entity.SysUser;

/**
 * 系统资源管理类
 * @author wangc
 * @date 2017年8月3日
 * @description SystemManage.java
 */
public class SystemManage {
	private static SystemManage instance=new SystemManage();
	private SystemManage(){}
	public static SystemManage getInstance(){
		return instance;
	}
	
	/**
	 * 当前用户session key
	 */
	public static final String CURRENT_USER = "current_user";
	/**
	 * 当前用户request key
	 */
	public static final String CURRENT_USERID = "current_userid";
	
	public HttpServletRequest getCurrentRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 设置当前用户
	 * @param user
	 */
	public String setCurrentUser(CurrentUser currentUser){
		HttpServletRequest request=getCurrentRequest();
		HttpSession session=request.getSession(false);
		if(session!=null){
			session.invalidate();
		}
		session=request.getSession();
		session.setAttribute(CURRENT_USER,currentUser);
		return session.getId();
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	public CurrentUser getCurrentUser(){
		CurrentUser currentUser=null;
		HttpSession session=getCurrentRequest().getSession(false);
		if(session==null)
			return null;
		Object obj=session.getAttribute(CURRENT_USER);
		if(obj!=null && obj instanceof CurrentUser)
			currentUser=(CurrentUser)obj;
		return currentUser;
	}
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public SysUser getSysUser(){
		return getCurrentUser().getUser();
	}
	
	/**
	 * 获取当前用户账号
	 * @return
	 */
	public String getCurrentUserName(){
		return getSysUser().getAccount();
	}
	
	/**
	 * 获取当前用户id
	 * @return
	 */
	public int getCurrentUserId(){
		int userId=getCurrentUserIdByToken();
		if(userId!=0) 
			return userId;
		return getSysUser().getId();
	}
	
	/**
	 * 移除用户
	 */
	public boolean removeCurrentUser(){
		HttpSession session=getCurrentRequest().getSession(false);
		if(session==null)
			return true;
		session.removeAttribute(CURRENT_USER);
		session.invalidate();
		return true;
	}
	
	/**
	 * 根据token获取当前用户id
	 * @return
	 */
	private int getCurrentUserIdByToken(){
		Object obj=getCurrentRequest().getAttribute(CURRENT_USERID);
		return StringUtils.isEmpty(obj)?0: ((int)obj);
	}
}
