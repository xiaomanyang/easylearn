package com.bim.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysDepartmentMapper;
import com.bim.dao.SysDictMapper;
import com.bim.dao.SysManagerMenuRelMapper;
import com.bim.dao.SysMenuMapper;
import com.bim.dao.SysOrgProjectRelMapper;
import com.bim.dao.SysOrganizationMapper;
import com.bim.dao.SysUserGroupRelMapper;
import com.bim.dao.SysUserMapper;
import com.bim.dao.SysUserMenuRelMapper;
import com.bim.dao.SysUserProjectMenuRelMapper;
import com.bim.entity.BimProject;
import com.bim.entity.CheckBoxTree;
import com.bim.entity.Combobox;
import com.bim.entity.ComboboxGroup;
import com.bim.entity.SysDepartment;
import com.bim.entity.SysDict;
import com.bim.entity.SysManagerMenuRel;
import com.bim.entity.SysMenu;
import com.bim.entity.SysOrgProjectRel;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysUser;
import com.bim.entity.SysUserGroupRel;
import com.bim.entity.SysUserMenuRel;
import com.bim.entity.SysUserProjectMenuRel;
import com.bim.entity.SysUserProjectMenuRelKey;
import com.bim.service.SysUserService;
import com.bim.util.CheckUtil;
import com.bim.util.Code;
import com.bim.util.Constant;
import com.bim.util.LanguageUtil;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysUserGroupRelMapper sysUserGroupRelMapper;
	@Autowired
	private SysDepartmentMapper sysDepartmentMapper;
	@Autowired
	private SysOrganizationMapper sysOrganizationMapper;
	@Autowired
	private SysDictMapper sysDictMapper;
	@Autowired
	private BimProjectMapper bimProjectMapper;
	@Autowired
	private SysUserProjectMenuRelMapper sysUserProjectMenuRelMapper;
	@Autowired
	private SysMenuMapper sysMenuMapper;
	@Autowired
	private SysOrgProjectRelMapper sysOrgProjectRelMapper;
	@Autowired
	private SysUserMenuRelMapper sysUserMenuRelMapper;
	/**
	 * 系统权限管理员权限关系表
	 */
	@Autowired
	private SysManagerMenuRelMapper sysManagerMenuRelMapper;
	
	@Override
	public List<CheckBoxTree> listTree(Integer userId, Integer type) {
		String projectPower = "项目权限";
		String adminPower = "管理页面权限";
		List<CheckBoxTree> totalList = new LinkedList<CheckBoxTree>();
		//根
		CheckBoxTree c = new CheckBoxTree();
		c.setId("0");
		c.setText(type==1?projectPower:adminPower);
		//c.setState("closed");
		//第二级,项目,管理页面权限
		if(type==Constant.user_type_1){//项目权限
			SysUser user = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
			List<SysMenu> SysMenuList = sysMenuMapper.selectBySign(type);
			if(user.getUserType()==Constant.user_type_0){//超级管理员
				List<BimProject> pro = bimProjectMapper.selectAll();
				List<CheckBoxTree> projectList = new LinkedList<CheckBoxTree>();
				for (int i = 0; i < pro.size(); i++) {
					CheckBoxTree checkboxtree = new CheckBoxTree();
					checkboxtree.setId(String.valueOf(pro.get(i).getId()));
					checkboxtree.setText(pro.get(i).getProName());
					checkboxtree.setState("closed");
					List<CheckBoxTree> menu = new LinkedList<CheckBoxTree>();
					for (int j = 0; j < SysMenuList.size(); j++) {
						//查询是否分配了权限
						SysUserProjectMenuRelKey select = new SysUserProjectMenuRelKey();
						select.setMenuId(SysMenuList.get(j).getId());
						select.setProjectId(pro.get(i).getId());
						select.setUserId(userId);
						SysUserProjectMenuRelKey res = sysUserProjectMenuRelMapper.selectByPrimaryKey(select);
						CheckBoxTree checkboxtrees = new CheckBoxTree();
						checkboxtrees.setId(String.valueOf(SysMenuList.get(j).getId()));
						checkboxtrees.setText(SysMenuList.get(j).getMenuName());
						if(res!=null){
							checkboxtrees.setChecked(true);
						}
						menu.add(checkboxtrees);
					}
					checkboxtree.setChildren(menu);
					projectList.add(checkboxtree);
				}
				c.setChildren(projectList);
			}else{//查询自己公司的项目
				SysUser users = sysUserMapper.selectByPrimaryKey(userId);
				List<SysOrgProjectRel> sysOrgProjectRelList = sysOrgProjectRelMapper.selectByOrgId(users.getSysOrgId());
				List<CheckBoxTree> projectList = new LinkedList<CheckBoxTree>();
				for (int i = 0; i < sysOrgProjectRelList.size(); i++) {
					CheckBoxTree checkboxtree = new CheckBoxTree();
					checkboxtree.setId(String.valueOf(sysOrgProjectRelList.get(i).getProId()));
					checkboxtree.setText(sysOrgProjectRelList.get(i).getProName());
					//checkboxtree.setState("closed");
					List<CheckBoxTree> menu = new LinkedList<CheckBoxTree>();
					for (int j = 0; j < SysMenuList.size(); j++) {
						//查询是否分配了权限
						SysUserProjectMenuRelKey select = new SysUserProjectMenuRelKey();
						select.setMenuId(SysMenuList.get(j).getId());
						select.setProjectId(sysOrgProjectRelList.get(i).getProId());
						select.setUserId(userId);
						SysUserProjectMenuRelKey res = sysUserProjectMenuRelMapper.selectByPrimaryKey(select);
						CheckBoxTree checkboxtrees = new CheckBoxTree();
						checkboxtrees.setId(String.valueOf(SysMenuList.get(j).getId()));
						checkboxtrees.setText(SysMenuList.get(j).getMenuName());
						if(res!=null){
							checkboxtrees.setChecked(true);
						}
						menu.add(checkboxtrees);
					}
					checkboxtree.setChildren(menu);
					projectList.add(checkboxtree);
				}
				c.setChildren(projectList);
			}
		}else{//管理页面权限
			List<SysMenu> sysmenulist = sysMenuMapper.selectBySign(type);
			List<CheckBoxTree> admin = new LinkedList<CheckBoxTree>();
			for (int i = 0; i < sysmenulist.size(); i++) {
				//查询是否分配了权限
				SysUserMenuRel select = new SysUserMenuRel();
				select.setMenuId(sysmenulist.get(i).getId());
				select.setUserId(userId);
				SysUserMenuRel res = sysUserMenuRelMapper.selectByPrimaryKey(select);
				CheckBoxTree checkboxtree = new CheckBoxTree();
				checkboxtree.setId(String.valueOf(sysmenulist.get(i).getId()));
				checkboxtree.setText(sysmenulist.get(i).getMenuName());
				if(res!=null){
					checkboxtree.setChecked(true);
				}
				admin.add(checkboxtree);
			}
			c.setChildren(admin);
		}
		totalList.add(c);
		return totalList;
	}
	
	@Override
	public JSONObject listUser(int page, int rows, String account) {
		SysUser user = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
		if(null==user){
			return R.To(false, null,Code.C_201);
		}
		PageHelper.startPage(page,rows);
		user.setMobile(account==null?null:account.trim());
		user.setUserType(0);
		List<SysUser> list = sysUserMapper.selectByOrgId(user);
		for (int i = 0; i < list.size(); i++) {
			SysDepartment dept = sysDepartmentMapper.selectByPrimaryKey(list.get(i).getSysDepId());
			if(dept!=null){
				list.get(i).setSysDepName(dept.getDepartmentName());
				SysOrganization org = sysOrganizationMapper.selectByPrimaryKey(dept.getOrgId());
				if(org!=null){
					list.get(i).setSysOrgName(org.getName());
				}
			}
			SysDict sysDict = sysDictMapper.getSingleByCode(list.get(i).getUserLevel());
			if(sysDict!=null){
				list.get(i).setUserLevelName(sysDict.getShowName());
			}
		}
		return R.ToPage(true, list,Code.C_200,new PageInfo(list).getTotal());
	}
	
	@Transactional
	@Override
	public JSONObject addOrEditUser(SysUser user) {
		//手机号是否存在
		List<SysUser> is = sysUserMapper.selectByMobile(user);
		SysDepartment dept = sysDepartmentMapper.selectByPrimaryKey(user.getSysDepId());
		if(user.getId()!=null){//修改
			SysUser source = sysUserMapper.selectByPrimaryKey(user.getId());
			/*//判断密码是否修改了
			String password = user.getPassword().trim();
			if(password.equals("")){
				user.setPassword(source.getPassword());
			}else{
				//校验密码规则
				boolean flag = CheckUtil.checkContainChinese(password);
				if(flag){
					return R.To(false, null,Code.C_126);
				}
				if(password.length()>=6 && password.length()<=10){
					user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
				}else{
					return R.To(false, null,Code.C_127);
				}
			}*/
			if(!user.getMobile().equals(source.getMobile())){
				if(is!=null && is.size()>0){
					return R.To(false, null,Code.C_202);
				}
			}
			user.setUpdateTime(new Date());
			user.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
			user.setSysOrgId(dept==null?null:dept.getOrgId());
			user.setAccount(user.getMobile());
			sysUserMapper.updateByPrimaryKeySelective(user);
		}else{
			if(is!=null && is.size()>0){
				return R.To(false, null,Code.C_202);
			}
			/*String password = user.getPassword().trim();
			//账号字段暂时无用
			if(user.getPassword().equals("")){
				password = Constant.defult_password;
			}else{
				//校验密码规则
				boolean flag = CheckUtil.checkContainChinese(password);
				if(flag){
					return R.To(false, null,Code.C_126);
				}
				if(password.length()<6 || password.length()>10){
					return R.To(false, null,Code.C_127);
				}
			}
			user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));*/
			user.setCreateTime(new Date());
			user.setCreateUser(SystemManage.getInstance().getCurrentUserId());
			user.setUserType(Constant.user_type_1);
			user.setSysOrgId(dept==null?null:dept.getOrgId());
			user.setAccount(user.getMobile());
			sysUserMapper.insertSelective(user);
		}
		return R.To(true, null,Code.C_200);
	}
	public JSONObject addOrEditUser2(SysUser user) {
		//手机号是否存在
		List<SysUser> is = sysUserMapper.selectByMobile(user);
		SysDepartment dept = sysDepartmentMapper.selectByPrimaryKey(user.getSysDepId());
		if(user.getId()!=null){//修改
			SysUser source = sysUserMapper.selectByPrimaryKey(user.getId());
			/*//判断密码是否修改了
			String password = user.getPassword().trim();
			if(password.equals("")){
				user.setPassword(source.getPassword());
			}else{
				//校验密码规则
				boolean flag = CheckUtil.checkContainChinese(password);
				if(flag){
					return R.To(false, null,Code.C_126);
				}
				if(password.length()>=6 && password.length()<=10){
					user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
				}else{
					return R.To(false, null,Code.C_127);
				}
			}*/
			if(!user.getMobile().equals(source.getMobile())){
				if(is!=null && is.size()>0){
					return R.To(false, null,Code.C_202);
				}
			}
			user.setUpdateTime(new Date());
			user.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
			user.setSysOrgId(dept==null?null:dept.getOrgId());
			user.setAccount(user.getMobile());
			sysUserMapper.updateByPrimaryKeySelective(user);
		}else{
			if(is!=null && is.size()>0){
				return R.To(false, null,Code.C_202);
			}
			/*String password = user.getPassword().trim();
			//账号字段暂时无用
			if(user.getPassword().equals("")){
				password = Constant.defult_password;
			}else{
				//校验密码规则
				boolean flag = CheckUtil.checkContainChinese(password);
				if(flag){
					return R.To(false, null,Code.C_126);
				}
				if(password.length()<6 || password.length()>10){
					return R.To(false, null,Code.C_127);
				}
			}*/
			//user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
			user.setCreateTime(new Date());
			user.setCreateUser(SystemManage.getInstance().getCurrentUserId());
			user.setUserType(Constant.user_type_1);
			user.setSysOrgId(dept==null?null:dept.getOrgId());
			user.setAccount(user.getMobile());
			sysUserMapper.insertSelective(user);
		}
		// 关联管理员所管理的项目组
		List<Integer> groupIds = sysUserGroupRelMapper.getGroupIdsByManagerId(SystemManage.getInstance().getCurrentUserId());
		for(Integer groupId : groupIds){
			SysUserGroupRel ug = new SysUserGroupRel();
			ug.setUserId(user.getId());
			ug.setGroupId(groupId);
			ug.setUserType(0);
			ug.setSourceSign(1);
			ug.setCreateTime(new Date());
			ug.setUpdateTime(new Date());
			ug.setCreateUser(SystemManage.getInstance().getCurrentUserId());
			ug.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
			try {
				sysUserGroupRelMapper.insertSelective(ug);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return R.To(true, null,Code.C_200);
	}

	@Override
	@Transactional
	public JSONObject updateStatusById(SysUser user) {
		user.setUpdateTime(new Date());
		user.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
		sysUserMapper.updateByPrimaryKeySelective(user);
		return R.To(true, null,Code.C_200);
	}

	@Override
	public List<ComboboxGroup> deptList() {
		List<ComboboxGroup> list = new ArrayList<ComboboxGroup>();
		//用户是否是超级用户
		SysUser user = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
		user.setMobile(null);
		//查询组织
		List<SysOrganization> orgList = sysOrganizationMapper.selectAllOrById(user);
		for (int i = 0; i < orgList.size(); i++) {
			SysDepartment select = new SysDepartment();
			select.setOrgId(orgList.get(i).getId());
			//根据组织id查询部门
			List<SysDepartment> deptList = sysDepartmentMapper.selectDeptByOrgId(select);
			for (int j = 0; j < deptList.size(); j++) {
				ComboboxGroup comboboxgroup = new ComboboxGroup();
				comboboxgroup.setGroup(orgList.get(i).getName());
				comboboxgroup.setValue(deptList.get(j).getId());
				comboboxgroup.setText(deptList.get(j).getDepartmentName());
				list.add(comboboxgroup);
			}
		}
		return list;
	}

	@Override
	public List<Combobox> userLevel() {
		List<Combobox> list = new ArrayList<Combobox>();
		List<SysDict> listSysDict = sysDictMapper.getListByParentCode("user_level");
		if(listSysDict!=null && listSysDict.size()>0){
			for (int i = 0; i < listSysDict.size(); i++) {
				Combobox c = new Combobox();
				c.setId(listSysDict.get(i).getCode());
				c.setText(listSysDict.get(i).getShowName());
				list.add(c);
			}
		}
		return list;
	}

	@Override
	@Transactional
	public JSONObject havePurview(Integer userId, Integer type, String[] ids) {
		int currentUserId = SystemManage.getInstance().getCurrentUserId();
		try {
			if(type==Constant.user_type_1){//前端项目权限
				//查询
				List<SysUserProjectMenuRel> list = sysUserProjectMenuRelMapper.selectByUserId(userId);
				if(list!=null && list.size()>0){
					//删除
					sysUserProjectMenuRelMapper.deleteByUserIdAndManagerId(userId, currentUserId);
				}
				list.clear();
				if(!ids[0].equals("-1")){
					for (int i = 0; i < ids.length; i++) {
						String[] id = ids[i].split("\\|");
						SysUserProjectMenuRel s = new SysUserProjectMenuRel();
						s.setCreateTime(new Date());
						s.setCreateUser(currentUserId);
						s.setMenuId(Integer.valueOf(id[1]));
						s.setProjectId(Integer.valueOf(id[0]));
						s.setUserId(userId);
						list.add(s);
					}
					//添加
					sysUserProjectMenuRelMapper.betchAdd(list);
				}
			}else if(type==Constant.user_type_0){//后端项目权限
				List<SysUserMenuRel> list = sysUserMenuRelMapper.selectByUserId(userId);
				if(list!=null && list.size()>0){
					sysUserMenuRelMapper.deleteByUserIdAndManagerId(userId, currentUserId);
				}
				list.clear();
				if(!ids[0].equals("-1")){
					for (int i = 0; i < ids.length; i++) {
						String[] id = ids[i].split("\\|");
						SysUserMenuRel s = new SysUserMenuRel();
						s.setCreateTime(new Date());
						s.setCreateUser(currentUserId);
						s.setMenuId(Integer.valueOf(id[1]));
						s.setProjectId(Integer.valueOf(id[0]));
						s.setUserId(userId);
						list.add(s);
					}
					sysUserMenuRelMapper.betchAdd(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null,Code.C_500);
		}
		return R.To(true, null,Code.C_200);
	}

	@Override
	public SysUser getById(int id) {
		// TODO Auto-generated method stub
		return sysUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Map<String, Object>> getListByProAndKey(Integer proId, String key) {
		// TODO Auto-generated method stub
		return sysUserMapper.getListByProAndKey(proId, key);
	}

	@Override
	public JSONObject modifyPassword(String newPwd,String confirmPwd,String originalPwd) {
		try {
			SysUser update = null;
			SysUser source = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
			//校验原密码
			String password = originalPwd.trim();
			if(password!=null && !password.equals("")){
				if(source.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
					if(newPwd.trim().equals("") || confirmPwd.trim().equals("")){
						//密码不能为空
						return R.To(false, null,LanguageUtil.getMessage("MiMaBuNengWeiKong"));
					}
					//校验新密码两次输入是否一致
					if(confirmPwd.equals(newPwd)){
						//校验密码规则
						boolean flag = CheckUtil.checkContainChinese(newPwd);
						if(flag){
							return R.To(false, null,LanguageUtil.getMessage("Code126"));
						}
						if(newPwd.length()<6 || newPwd.length()>10){
							return R.To(false, null,LanguageUtil.getMessage("Code127"));
						}
						update = new SysUser();
						update.setPassword(DigestUtils.md5DigestAsHex(newPwd.getBytes()));
						update.setUpdateTime(new Date());
						update.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
						update.setId(SystemManage.getInstance().getCurrentUserId());
						sysUserMapper.updateByPrimaryKeySelective(update);
					}else{
						//新密码不一致
						return R.To(false, null,LanguageUtil.getMessage("Code122"));
					}
				}else{
					//原密码不一致
					return R.To(false, null,LanguageUtil.getMessage("Code123"));
				}
			}else{
				//密码不能为空
				return R.To(false, null,LanguageUtil.getMessage("MiMaBuNengWeiKong"));
			}
		} catch (Exception e) {
			return R.To(false, null,LanguageUtil.getMessage("Code500"));
		}
		return R.To(true, null,LanguageUtil.getMessage("Code200"));
	}

	@Transactional
	@Override
	public JSONObject saveUserGroupRel(int userId, int[] groupIds) {
		try {
			// 删除原有的项目组分配记录
			sysUserGroupRelMapper.deleteByUserId(userId);
			// 增加新的分配记录
			List<SysUserGroupRel> userGroupRels = new ArrayList<SysUserGroupRel>();
			Integer currentUserId = SystemManage.getInstance().getCurrentUserId();
			for(int groupId : groupIds){
				SysUserGroupRel ug = new SysUserGroupRel();
				ug.setUserId(userId);
				ug.setGroupId(groupId);
				ug.setUserType(1);
				ug.setSourceSign(0);
				ug.setCreateTime(new Date());
				ug.setUpdateTime(new Date());
				ug.setCreateUser(currentUserId);
				ug.setUpdateUser(currentUserId);
				userGroupRels.add(ug);
			}
			if(userGroupRels.size()>0)
				sysUserGroupRelMapper.insertBatch(userGroupRels);
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
		return R.To(true, null, Code.C_200);
	}
	
	@Transactional
	@Override
	public JSONObject saveUserMenuRel(int userId, int[] menuIds) {
		try {
			// 删除原有记录
			sysManagerMenuRelMapper.deleteByUserId(userId);
			// 增加新分配权限
			List<SysManagerMenuRel> managerMenuRels = new ArrayList<SysManagerMenuRel>();
			Integer currentUserId = SystemManage.getInstance().getCurrentUserId();
			for(int menuId : menuIds){
				SysManagerMenuRel mm = new SysManagerMenuRel();
				mm.setUserId(userId);
				mm.setMenuId(menuId);
				mm.setCreateTime(new Date());
				mm.setUpdateTime(new Date());
				mm.setCreateUser(currentUserId);
				mm.setUpdateUser(currentUserId);
				managerMenuRels.add(mm);
			}
			if(managerMenuRels.size()>0)
				sysManagerMenuRelMapper.insertBatch(managerMenuRels);
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
		return R.To(true, null, Code.C_200);
	}

	public JSONObject setPwd(Integer userId,String newPwd,String confirmPwd){
		if(StringUtils.isEmpty(newPwd)){
			return R.ToJson(false, null, LanguageUtil.getMessage("MiMaBuNengWeiKong"));
		}
		if(StringUtils.isEmpty(confirmPwd)){
			return R.ToJson(false, null, LanguageUtil.getMessage("QueRenMiMaBuNengWeiKong"));
		}
		if(!newPwd.equals(confirmPwd)){
			return R.ToJson(false, null, LanguageUtil.getMessage("LiangCiShuRuMiMaBuYiZhi"));
		}
		SysUser user=sysUserMapper.selectByPrimaryKey(userId);
		if(user==null){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		SysUser newUser=new SysUser();
		newUser.setId(user.getId());
		String strPwd=DigestUtils.md5DigestAsHex(newPwd.getBytes());
		newUser.setPassword(strPwd);
		sysUserMapper.updateByPrimaryKeySelective(newUser);
		if(SystemManage.getInstance().getSysUser()!=null)
			SystemManage.getInstance().getSysUser().setPassword(strPwd);
		return R.ToJson(true, null, LanguageUtil.getMessage("MiMaSheZhiChengGong"));
	}

	@Override
	public Map<Integer, String> getAllName() {
		// TODO Auto-generated method stub
		List<SysUser> listUser=sysUserMapper.getAll();
		Map<Integer, String> resultMap=new HashMap<Integer, String>();
		for (SysUser user : listUser) {
			resultMap.put(user.getId(), user.getSurNames()+user.getRealName()); 
		}
		return resultMap;
	}
}
