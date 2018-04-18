package com.bim.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bim.base.BaseClass;
import com.bim.base.CurrentUser;
import com.bim.base.SystemManage;
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysServerMapper;
import com.bim.entity.BimProject;
import com.bim.entity.SysMenu;
import com.bim.entity.SysServer;
import com.bim.util.Constant;
import com.bim.util.LanguageUtil;
import com.bim.util.PropertiesUtil;

import io.jsonwebtoken.lang.Collections;

@Controller
public class HomeController extends BaseClass{
	@Autowired
	private BimProjectMapper projectMapper;
	
	@Autowired
	private SysServerMapper serverMapper;
	
	@RequestMapping("/home")
	public String home(HttpServletRequest request,String currRole){
		//默认为系统管理员
		if(StringUtils.isEmpty(currRole)){
			currRole=Constant.SYSTEM_ROLE_0;
		}
		
		//检查角色
		CurrentUser userInfo=SystemManage.getInstance().getCurrentUser();
		request.setAttribute("user", userInfo);
		if(!Collections.contains(userInfo.getRoles().iterator(), currRole)){
			info(userInfo.getUser().getAccount()+" 不存在角色:"+currRole);
			return "404";
		}
		
		List<SysMenu> menus=null;
		switch(currRole){
			case Constant.SYSTEM_ROLE_0:
				menus=userInfo.getMenus();
				break;
			case Constant.SYSTEM_ROLE_1:
				menus=new ArrayList<>();
				SysMenu menu= new SysMenu();
				menu.setCode("01");
				menu.setMenuName("用户管理");
				menu.setUrl("/user2/init.do");
				menus.add(menu);
				break;
			default:
				break;
		}
		
		request.setAttribute("menus", menus);
		request.setAttribute("currRole", currRole);
		/*项目数据管理员或者有2个以上管理员角色的用户显示返回列表*/
		request.setAttribute("showBack", userInfo.getRoles().size()>1 || userInfo.getRoles().contains(Constant.SYSTEM_ROLE_2));
		return "home";
	}
	
	@RequestMapping("/prolist")
	public String proList(HttpServletRequest request){
		boolean isEn=Locale.US.equals(LanguageUtil.getLocale());
		CurrentUser user= SystemManage.getInstance().getCurrentUser();
		String proServer=PropertiesUtil.getValue("debugProjectServer");
		request.setAttribute("user", user);
		request.setAttribute("roles", user.getRoles());
		List<BimProject> list= projectMapper.getProListByUserId(user.getUser().getId());
		if(!Collections.isEmpty(list)){
			String path=PropertiesUtil.getValue("requestPath");
			for (BimProject bimProject : list) {
				if(isEn){
					bimProject.setProName(bimProject.getProNameEn());
				}
				bimProject.setImgUrl(StringUtils.isEmpty(bimProject.getImgUrl())?"": path+bimProject.getImgUrl());
				if(StringUtils.isEmpty(proServer)){
					SysServer server=serverMapper.selectByPrimaryKey(bimProject.getServerId());
					bimProject.setAddress(server==null?"":server.getAddress());
					continue;
				}
				bimProject.setAddress(proServer); 
			}
		}
		request.setAttribute("proList", list);
		request.setAttribute("token", user.getToken());
		return "prolist";
	}
}
