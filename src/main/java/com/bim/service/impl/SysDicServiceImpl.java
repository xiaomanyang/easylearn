package com.bim.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bim.dao.SysDictMapper;
import com.bim.entity.SysDict;
import com.bim.service.SysDicService;
import com.bim.util.Code;
import com.bim.util.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SysDicServiceImpl implements SysDicService{
	@Autowired
	private SysDictMapper sysDictMapper;

	@Override
	public JSONObject getListByPage(String searchKey,int pageNumber,int pageSize) {
    	Page<SysDict> page=PageHelper.startPage(pageNumber, pageSize);
    	List<SysDict> list=sysDictMapper.getListByKey(searchKey);
		// TODO Auto-generated method stub
		return R.ToPage(true, list, Code.C_200, page.getTotal());
	}

	@Override
	public JSONObject delete(int id) {
		// TODO Auto-generated method stub
		SysDict dictionary= sysDictMapper.selectByPrimaryKey(id);
		dictionary.setIsDelete(new Byte("1"));
		int i=sysDictMapper.updateByPrimaryKey(dictionary);// sysDictMapper.deleteByPrimaryKey(id);
		return R.To(i>0, null, "操作成功！"); 
	}
	
	@Override
	public List<SysDict> getListByParentCode(@Param("parentCode") String parentCode)
	{
		return sysDictMapper.getListByParentCode(parentCode);
	}
	
	
	@Override
	public JSONObject addOrEdit(SysDict entity) {
		int result=0;
		if(entity.getId()==null){
			if(checkHasCode(entity.getCode()))
				return R.To(false, null,"操作失败！编码["+entity.getCode()+"]已存在...");
			result= sysDictMapper.insertSelective(entity);
		}else{
			if(checkHasCode(entity.getCode(),entity.getId()))
				return R.To(false, null,"操作失败！编码["+entity.getCode()+"]已存在...");
			result=sysDictMapper.updateByPrimaryKeySelective(entity);
		}
		return result>0? R.To(true, null,"操作成功！"):R.To(true, null,"操作失败！");
	}

	@Override
	public int batchDeleteByIds(int[] ids) {
		return sysDictMapper.batchUpdateStatusByIds(1,ids);
	}

	@Override
	public boolean checkHasCode(String code,Integer excludeId) {
		SysDict dic=sysDictMapper.getByCode(code,excludeId);
		return dic!=null;
	}
	
	public boolean checkHasCode(String code) {
		return checkHasCode(code,null);
	}

	@Override
	public SysDict selectByCode(String code) {
		return sysDictMapper.getSingleByCode(code);
	}

	@Override
	public int batchEnableByIds(int[] ids, int status) {
		// TODO Auto-generated method stub
		return sysDictMapper.batchUpdateStatusByIds(status,ids);
	}

}

