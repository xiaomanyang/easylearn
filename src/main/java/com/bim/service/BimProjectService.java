package com.bim.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.BimProject;
import com.bim.entity.CheckBoxTree;

public interface BimProjectService {
	/**
	 * 首页分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param searchKey
	 * @return
	 */
	JSONObject getListByPage(int pageNumber,int pageSize,String searchKey);
	
	/**
	 * 提交表单（增加或修改）
	 * @param entity
	 * @return
	 */
	int addOrEdit(BimProject entity);
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	JSONObject delete(int id);
	
	/**
	 * 根据user查所在组织的项目
	 * @param id
	 * @return
	 */
	List<BimProject> getListByUserId(int userId);
	
	/**
	 * 根据userid查引用的项目
	 * @param id
	 * @return
	 */
	List<BimProject> getListByUserRel(int userId);
	
	List<CheckBoxTree> getOrgProjectRelTree(int proId);
	
	int saveOrgProRel(int[] orgIds,int proId);
	
	/**
	 * 根据项目编码获取项目
	 * @param proCode
	 * @return
	 */
	BimProject getByProCode(String proCode);
	
	/**
	 * 启用/禁用
	 * @param id
	 * @param status 0_启用/1_禁用
	 * @return
	 */
	JSONObject enabled(int id,int status);
	
	List<BimProject> getAllList();
}
