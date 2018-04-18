package com.bim.controller;

import java.util.ArrayList;
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
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysMenuMapper;
import com.bim.dao.SysOrganizationMapper;
import com.bim.dao.SysProGroupMapper;
import com.bim.dao.SysUserGroupRelMapper;
import com.bim.dao.SysUserMapper;
import com.bim.dao.SysUserMenuRelMapper;
import com.bim.dao.SysUserProjectMenuRelMapper;
import com.bim.entity.BimProject;
import com.bim.entity.CheckBoxTree;
import com.bim.entity.Combobox;
import com.bim.entity.ComboboxGroup;
import com.bim.entity.SysDepartment;
import com.bim.entity.SysMenu;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysProGroup;
import com.bim.entity.SysUser;
import com.bim.entity.SysUserMenuRel;
import com.bim.entity.SysUserProjectMenuRel;
import com.bim.http.TreeMenu;
import com.bim.service.SysUserService;
import com.bim.util.Code;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/user2")
public class SysUser2Controller extends BaseClass{
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysProGroupMapper sysProGroupMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;
	
	@Autowired
	private SysOrganizationMapper sysOrganizationMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private BimProjectMapper bimProjectMapper;
	@Autowired
	private SysUserGroupRelMapper sysUserGroupRelMapper;
	/**
	 * 前端权限
	 */
	@Autowired
	private SysUserProjectMenuRelMapper sysUserProjectMenuRelMapper;
	/**
	 * 后端权限
	 */
	@Autowired
	private SysUserMenuRelMapper sysUserMenuRelMapper;
	
	 
	/**
	 * 用户页面初始化
	 */
    @RequestMapping("/init")
    public String init(){
		return "admin/user2";
    }
    
    /////////////// user2 start
    
    /**
     * TODO 获取组织机构树
     * @return
     * List<TreeMenu>
     * date:2017年11月22日
     * user:BIM-10
     */
    @RequestMapping("orgTree")
    @ResponseBody
    public List<TreeMenu> getOrgTree(){
    	List<SysOrganization> orgs =sysOrganizationMapper.selectOrgDepByManagerId(SystemManage.getInstance().getCurrentUserId());
    	List<TreeMenu> tm = new ArrayList<TreeMenu>();
    	TreeMenu treeMenu = new TreeMenu("0", "组织机构");
    	treeMenu.setState("open");
    	treeMenu.setIconCls("icon-propwoer");
    	tm.add(treeMenu);
    	
    	List<TreeMenu> orgMenus = new ArrayList<TreeMenu>();
    	treeMenu.setChildren(orgMenus);
    	for(SysOrganization org : orgs){
    		TreeMenu orgMenu = new TreeMenu(String.valueOf(org.getId()*-1), org.getName());
    		orgMenu.setState("open");
    		List<SysDepartment> deps = org.getDepartments();
    		if(null != deps){
    			List<TreeMenu> depMenus = new ArrayList<TreeMenu>();
    			orgMenu.setChildren(depMenus);
    			orgMenu.setIconCls("icon-propage");
    			for(SysDepartment dep : deps){
    				TreeMenu depMenu = new TreeMenu(dep.getId().toString(), dep.getDepartmentName());
    				depMenu.setState("open");
    				depMenus.add(depMenu);
    			}
    		}
    		orgMenus.add(orgMenu);
    	}
    	return tm;
    }
    
    /**
     * TODO 获取权限树
     * @param userId
     * @param type 获取的权限类型，0：后台权限，1：前台权限
     * @return
     * List<TreeMenu>
     * date:2017年11月21日
     * user:BIM-10
     */
    @RequestMapping("menuTree")
    @ResponseBody
    public List<TreeMenu> getMenuTree(int userId, int type){
    	List<TreeMenu> tms = new ArrayList<TreeMenu>();
    	
    	int currentUserId = SystemManage.getInstance().getCurrentUserId();
    	// 获得管理员管理的项目
    	List<BimProject> projects = bimProjectMapper.getProjectOfManager(currentUserId);
    	// 获得菜单
    	List<SysMenu> menus = sysMenuMapper.selectBySign(type);
    	// 获得用户分配的项目
    	List<ProjectMenu> projectMenus = new ArrayList<ProjectMenu>();
    	if(1 == type){// 前
    		List<SysUserProjectMenuRel> userMenuRels = sysUserProjectMenuRelMapper.selectByUserId(userId);
    		for(SysUserProjectMenuRel um : userMenuRels){
    			ProjectMenu pm = new ProjectMenu(um.getProjectId(), um.getMenuId());
    			projectMenus.add(pm);
    		}
    	}else{ // 后
    		List<SysUserMenuRel> userMenuRels = sysUserMenuRelMapper.selectByUserId(userId);
    		for(SysUserMenuRel um : userMenuRels){
    			ProjectMenu pm = new ProjectMenu(um.getProjectId(), um.getMenuId());
    			projectMenus.add(pm);
    		}
    	}
    	
    	/// 下面开始我的表演，创建树
    	for(BimProject project : projects){ // 项目节点
    		TreeMenu ptm = new TreeMenu(String.valueOf(project.getId()), project.getProName());
    		List<TreeMenu> mtms = new ArrayList<TreeMenu>();
    		ptm.setChildren(mtms);
    		// 可能设置点样式图标 。。。
    		for(SysMenu menu : menus){ // 菜单节点
    			TreeMenu mtm = new TreeMenu(String.valueOf(project.getId())+"|"+String.valueOf(menu.getId()), menu.getMenuName());
    			mtm.setState("open");
    			for(ProjectMenu pm : projectMenus){
    				if(project.getId() == pm.project_id && menu.getId() == pm.menu_id)
    					mtm.setChecked(true);
    			}
    			mtms.add(mtm);
    		}
    		
    		tms.add(ptm);
    	}
    	return tms;
    }
    
