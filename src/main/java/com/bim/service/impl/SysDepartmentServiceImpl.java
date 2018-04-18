package com.bim.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.SysDepartmentMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.SysDepartment;
import com.bim.entity.SysUser;
import com.bim.service.SysDepartmentService;
import com.bim.util.Code;
import com.bim.util.Constant;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {

	@Autowired
	private SysDepartmentMapper sysDepartmentMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Transactional
	@Override
	public JSONObject addOrEditDept(SysDepartment sysDepartment) {
		sysDepartment.setDepartmentName(sysDepartment.getDepartmentName().trim());
		if(sysDepartment.getId()!=null){
			SysDepartment res = sysDepartmentMapper.selectByPrimaryKey(sysDepartment.getId());
			if(!res.getDepartmentName().equals(sysDepartment.getDepartmentName())){
				List<SysDepartment> deptName = sysDepartmentMapper.selectByDeptName(sysDepartment.getDepartmentName(),sysDepartment.getOrgId());
				if(deptName.size()>0){
					return R.To(false, null,Code.C_112);
				}
			}
			sysDepartment.setUpdateTime(new Date());
			sysDepartment.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
			sysDepartmentMapper.updateByPrimaryKeySelective(sysDepartment);
		}else{
			List<SysDepartment> deptName = sysDepartmentMapper.selectByDeptName(sysDepartment.getDepartmentName(),sysDepartment.getOrgId());
			if(deptName.size()>0){
				return R.To(false, null,Code.C_112);
			}
			sysDepartment.setStatus(Constant.user_type_0);//默认启用
			sysDepartment.setCreateTime(new Date());
			sysDepartment.setCreateUser(SystemManage.getInstance().getCurrentUserId());
			sysDepartmentMapper.insertSelective(sysDepartment);
		}
		return R.To(true, null,Code.C_200);
	}

	@Override
	public JSONObject listDept(int page, int rows,String deptName,Integer orgId) {
		PageHelper.startPage(page,rows);
		SysDepartment select = new SysDepartment();
		select.setOrgId(orgId);
		select.setDepartmentName(deptName==null?null:deptName.trim());
		List<SysDepartment> list = sysDepartmentMapper.selectDeptByOrgId(select);
		return R.ToPage(true, list,Code.C_200,new PageInfo(list).getTotal());
	}

	@Override
	public JSONObject deleteDepts(String[] ids) {
		try {
			//是否有人
			List<SysUser> users = sysUserMapper.selectIsHaveInDeptIds(ids);
			if(users.size()>0){
				return R.To(false, null, Code.C_103);
			}
			//删除
			if(ids.length!=0){
				sysDepartmentMapper.betchDelete(ids);
			}
			return R.To(true, null, Code.C_200);
		} catch (Exception e) {
			return R.To(false, null, Code.C_500);
		}
	}

}
