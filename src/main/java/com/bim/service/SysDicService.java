package com.bim.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.SysDict;

public interface SysDicService {
	JSONObject getListByPage(String searchKey,int pageNumber,int pageSize);
	
	JSONObject delete(int id);

	List<SysDict> getListByParentCode(@Param("parentCode") String parentCode);
	
	JSONObject addOrEdit(SysDict entity);
	
	/**
	 * 批量软删除
	 * @param ids id数组
	 * @return
	 */
	int batchDeleteByIds(int[] ids);
	
	/**
	 * 检查编码是否存在
	 * @param code
	 * @param excludeId 需排除的id
	 * @return false 不存在,true 存在
	 */
	boolean checkHasCode(String code,Integer excludeId);

	SysDict selectByCode(String code);
	
	/**
	 * 批量禁用/启用 
	 * @param ids id数组
	 * @param status 状态
	 * @return
	 */
	int batchEnableByIds(int[] ids,int status);
}