    class ProjectMenu{
    	public int project_id;
    	public int menu_id;
    	public ProjectMenu() {
		}
    	public ProjectMenu(int project_id, int menu_id) {
    		this.project_id = project_id;
    		this.menu_id = menu_id;
		}
    }
    
    /**
     * TODO 查询用户信息
     * @param mobile
     * @return
     * SysUser
     * date:2017年11月22日
     * user:BIM-10
     */
    @RequestMapping("userInfo")
    @ResponseBody
    public JSONObject getUserByMobile(String mobile){
    	int currentUserId = SystemManage.getInstance().getCurrentUserId();
    	SysUser user = sysUserMapper.selectByMobile(mobile);
    	if(null == user)
    		return R.To(true, null, Code.C_200);
    	if(user.getId() == currentUserId){
    		user.setEditable(true);
    	}else{
    		List<Integer> managerIds = sysUserGroupRelMapper.getManagerIdsByManagerId(currentUserId);
    		for(Integer managerId : managerIds){
    			if(user.getCreateUser() == managerId){
    				user.setEditable(true);
    			}
    		}
    	}
    	if(!user.isEditable()){
    		user.setEmail("***");
			user.setMemo("***");
			user.setMobile(user.getMobile().substring(0, 3)+"****"+user.getMobile().substring(7));
			user.setCreateTime(null);
			user.setStatus(-1);
			user.setUserLevelName("***");
    	}
    	return R.To(true, user, Code.C_200);
    }
    
    /////////////// user2 stop
	
	/**
	 * 添加用户
	 * @return
	 */
	@RequestMapping(value="/addOrEditUser2", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOrEditUser(@Valid SysUser user,BindingResult bindingResult){
		//参数校验
		if (bindingResult.hasErrors()) {
            return R.To(false, null, bindingResult.getFieldError().getDefaultMessage());
        }
		return sysUserService.addOrEditUser2(user);
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
	 * TODO 项目权限管理员，根据自己的id查询，查询所管理的项目关联的用户
	 * @param page
	 * @param rows
	 * @param account
	 * @param mobile
	 * @param sysDepId
	 * @return
	 * JSONObject
	 * date:2017年11月20日
	 * user:BIM-10
	 */
	@RequestMapping(value="/listUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject listUser(int page, int rows,String account, Integer sysDepId){
		PageHelper.startPage(page,rows);
		int currentUserId = SystemManage.getInstance().getCurrentUserId();
		List<SysUser> list = sysUserMapper.getUsersOfManager(currentUserId, sysDepId, account);
		// 如果是系统管理员，啥都不管了，都可以编辑
		if(0 == sysUserMapper.selectByPrimaryKey(currentUserId).getUserType()){
			for(SysUser user : list){
				user.setEditable(true);
			}
			return R.ToPage(true, list,Code.C_200,new PageInfo(list).getTotal());
		}
		// 其他管理员的id集合
		List<Integer> managerIds = sysUserGroupRelMapper.getManagerIdsByManagerId(currentUserId);
		for(SysUser user : list){
			// 当前管理员创建的用户
			if(null != user.getCreateUser() && user.getCreateUser() == currentUserId){
				user.setEditable(true);
				continue;
			}
			// 自己
			if(user.getId() == currentUserId){
				user.setEditable(true);
				continue;
			}
			// 其他管理员创建的用户
    		for(Integer managerId : managerIds){
    			if(user.getCreateUser() == managerId){
    				user.setEditable(true);
    				break;
    			}
    		}
    		if(!user.isEditable()){
    			user.setEmail("***");
    			user.setMemo("***");
    			user.setMobile(user.getMobile().substring(0, 3)+"****"+user.getMobile().substring(7));
    			user.setCreateTime(null);
    			user.setStatus(-1);
    			user.setUserLevelName("***");
    		}
		}
		return R.ToPage(true, list,Code.C_200,new PageInfo(list).getTotal());
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
	 * TODO 用户前后台权限分配
	 * @param userId 用户id
	 * @param type 前后台，1：前台，0：后台
	 * @param ids 项目id|菜单id
	 * @return
	 * JSONObject
	 * date:2017年11月22日
	 * user:BIM-10
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
