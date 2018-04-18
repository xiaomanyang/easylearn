package com.bim.controller;

import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysProGroupMapper;
import com.bim.entity.BimProject;
import com.bim.entity.SysProGroup;
import com.bim.service.BimProjectGroupService;
import com.bim.util.Code;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("projectGroup")
public class BimProjectGroupController {
	private final Logger log=Logger.getLogger(BimProjectGroupController.class);
	
	@Autowired
	private SysProGroupMapper sysProGroupMapper;
	@Autowired
	private BimProjectMapper bimProjectMapper;
	@Autowired
	private BimProjectGroupService bimProjectGroupService;
	
	@RequestMapping("/init")
	public String init(){
		return "admin/projectgroup";
	}
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public JSONObject getListByPage(int page,int rows,String searchKey){
		PageHelper.startPage(page, rows);
		List<SysProGroup> list = sysProGroupMapper.listGroup(searchKey);
		return R.ToPage(true, list, Code.C_200, new PageInfo(list).getTotal());
	}
	
	/**
	 * 提交表单
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public JSONObject submitForm(SysProGroup sysProGroup){
		Integer userId = SystemManage.getInstance().getCurrentUserId();
		sysProGroup.setUpdateTime(new Date());
		sysProGroup.setUpdateUser(userId);
		try {
			if(null == sysProGroup.getId()){
				sysProGroup.setCreateTime(new Date());
				sysProGroup.setCreateUser(userId);
				sysProGroupMapper.insertSelective(sysProGroup);
			}else{
				sysProGroupMapper.updateByPrimaryKeySelective(sysProGroup);
			}
		} 
		catch (DataIntegrityViolationException e){
			return R.To(false, null, "项目组名称重复");
		}
		catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
		return R.To(true, null, "操作成功！");
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(int id){
		int result = sysProGroupMapper.deleteByPrimaryKey(id);
		if(0 < result)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
	
	/**
	 * TODO 查询项目组分配的项目情况。
	 * @param groupId
	 * @return 所有项目，分配的项目group_id不为null
	 * List<BimProject>
	 * date:2017年11月13日
	 * user:BIM-10
	 */
	@RequestMapping("groupProject")
	@ResponseBody
	public List<BimProject> groupProject(Integer groupId){
		return bimProjectMapper.getProjectOfGroupRel(groupId);
	}
	
	/**
	 * TODO 保存项目组分配的项目数据
	 * @param projectIds 项目id数组
	 * @param groupId 项目组id
	 * @return
	 * JSONObject
	 * date:2017年11月14日
	 * user:BIM-10
	 */
	@RequestMapping("saveGroupProject")
	@ResponseBody
	public JSONObject saveGroupProjectRel(int[] projectIds,int groupId){
		return bimProjectGroupService.saveProjectGroupRel(projectIds, groupId);
	}
	
}
