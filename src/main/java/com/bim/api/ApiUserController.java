package com.bim.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.entity.SysDict;
import com.bim.entity.SysUser;
import com.bim.entity.UserLoginVO;
import com.bim.entity.UserProMenuBO;
import com.bim.service.LoginService;
import com.bim.service.SysDicService;
import com.bim.service.SysUserService;
import com.bim.util.Code;
import com.bim.util.LanguageUtil;
import com.bim.util.R;

/**
 * 用户api
 * @author wangc
 * @date 2017年8月8日
 * @description ApiUserController.java
 */
@Controller
@RequestMapping("api/user")
public class ApiUserController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private SysDicService dicService;
	
	/**
	 * 发送登录短信
	 * @return
	 */
	@RequestMapping(value="/loginSms",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject sendLoginSms(HttpServletRequest req,String mobile){
		return loginService.sendVerifySMS(mobile);
	}
	
	/**
	 * 短信登录
	 * @param mobile 手机号
	 * @param vcode 短信验证码
	 * @return
	 */
	@RequestMapping(value="/loginBySms",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject loginBySms(HttpServletRequest request, HttpServletResponse response,UserLoginVO userLoginVO){
		//UserLoginVO userLoginVO=new UserLoginVO(SystemManage.getInstance().getCurrentUserId(), mobile,vcode,request.getRemoteAddr(),udid,"","deviceType","sms");
		userLoginVO.setIp(request.getRemoteAddr());
		userLoginVO.setLoginMode("sms");
		userLoginVO.setDeviceType("mobile");
		return loginService.loginBySms(userLoginVO);
	}
	
	/**
	 * 确认二维码登录
	 * @param account 用户名
	 * @param pwd 密码
	 * @param uuid 二维码唯一标识
	 */
	@RequestMapping(value="/loginConfirm",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject loginConfirm(String accessToken,String uuid){
		return loginService.loginConfirm(accessToken, uuid);
	}
	
	@RequestMapping("/my")
	@ResponseBody
	public JSONObject my(HttpServletRequest request){
		List<SysDict> dicUserLevels= dicService.getListByParentCode("user_level");
		int userId=SystemManage.getInstance().getCurrentUserId();
		SysUser user= userService.getById(userId);
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("token", request.getParameter("accessToken"));
		resultMap.put("sur_names", user.getSurNames());
		resultMap.put("real_name", user.getRealName());
		resultMap.put("mobile_zone", user.getMobileZone());
		resultMap.put("mobile", user.getMobile());
		resultMap.put("email", user.getEmail());
		resultMap.put("status", user.getStatus());
		//resultMap.put("user_level", user.getUserLevel());
		String userLevelName="";
		for(SysDict dic : dicUserLevels){
			if(dic.getCode().equals(user.getUserLevel())){
				userLevelName=dic.getShowName();
				break;
			}
		}
		resultMap.put("user_level", userLevelName);
		resultMap.put("render_level", user.getRenderLevel());
		resultMap.put("voice", user.getVoice());
		resultMap.put("memo",user.getMemo());
		resultMap.put("password", user.getPassword());
		return R.ToJson(true, resultMap, LanguageUtil.getMessage("Code200"));
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject logout(HttpServletRequest request,HttpServletResponse response){
		Cookie cookie=new Cookie("JSESSIONID", request.getRequestedSessionId());
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return loginService.doLogout();
	}
	
	@RequestMapping(value="/getListByPro",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getListByPro(int proId,String key){
		List<Map<String,Object>> list=userService.getListByProAndKey(proId, key);
		return R.ToJson(true, list, LanguageUtil.getMessage("Code200"));
	}
	
	@RequestMapping(value="/getMenuByPro",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getMenuByPro(Integer proId){
		if(proId==null || proId==0)
			return R.ToJson(false, null, "请选择项目!");
		List<UserProMenuBO> list= SystemManage.getInstance().getCurrentUser().getProMenus();
		Optional<UserProMenuBO> result=list.stream().filter(p->p.getProId().equals(proId)).findFirst();
		if(!result.isPresent())
			return R.ToJson(false, null, LanguageUtil.getMessage("DangQianYongHuMeiYouXiangMuQuanXian")); 
		return R.ToJson(true, result.get(), LanguageUtil.getMessage("Code200"));
	}
	
	/**
	 * 修改密码
	 * @param userId
	 * @param newPwd
	 * @param confirmPwd
	 * @param originalPwd
	 * @return
	 */
	@RequestMapping(value="/modifyPwd",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject modifyPwd(Integer userId, String newPwd, String confirmPwd, String originalPwd){
		return userService.modifyPassword(newPwd, confirmPwd, originalPwd);
	}
	
	/**
	 * 确认二维码扫描
	 * @param accessToken 移动端凭证
	 * @param uuid 二维码唯一标识
	 */
	@RequestMapping(value = "/loginScanConfirm", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject loginScanConfirm(String accessToken, String uuid) {
		return loginService.loginScan(accessToken, uuid);
	}
	
	/**
	 * 设置密码
	 * @param userId
	 * @param newPwd
	 * @param confirmPwd
	 * @return
	 */
	@RequestMapping(value="/setPwd",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject setPwd(String newPwd, String confirmPwd){
		return userService.setPwd(SystemManage.getInstance().getCurrentUserId(), newPwd, confirmPwd);
	}
	
	/**
	 * 设置密码
	 * @param userId
	 * @param newPwd
	 * @param confirmPwd
	 * @return
	 */
	@RequestMapping(value="/getAllName",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getAllName(){
		return R.ToJson(true, userService.getAllName(), LanguageUtil.getMessage("Code200"));
	}
}
