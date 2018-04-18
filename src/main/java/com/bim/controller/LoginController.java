package com.bim.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.CurrentUser;
import com.bim.base.SystemManage;
import com.bim.service.LoginService;
import com.bim.util.Constant;
import com.bim.util.PropertiesUtil;
import com.bim.util.QRCodeUtil;
import com.bim.util.R;

@Controller
public class LoginController {
	protected Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private LoginService systemService;

	@RequestMapping("/login")
	public String login(Integer lo,HttpServletRequest req){
		String path=PropertiesUtil.getValue("requestPath");
		String dic="/app";//目录
		//System.out.println("testparam:"+LanguageUtil.getMessage("TestParam", new Object[]{"test"}));
		req.setAttribute("downloadQRCodeUrl", path+dic+"/download_qr.png");
		req.setAttribute("IosDownloadQRCodeUrl", path+dic+"/ios_download_qr.png");
		req.setAttribute("downloadUrl", path+dic+"/bim.apk");
		return (lo==null || lo==0)?"admin/login":"admin/login2";
	}
	
	/**
	 * 获取二维码
	 * @param request
	 * @param response
	 * @param width
	 * @param height
	 * @param uuid
	 */
	@RequestMapping("/loginQRCode")
	public void loginQRCode(HttpServletRequest request,HttpServletResponse response,Integer width,Integer height,String uuid){
		String path=request.getRequestURL().toString().replace(request.getRequestURI(),request.getContextPath()+"/download.html") ;//"http://www.project-5d.com/login.do";
		//String uuid=UUID.randomUUID().toString();
		System.out.println("request.getRequestURL():"+request.getRequestURL());
		String url = path+"?token="+uuid;
		try {
			int iWidth = (width == null ? 200 : width);
			int iHeight = (height == null ? 200 : height);
			QRCodeUtil.createRqCode(url, iWidth, iHeight, response.getOutputStream());
			logger.info(String.format("生成二维码成功： url： %s", url));
		} catch (Exception e) {
			logger.error(String.format("生成二维码失败： url： %s", url), e);
		}
	}
	
