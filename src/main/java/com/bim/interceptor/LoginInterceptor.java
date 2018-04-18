package com.bim.interceptor;

import java.io.IOException;

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
import com.bim.util.Constant;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private SysActionMapper actionMapper;
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private LoginService loginService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("========================================LoginInterceptor :"+request.getRequestURI());
		HttpSession session=request.getSession(false);
    	if(session==null || StringUtils.isEmpty(session.getAttribute(SystemManage.CURRENT_USER))){
    		if(checkToken(request,response)){
    			return true;
    		}
    		if(checkIsAjax(request)){
    			ajaxTimeOutHandle(response);
    		}else{
    			/*response.sendRedirect("http://localhost:8080/Bim/login.do");*/
    			response.getWriter().write("<script>window.parent.location.href='"+ request.getContextPath()+"/login.do'</script>");
    		}
    		return false;
    	}
        return true;
        //检查是否有token 有则进行token验证
        /*if(!checkToken(request)){
        	HttpSession session=request.getSession(false);
        	if(session==null){
        		if(checkIsAjax(request)){
        			ajaxTimeOutHandle(response);
        		}else{
        			response.sendRedirect(request.getContextPath()+"/login.do");
        		}
        		return false;
        	}else{
        		Object obj = session.getAttribute(SystemManage.CURRENT_USER);  
                if (obj==null || "".equals(obj.toString())) {  
                	if(checkIsAjax(request)){
                		ajaxTimeOutHandle(response);
            		}else{
            			response.sendRedirect(request.getContextPath()+"/login.do");
            		}
                    return false;
                }else{
                	return true;
                }
        	}
        }
        return true;*/
    }
	
	/**
	 * ajax 超时后设置response
	 * @param response
	 * @throws Exception
	 */
	public void ajaxTimeOutHandle(HttpServletResponse response) throws Exception{
	    /*response.setCharacterEncoding("UTF-8");  
        response.setContentType("application/json;charset=utf-8"); 
		response.setStatus(0);
		//response.getOutputStream().print("中文字");  //这行会出错*/
		response.getWriter().print("请先登录系统！");
		response.addHeader("sessionstatus", "timeout");
		response.sendError(302);
		response.getWriter().close();
	}
	
	public boolean checkToken(HttpServletRequest request,HttpServletResponse response){
		String token= request.getParameter("userjsessionid");
		if(StringUtils.isEmpty(token)){
			Cookie[] cookies=request.getCookies();
			if(cookies==null){
				return false;
			}
			for (Cookie cookie : cookies) {
				if(Constant.TOKEN_KEY.equals(cookie.getName())){
					token=cookie.getValue();
					break;
				}
			}
		}
		if(StringUtils.isEmpty(token)){
			return false;
		}
		
		SysAction action= actionMapper.getByToken(token);
		if(action==null){
			return false;
		}
		
		//获取用户信息
		SysUser user=userMapper.selectByPrimaryKey(action.getUserId());
		//用户不存在或被禁用
		if(user==null || 1==user.getStatus()){
			return false;
		}
		CurrentUser currentUser= loginService.getCurrentUserByUser(user);
		currentUser.setToken(action.getToken());
		HttpSession session= request.getSession();
		session.setAttribute(SystemManage.CURRENT_USER, currentUser);
		//存cookie
		Cookie c=new Cookie(Constant.TOKEN_KEY, action.getToken());
		c.setMaxAge(Constant.LOGIN_EXPIRE_TIME);
		System.out.println("perHandle request.getContextPath():"+request.getContextPath());
		c.setPath(request.getContextPath());
		response.addCookie(c);
		return true;
	}
	
	private static final String[] IGNORE_URI = {"/login.do","/login"};
	 
    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        //检查访问的url是否需要登录验证
        String url = request.getRequestURL().toString();
        System.err.println("url:"+url);
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        
        //检查是否有token 有则进行token验证
        if(!flag)
        	flag=checkToken(request);

        if (!flag) {
        	HttpSession session=request.getSession(false);
        	if(session==null){
        		if(checkIsAjax(request)){
        			response.getWriter().write(301);
        			response.setCharacterEncoding("UTF-8");  
                    response.setContentType("text/html");  
                    response.setDateHeader("Expires", 0);  
                    response.setHeader("sessionstatus", "timeout");  
        			 ServletOutputStream out = response.getOutputStream();
        			 response.setStatus(0);
                     out.print("登录超时,请重新登录!");//返回给前端页面的未登陆标识
				     out.flush();
				     out.close();
				     return false;
        		}else{
        			response.sendRedirect(request.getContextPath()+"/login.do");
        		}
        		flag= false;
        	}
        	else{
        		Object obj = session.getAttribute(SystemManage.CURRENT_USER);  
                if (obj==null || "".equals(obj.toString())) {  
                	if(checkIsAjax(request)){
                		 ServletOutputStream out = response.getOutputStream();
            			 response.setStatus(301);
            			 out.print("登录超时,请重新登录!");//返回给前端页面的未登陆标识
    				     out.flush();
    				     out.close();
            		}else{
            			response.sendRedirect(request.getContextPath()+"/login.do");
            		}
                    flag= false;
                }else{
                	flag = true;
                }
        	}
        }
        return flag;
    }*/
    
    /*public boolean checkToken(HttpServletRequest request){
    	String token= request.getParameter("accessToken");
    	if(null==token || "".equals(token))
    		return false;
        SysAction action= actionMapper.getByToken(token);
        if(action==null)
        	return false;
        request.setAttribute(SystemManage.CURRENT_USERID, action.getUserId());
        return true;
    }*/
    
    /**
     * 判断是否是ajax请求
     * @param request
     * @return false/不是 ,true/是
     * @throws IOException 
     */
    public boolean checkIsAjax(HttpServletRequest request) throws IOException{
    	String requestType = request.getHeader("X-Requested-With");
    	if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")){
    		return true;
    	}
    	return false;
    }
}
