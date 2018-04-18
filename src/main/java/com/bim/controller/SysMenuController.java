package com.bim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bim.base.SystemManage;
import com.bim.entity.SysMenu;
import com.bim.entity.SysUser;
import com.bim.service.SysMenuService;

@Controller
@RequestMapping("menu")
public class SysMenuController {
	
	@Autowired
	private SysMenuService menuService;
	
	/**
	 * 管理首页 左侧功能菜单
	 * @return
	 */
	@RequestMapping(value="/getMenuList",method=RequestMethod.POST)
	@ResponseBody
	public List<SysMenu> getMenuList(@RequestParam("currRole") String currRole){
		SysUser user=SystemManage.getInstance().getSysUser();
		if(user.getUserType()==0)
			return menuService.getAll();
		return menuService.getListByUser(user.getId());
	}
}