	/**
	 * 检查二维码是否确认登录
	 * @param uuid 二维码唯一标识
	 */
	@RequestMapping("/loginByQRCode")
	@ResponseBody
	public JSONObject loginByQRCode(HttpServletRequest request, HttpServletResponse response,String uuid){
		clearOldToken(request,response);
		HttpSession session= request.getSession();
		if(session!=null){
			session.invalidate();
		}
		JSONObject result= systemService.loginByQRCode(uuid);
		setCookie(result, response);
		return result;
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public JSONObject doLogin(HttpServletRequest request, HttpServletResponse response, String account,String pwd){
		clearOldToken(request,response);
		HttpSession session= request.getSession();
		if(session!=null){
			session.invalidate();
		}
		JSONObject result= systemService.doLogin(account, pwd,request.getRemoteAddr(),request.getRemoteHost());
		setCookie(result,response);
		return result;
	}
	
	/**
	 * 如果同浏览器登录第2个用户,则剔除第一个用户
	 * @param request
	 * @param response
	 */
	public void clearOldToken(HttpServletRequest request,HttpServletResponse response){
		String token="";
		CurrentUser user=SystemManage.getInstance().getCurrentUser();
		if(user!=null){
			token=user.getToken();
		}else{
			Cookie[] cookies= request.getCookies();
			if(cookies!=null){
				for (int i = 0; i < cookies.length; i++) {
					if(Constant.TOKEN_KEY.equals(cookies[i].getName())){
						token=cookies[i].getValue();
						
						Cookie c=new Cookie(Constant.TOKEN_KEY, null);
						System.out.println("request.getRemoteHost() ："+request.getRemoteHost());
						String domain="";
						try {
							domain = new URL(request.getRequestURL().toString()).getHost();
							c.setDomain(domain);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error("清除上次登录的token时,获取domain失败");
						}
						c.setPath(request.getContextPath());
						c.setMaxAge(0);
						response.addCookie(c);
						break;
					}
				}
			}
		}
		if(!StringUtils.isEmpty(token)){
			logger.info("剔除上次登录用户,该用户token为："+token);
			systemService.doLogout(token);
		}
	}
	
	private void setCookie(JSONObject obj,HttpServletResponse response){
		if(obj.getBoolean("req")){
			Cookie c=new Cookie(Constant.TOKEN_KEY, obj.getString("rows"));
			c.setMaxAge(Constant.LOGIN_EXPIRE_TIME);
			response.addCookie(c);			
		}
	}
	
	/**
	 * 更新session
	 * @param request
	 */
	public void updateSession(HttpServletRequest request){
		HttpSession session=request.getSession(false);
		if(session==null)
			return;
		//首先将原session中的数据转移至一临时map中  
	    Map<String,Object> tempMap = new HashMap<>();  
	    Enumeration<String> sessionNames = session.getAttributeNames();  
	    while(sessionNames.hasMoreElements()){  
	        String sessionName = sessionNames.nextElement();  
	        tempMap.put(sessionName, session.getAttribute(sessionName));  
	    }  
	    //注销原session，为的是重置sessionId  
	    session.invalidate();  
	    //将临时map中的数据转移至新session  
	    session = request.getSession();  
	    for(Map.Entry<String, Object> entry : tempMap.entrySet()){  
	        session.setAttribute(entry.getKey(), entry.getValue());  
	    }
	}
	
	/**
	 * 手机号+短信登录
	 * @param uuid 二维码唯一标识
	 */
	@RequestMapping("/doLoginByMobile")
	@ResponseBody
	public JSONObject doLoginByMobile(HttpServletRequest request, HttpServletResponse response){
		String mobile=request.getParameter("mobile");
		String sms=request.getParameter("sms");
		if(StringUtils.isEmpty(mobile)){
			return R.To(false, null, "请输入用户名！");
		}
		if(StringUtils.isEmpty(sms)){
			return R.To(false, null, "请输入短信验证码！");
		}
		clearOldToken(request,response);
		HttpSession session= request.getSession();
		if(session!=null){
			session.invalidate();
		}
		JSONObject result= systemService.loginByPcSms(mobile, sms, request.getRemoteAddr());
		setCookie(result,response);
		return result;
	}
	
	@RequestMapping("/doLogout")
	@ResponseBody
	public JSONObject doLogout(HttpServletRequest request,HttpServletResponse response){
		JSONObject result= systemService.doLogout();
		String domain="";
		String path=request.getContextPath();
		try {
			domain = new URL(request.getRequestURL().toString()).getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("退出登录,清空cookie时,获取domain 失败...");
		}
		Cookie cookie=new Cookie("JSESSIONID", null);
		cookie.setMaxAge(0);
		cookie.setDomain(domain);
		cookie.setPath(path);
		response.addCookie(cookie);
		
		Cookie cookieToken=new Cookie(Constant.TOKEN_KEY, null);
		cookieToken.setMaxAge(0);
		cookieToken.setDomain(domain);
		cookieToken.setPath(path);
		response.addCookie(cookieToken);
		return result;
	}
	
	@RequestMapping("/loginDownloadQRCode")
	public void loginDownloadQRCode(HttpServletRequest req,HttpServletResponse response){
		String contextPath=req.getContextPath();
		String url = req.getRequestURL().toString().replace(req.getRequestURI(),"/".equals(contextPath)?"/download.html":contextPath+"/download.html")+"?v="+System.currentTimeMillis();
		try {
			QRCodeUtil.createRqCode(url, 200, 200, response.getOutputStream());
			logger.info(String.format("安卓下载二维码生成成功： url： %s", url));
		} catch (Exception e) {
			logger.error(String.format("安卓下载二维码生成失败： url： %s", url), e);
		}
	}
}
