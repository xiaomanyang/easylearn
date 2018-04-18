package com.bim.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.BaseClass;
import com.bim.base.SystemManage;
import com.bim.dao.SysServerMapper;
import com.bim.entity.BimProject;
import com.bim.entity.SysDict;
import com.bim.entity.SysServer;
import com.bim.service.BimProjectService;
import com.bim.service.SysDicService;
import com.bim.util.Code;
import com.bim.util.LanguageUtil;
import com.bim.util.PropertiesUtil;
import com.bim.util.R;

/**
 * 
 * @author wangc
 * @date 2017年11月08日
 * @description 项目api
 */
@Controller
@RequestMapping("api/project")
public class ApiProjectController extends BaseClass {
	
	@Autowired
	private BimProjectService projectService;
	
	@Autowired
	private SysDicService dicService;
	
	@Autowired
	private SysServerMapper serverMapper;
	
	/**
	 * 项目列表接口
	 * @return
	*/
	@RequestMapping(value="/getAllList",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getAllList(HttpServletRequest request){
		boolean isEn=Locale.US.equals(LanguageUtil.getLocale());
		this.info("获取项目列表： ip->"+request.getRemoteAddr());
		List<BimProject> list= projectService.getAllList();
		List<Map<String, Object>> resultMap=new ArrayList<>();
		for (BimProject bimProject : list) {
			Map<String, Object> proMap=new HashMap<>();
			proMap.put("id", bimProject.getId());
			proMap.put("proName", isEn?bimProject.getProNameEn():bimProject.getProName());
			resultMap.add(proMap);
		}
		return R.ToJson(true, resultMap, Code.C_200);
	}
	
	/**
	 * 项目列表接口
	 * @return
	 */
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getList(HttpServletRequest request){
		boolean isEn=Locale.US.equals(LanguageUtil.getLocale()); 
		int userId=SystemManage.getInstance().getCurrentUserId();
		//String path=request.getRequestURL().toString().replace(request.getRequestURI(), "/resource/");
		String path=PropertiesUtil.getValue("requestPath");
		String proServer=PropertiesUtil.getValue("debugProjectServer");
		List<SysDict> dicRegion= dicService.getListByParentCode("pro_region");
		List<BimProject> projects= projectService.getListByUserRel(userId);
		List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
		for (BimProject item : projects) {
			Map<String, Object> entityMap=new HashMap<String,Object>();
			entityMap.put("id", item.getId());
			entityMap.put("proCode", item.getProCode());
			entityMap.put("proName", isEn?item.getProNameEn():item.getProName());
			//entityMap.put("proNumber", item.getProNumber());
			for(SysDict dic : dicRegion){
				if(dic.getCode().equals(item.getRegion())){
					entityMap.put("region", dic.getShowName());
					break;
				}
			}
			entityMap.put("address", item.getAddress());
			entityMap.put("imgUrl",StringUtils.isEmpty(item.getImgUrl())?"": path+item.getImgUrl());
			if(StringUtils.isEmpty(proServer)){
				SysServer server=serverMapper.selectByPrimaryKey(item.getServerId());
				entityMap.put("serverUrl", server == null ? "" : server.getAddress());
			}else{
				entityMap.put("serverUrl", proServer);
			}
			data.add(entityMap);
		}
		return R.ToJson(true, data, LanguageUtil.getMessage("Code200"));
	}

}
