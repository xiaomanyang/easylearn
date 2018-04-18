package com.bim.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.CurrentUser;
import com.bim.dao.SysActionMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.SysAction;
import com.bim.entity.SysUser;
import com.bim.entity.UserProMenuBO;
import com.bim.service.LoginService;
import com.bim.service.SysMenuService;
import com.bim.util.Code;
import com.bim.util.R;

@Controller
@RequestMapping("api/auth")
public class ApiAuthController {
	
	@Autowired
	private  SysActionMapper actionMapper;
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SysMenuService menuService;
	
	/**
	 * 用户凭证 (token)校验
	 * @param token
	 */
	@RequestMapping(value = "/loginverify")
	@ResponseBody
	public JSONObject loginverify(String accessToken) {
		SysAction action= actionMapper.getByToken(accessToken);
		if(action==null)
			return R.ToJson(false, null, "用户未登录");
		SysUser user=userMapper.selectByPrimaryKey(action.getUserId());		//获取用户信息
		if(user==null){
			//用户不存在或被禁用
			return R.ToJson(false, null, Code.C_201);
		}
		if(1==user.getStatus()){
			return R.ToJson(false, null, "用户已禁用");
		}
		CurrentUser currentUser= loginService.getCurrentUserByUser(user);
		
		//List<UserProMenuBO> list= menuService.getProDataMenu(user.getId());
		List<UserProMenuBO> listMenu= menuService.getMenu(user.getId());
		//currentUser.setProDataMenus(list);
		currentUser.setProMenus(listMenu);
		return R.ToJson(true, currentUser, Code.C_200);
	}
	
	/**
	 * 用户凭证token校验 移动端
	 * @param token
	 */
	@RequestMapping(value = "/loginValidByMobile")
	@ResponseBody
	public JSONObject loginValidByMobile(String accessToken) {
		SysAction action= actionMapper.getByToken(accessToken);
		if(action==null)
			return R.ToJson(false, null, "用户未登录");
		SysUser user=userMapper.selectByPrimaryKey(action.getUserId());		//获取用户信息
		if(user==null){
			//用户不存在或被禁用
			return R.ToJson(false, null, Code.C_201);
		}
		if(1==user.getStatus()){
			return R.ToJson(false, null, "用户已禁用");
		}
		return R.ToJson(true, user.getId(), Code.C_200);
	}
	
	
	/**
	 * 用户退出
	 * @param token
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public JSONObject logout(HttpServletRequest request, String accessToken) {
		HttpSession session=request.getSession(false);
		if(session!=null)
			session.invalidate();
		if(!loginService.doLogout(accessToken)){
			return R.ToJson(false, "fail", "退出失败");
		}
		return R.ToJson(true, "success", Code.C_200);
	}
	
	/**
	 * 问题审阅-责任人/参与人 下拉选数据源
	 * @param proId
	 * @param key
	 * @return
	 */
	@RequestMapping(value="/getListByPro",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getListByPro(int proId,String key){
		List<Map<String,Object>> list=userMapper.getListByProAndKey(proId, key);
		return R.ToJson(true, list, Code.C_200);
	}
	
	/**
	 * 根据id列表查询用户
	 */
	@RequestMapping(value="/getUserListByIds",method=RequestMethod.POST)
	@ResponseBody
	public List<SysUser> getUserListByIds(Integer[] ids){
		/*List<Integer> list= JSONObject.parseArray(ids, Integer.class);
		Integer[] arrIds=new Integer[list.size()];*/
		List<SysUser> user=userMapper.getListByIds(ids);
		return user;
	}
}
