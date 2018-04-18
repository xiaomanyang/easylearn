package com.bim.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.BaseClass;
import com.bim.base.SystemManage;
import com.bim.dao.SysMenuMapper;
import com.bim.dao.SysProGroupMapper;
import com.bim.entity.CheckBoxTree;
import com.bim.entity.Combobox;
import com.bim.entity.ComboboxGroup;
import com.bim.entity.SysMenu;
import com.bim.entity.SysProGroup;
import com.bim.entity.SysUser;
import com.bim.service.SysUserService;
import com.bim.util.R;

@Controller
@RequestMapping("/user")
public class SysUserController extends BaseClass{
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysProGroupMapper sysProGroupMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;
	
	/**
	 * 用户页面初始化
	 */
    @RequestMapping("/init")
    public String init(){
		return "admin/user";
    }
	
	/**
	 * 添加用户
	 * @return
	 */
	@RequestMapping(value="/addOrEditUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOrEditUser(@Valid SysUser user,BindingResult bindingResult){
		//参数校验
		if (bindingResult.hasErrors()) {
            return R.To(false, null, bindingResult.getFieldError().getDefaultMessage());
        }
		return sysUserService.addOrEditUser(user);
	}
	
	/**
	 * 用户启用禁用
	 * @return
	 */
	@RequestMapping(value="/updateStatusById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateStatusById(SysUser user){
		//参数校验
		return sysUserService.updateStatusById(user);
	}
	
	/**
	 * 用户列表
	 * @param pageNumber 开始位置
	 * @param pageSize	每页数量
	 * @param userPrimaryId 用户id
	 * @param account 账号
	 * @return
	 */
	@RequestMapping(value="/listUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject listUser(int page, int rows,String account){
		return sysUserService.listUser(page,rows,account);
	}
	
	/**
	 * 根据用户id查询部门
	 * @param loginUserId
	 * @return
	 */
	@RequestMapping(value="/deptList", method = RequestMethod.POST)
	@ResponseBody
	public List<ComboboxGroup> deptList(){
		return sysUserService.deptList();
	}
	
	/**
	 * 查询用户等级
	 * @return
	 */
	@RequestMapping(value="/userLevel", method = RequestMethod.POST)
	@ResponseBody
	public List<Combobox> userLevel(){
		return sysUserService.userLevel();
	}
	
	/**
	 * 项目权限树,管理页面树
	 * @return
	 */
	@RequestMapping(value="/listTree", method = RequestMethod.POST)
	@ResponseBody
	public List<CheckBoxTree> listTree(Integer userId,Integer type){
		return sysUserService.listTree(userId,type);
	}
	
	/**
	 * 项目权限树,管理页面树
	 * @return
	 */
	@RequestMapping(value="/havePurview", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject havePurview(Integer userId,Integer type,@RequestParam(value = "ids[]") String[] ids){
		return sysUserService.havePurview(userId,type,ids);
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value="/modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject modifyPassword(String newPwd,String confirmPwd,String originalPwd){
		return sysUserService.modifyPassword(newPwd,confirmPwd,originalPwd);
	}
	
	/**
	 * TODO 查询用户分配的项目组
	 * @param userId
	 * @return 项目组所有，分配给用户的项目组userId不为空
	 * List<SysProGroup>
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	@RequestMapping("userGroupRel")
	@ResponseBody
	public List<SysProGroup> userGroupRel(Integer userId){
		return sysProGroupMapper.listAllOfUserRel(userId);
	}
	
	/**
	 * TODO 保存用户分配的项目组信息
	 * @param userId
	 * @param groupsIds
	 * @return
	 * JSONObject
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	@RequestMapping("saveUserGroupRel")
	@ResponseBody
	public JSONObject saveUserGroupRel(int userId, int[] groupIds){
		return sysUserService.saveUserGroupRel(userId, groupIds);
	}
	
	/**
	 * TODO 查询管理员分配的系统权限
	 * @param userId
	 * @return
	 * List<SysMenu>
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	@RequestMapping("managerMenu")
	@ResponseBody
	public List<SysMenu> managerMenu(Integer userId){
		SysUser currentUser = SystemManage.getInstance().getSysUser();
		if(1 == currentUser.getUserType())
			return sysMenuMapper.selectManagerMenuByManagerId(userId, currentUser.getId());
		return sysMenuMapper.selectManagerMenu(userId);
	}
	
	/**
	 * TODO 保存用户分配的系统管理权限
	 * @param userId
	 * @param menuIds
	 * @return
	 * JSONObject
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	@RequestMapping("saveUserMenuRel")
	@ResponseBody
	public JSONObject saveUserMenuRel(int userId, int[] menuIds){
		return sysUserService.saveUserMenuRel(userId, menuIds);
	}
}
