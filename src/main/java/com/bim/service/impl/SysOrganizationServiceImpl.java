package com.bim.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.SysOrgProjectRelMapper;
import com.bim.dao.SysOrganizationMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.Combobox;
import com.bim.entity.SysOrgProjectRel;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysUser;
import com.bim.service.SysOrganizationService;
import com.bim.util.Code;
import com.bim.util.Constant;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

	@Autowired
	private SysOrganizationMapper sysOrganizationMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysOrgProjectRelMapper sysOrgProjectRelMapper;
	
	@Transactional
	@Override
	public JSONObject addOrEditOrg(SysOrganization sysOrganization) {
		sysOrganization.setName(sysOrganization.getName().trim());
		sysOrganization.setShortName(sysOrganization.getShortName().trim());
		if(sysOrganization.getId()!=null){
			SysOrganization source = sysOrganizationMapper.selectByPrimaryKey(sysOrganization.getId());
			if(!source.getName().equals(sysOrganization.getName())){
				SysOrganization name = new SysOrganization();
				name.setName(sysOrganization.getName());
				List<SysOrganization> resName = sysOrganizationMapper.selectByOrgNameOrShortName(name);
				if(resName.size()>0){
					return R.To(false, null,Code.C_110);
				}
			}
			if(!source.getShortName().equals(sysOrganization.getShortName())){
				SysOrganization shortName = new SysOrganization();
				shortName.setShortName(sysOrganization.getShortName());
				List<SysOrganization> resShortName = sysOrganizationMapper.selectByOrgNameOrShortName(shortName);
				if(resShortName.size()>0){
					return R.To(false, null,Code.C_111);
				}
			}
			sysOrganization.setUpdateTime(new Date());
			sysOrganization.setUpdateUser(SystemManage.getInstance().getCurrentUserId());
			sysOrganizationMapper.updateByPrimaryKeySelective(sysOrganization);
		}else{
			SysOrganization name = new SysOrganization();
			name.setName(sysOrganization.getName());
			List<SysOrganization> resName = sysOrganizationMapper.selectByOrgNameOrShortName(name);
			if(resName.size()>0){
				return R.To(false, null,Code.C_110);
			}
			SysOrganization shortName = new SysOrganization();
			shortName.setShortName(sysOrganization.getShortName());
			List<SysOrganization> resShortName = sysOrganizationMapper.selectByOrgNameOrShortName(shortName);
			if(resShortName.size()>0){
				return R.To(false, null,Code.C_111);
			}
			shortName.setShortName(sysOrganization.getShortName());
			sysOrganization.setStatus(Constant.user_type_0);//默认启用
			sysOrganization.setCreateTime(new Date());
			sysOrganization.setCreateUser(SystemManage.getInstance().getCurrentUserId());
			sysOrganizationMapper.insertSelective(sysOrganization);
		}
		return R.To(true, null,Code.C_200);
	}

	@Override
	public JSONObject listOrg(int page,int rows,String orgName) {
		SysUser user = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
		if(null==user){
			return R.To(false, null,Code.C_201);
		}
		PageHelper.startPage(page, rows);
		user.setMobile(orgName==null?null:orgName.trim());
		user.setUserType(0);
		List<SysOrganization> list = sysOrganizationMapper.selectAllOrById(user);
		return R.ToPage(true, list,Code.C_200,new PageInfo(list).getTotal());
	}

	@Override
	public List<Combobox> listOrgByUserId() {
		List<Combobox> res = new ArrayList<Combobox>();
		SysUser user = sysUserMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
		user.setMobile(null);
		List<SysOrganization> list = sysOrganizationMapper.selectAllOrById(user);
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Combobox c = new Combobox();
				c.setId(String.valueOf(list.get(i).getId()));
				c.setText(list.get(i).getName());
				res.add(c);
			}
		}
		return res;
	}

	@Override
	@Transactional
	public JSONObject deleteOrgs(String[] ids) {
		try {
			//是否有人
			List<SysUser> users = sysUserMapper.selectIsHaveInOrgIds(ids);
			if(users.size()>0){
				return R.To(false, null, Code.C_101);
			}
			//是否有项目
			List<SysOrgProjectRel> project = sysOrgProjectRelMapper.selectIsHaveInOrgIds(ids);
			if(project.size()>0){
				return R.To(false, null, Code.C_102);
			}
			//删除
			if(ids.length!=0){
				sysOrganizationMapper.betchDelete(ids);
			}
			return R.To(true, null, Code.C_200);
		} catch (Exception e) {
			return R.To(false, null, Code.C_500);
		}
	}

	@Override
	public List<Combobox> listOrg() {
		List<Combobox> res = new ArrayList<Combobox>();
		List<SysOrganization> list = sysOrganizationMapper.selectAll();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Combobox c = new Combobox();
				c.setId(String.valueOf(list.get(i).getId()));
				c.setText(list.get(i).getName());
				res.add(c);
			}
		}
		return res;
	}
	
}
