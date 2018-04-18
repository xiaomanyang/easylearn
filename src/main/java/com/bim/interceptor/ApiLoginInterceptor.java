package com.bim.interceptor;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bim.base.CurrentUser;
import com.bim.base.SystemManage;
import com.bim.dao.SysActionMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.SysAction;
import com.bim.entity.SysUser;
import com.bim.service.LoginService;
import com.bim.util.Code;
import com.bim.util.Constant;
import com.bim.util.R;

public class ApiLoginInterceptor implements HandlerInterceptor {
	@Autowired
	private SysActionMapper actionMapper;
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private LoginService LoginService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("--------------------------------ApiLoginInterceptor :"+request.getRequestURI());
		String token= request.getParameter("accessToken");
    	if(null==token || "".equals(token)){
    		if(checkSession(request)){
    			return true;
    		}
    		if(checkSessionId(request, response)){
    			return true;
    		}
    		String str=R.ToJson(false, new ArrayList<>(), "用户未登录！").toJSONString();
    		writeJson(response, str);
    		return false;
    	}
        SysAction action= actionMapper.getByToken(token);
        if(action==null){
        	String str=R.ToJson(false, new ArrayList<>(), Code.C_201).toJSONString();
    		writeJson(response, str);
        	return false;
        }
        request.setAttribute(SystemManage.CURRENT_USERID, action.getUserId());
        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void writeJson(HttpServletResponse response,String resultData) throws IOException{
		response.setCharacterEncoding("UTF-8");  
        response.setContentType("application/json;charset=utf-8"); 
		response.setStatus(200);
		//response.getOutputStream().print("中文字");  //这行会出错
		response.getWriter().print(resultData); 
		response.getWriter().close();
	}
	
	/**
	 * 根据session检查是否登录
	 * @param request
	 * @return
	 */
	public boolean checkSession(HttpServletRequest request){
		HttpSession session=request.getSession(false);
		if(session==null){
			return false;
		}
		Object obj = session.getAttribute(SystemManage.CURRENT_USER);
		if(StringUtils.isEmpty(obj)){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据cookie中的sessionid 检查数据库
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean checkSessionId(HttpServletRequest request,HttpServletResponse response){
		String sessionId=request.getRequestedSessionId();
		SysAction action= actionMapper.getByToken(sessionId);
		if(action==null){
			return false;
		}
		//获取用户信息
		SysUser user= userMapper.selectByPrimaryKey(action.getUserId());
		//用户不存在或被禁用
		if(user==null || 1==user.getStatus()){
			return false;
		}
		CurrentUser currentUser= LoginService.getCurrentUserByUser(user);
		
		HttpSession session= request.getSession(true);
		session.setAttribute(SystemManage.CURRENT_USER, currentUser);
		//存cookie
		Cookie c=new Cookie("JSESSIONID", session.getId());
		c.setMaxAge(Constant.LOGIN_EXPIRE_TIME);
		response.addCookie(c);
		
		//更新token
		action.setToken(session.getId());
		actionMapper.updateByPrimaryKey(action);
		return true;
	}
}
