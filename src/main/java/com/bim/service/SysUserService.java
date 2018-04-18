package com.bim.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.CheckBoxTree;
import com.bim.entity.Combobox;
import com.bim.entity.ComboboxGroup;
import com.bim.entity.SysUser;

public interface SysUserService {

	JSONObject addOrEditUser(SysUser user);
	JSONObject addOrEditUser2(SysUser user);

	List<ComboboxGroup> deptList();

	List<Combobox> userLevel();

	JSONObject updateStatusById(SysUser user);

	List<CheckBoxTree> listTree(Integer userId, Integer type);

	JSONObject havePurview(Integer userId, Integer type, String[] ids);

	JSONObject listUser(int page, int rows, String account);
	
	SysUser getById(int id);

	List<Map<String,Object>> getListByProAndKey(@Param("proId") Integer proId,@Param("key") String key);

	JSONObject modifyPassword(String newPwd, String confirmPwd, String originalPwd);
	
	JSONObject saveUserGroupRel(int userId, int[] groupIds);
	
	JSONObject saveUserMenuRel(int userId, int[] menuIds);
	
	JSONObject setPwd(Integer userId,String newPwd,String confirmPwd);
	
	Map<Integer, String> getAllName();
}
