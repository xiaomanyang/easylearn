package com.bim.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysOrgProjectRelMapper;
import com.bim.dao.SysOrganizationMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.BimProject;
import com.bim.entity.CheckBoxTree;
import com.bim.entity.SysDict;
import com.bim.entity.SysOrgProjectRel;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysUser;
import com.bim.service.BimProjectService;
import com.bim.service.SysDicService;
import com.bim.util.Code;
import com.bim.util.PropertiesUtil;
import com.bim.util.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import io.jsonwebtoken.lang.Collections;

@Service
public class BimProjectServiceImpl implements BimProjectService {
	
	@Autowired
	private BimProjectMapper bimProjectMapper;
	
	@Autowired
	private SysOrganizationMapper organizationMapper;
	
	@Autowired
	private SysOrgProjectRelMapper orgProjectRelMapper;
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private SysDicService dicService;
	
	@Override
	public JSONObject getListByPage(int pageNumber, int pageSize, String searchKey) {
		ThreadLocal<String> s=new ThreadLocal<>();
		SysUser user=userMapper.selectByPrimaryKey(SystemManage.getInstance().getCurrentUserId());
		Integer orgId=user.getUserType()==0?null:user.getSysOrgId();
		if(!StringUtils.isEmpty(searchKey)){
			searchKey=searchKey.trim();
		}
		Page<BimProject> page=PageHelper.startPage(pageNumber, pageSize);
		List<BimProject> list=bimProjectMapper.getList(orgId,searchKey);
		List<SysDict> dicList=dicService.getListByParentCode("pro_region");
		if(!Collections.isEmpty(list) && !Collections.isEmpty(dicList)){
			list.forEach(p->{
				for (SysDict sysDict : dicList) {
					if(sysDict.getCode().equals(p.getRegion())){
						p.setRegion(sysDict.getShowName());
						break;
					}
				}
			});
		}
		return R.ToPage(true, list, Code.C_200, page.getTotal());
	}

	@Transactional
	@Override
	public int addOrEdit(BimProject entity) {
		SysUser user=SystemManage.getInstance().getSysUser();
		int result=0;
		if(entity.getId()==null){
			entity.setCreateUser(user.getId());
			entity.setCreateTime(new Date());
			result=bimProjectMapper.insertSelective(entity);
			
			//给项目加上默认组织机构
			/*SysOrgProjectRel orgProjectRel=new SysOrgProjectRel();
			orgProjectRel.setProId(entity.getId());
			orgProjectRel.setProName(entity.getProName());
			orgProjectRel.setOrgId(user.getSysOrgId());
			orgProjectRelMapper.insert(orgProjectRel);*/
			//创建整体节点 
			//bimBuildingService.addWholeNode(entity.getId());
		}else{
			entity.setUpdateUser(-1);
			entity.setUpdateTime(new Date());
			result=bimProjectMapper.updateByPrimaryKeySelective(entity);
		}
		return result;
	}

	@Override
	public JSONObject delete(int id) {
		int i= 0;
		BimProject p=bimProjectMapper.selectByPrimaryKey(id);
		if(p==null){
			return R.To(false, null, "操作失败!当前记录不存在...");
		}
		if(!StringUtils.isEmpty(p.getImgUrl())){
			String fileFullName= PropertiesUtil.getValue("resource")+p.getImgUrl();
			File file=new File(fileFullName);
			if(file.exists()){
				file.delete();
			}
		}
		i= bimProjectMapper.deleteByPrimaryKey(id);
		return i>0?R.To(true, null, "操作成功！"):R.To(false, null, "操作失败！");
	}
	
	//@Override
	public JSONObject enabled(int id,int status) {
		int i= 0;
		BimProject p=bimProjectMapper.selectByPrimaryKey(id);
		if(p==null){
			return R.To(false, null, "操作失败!当前记录不存在...");
		}
		BimProject newProject= new BimProject();
		newProject.setId(p.getId());
		newProject.setStatus(new Byte(String.valueOf(status)));
		i= bimProjectMapper.updateByPrimaryKeySelective(newProject);//.deleteByPrimaryKey(id);
		return i>0?R.To(true, null, "操作成功！"):R.To(false, null, "操作失败！");
	}

	@Override
	public List<BimProject> getListByUserId(int userId) {
		// TODO Auto-generated method stub
		if(1 == userId)
			return bimProjectMapper.getAllProject();
		SysUser user=userMapper.selectByPrimaryKey(userId);
		return bimProjectMapper.getListByOrgId(user.getSysOrgId());
	}
	
	@Override
	public List<BimProject> getListByUserRel(int userId) {
		// TODO Auto-generated method stub
		/*if(1 == userId)
			return bimProjectMapper.getAllProject();*/
		return bimProjectMapper.getListByUserId(userId);
	}

	@Override
	public List<CheckBoxTree> getOrgProjectRelTree(int proId) {
		SysUser user=new SysUser();
		user.setId(21);
		user.setUserType(0);
		List<SysOrganization> orgList= organizationMapper.selectAllOrById(user);
		List<SysOrgProjectRel> relList=orgProjectRelMapper.selectByProId(proId);
		List<CheckBoxTree> resultList=new ArrayList<CheckBoxTree>();
		for(int i=0;i<orgList.size();i++)
		{
			SysOrganization org=orgList.get(i);
			CheckBoxTree cbm=new CheckBoxTree();
			cbm.setId(String.valueOf(org.getId()));
			cbm.setText(org.getName());
			boolean checked= relList.stream().anyMatch(p->p.getOrgId().equals(org.getId()));
			cbm.setChecked(checked);
			resultList.add(cbm);
		}
		return resultList;
	}

	@Transactional
	@Override
	public int saveOrgProRel(int[] orgIds, int proId) {
		// 刪除已有的关联
		orgProjectRelMapper.deleteByProId(proId);
		if(orgIds==null || orgIds.length<=0)
			return 1;
		List<SysOrgProjectRel> relList=new ArrayList<SysOrgProjectRel>();
		for (int i = 0; i < orgIds.length; i++) {
			SysOrgProjectRel rel=new SysOrgProjectRel();
			rel.setProId(proId);
			rel.setOrgId(orgIds[i]);
			rel.setUpdateTime(new Date());
			rel.setUpdateUser(-1);
			relList.add(rel);
		}
		//添加新关联
		orgProjectRelMapper.betchAdd(relList);
		return 1;
	}

	@Override
	public BimProject getByProCode(String proCode) {
		return bimProjectMapper.getByProCode(proCode);
	}
	
	/**
	 * 获取所有项目
	 * @return
	 */
	public List<BimProject> getAllList(){
		return bimProjectMapper.getList(0, null);
	}
}
