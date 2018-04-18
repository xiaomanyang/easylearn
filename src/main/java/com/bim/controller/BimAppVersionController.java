package com.bim.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.BaseClass;
import com.bim.dao.BimAppVersionMapper;
import com.bim.entity.BimAppVersion;
import com.bim.util.Code;
import com.bim.util.PropertiesUtil;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("app")
public class BimAppVersionController extends BaseClass {

	@Autowired
	private BimAppVersionMapper bimAppVersionMapper;
	
	/**
	 * TODO 颜色页面初始化
	 * 
	 * @return String date:2017年7月26日 user:BIM-10
	 */
	@RequestMapping("init")
	public String inti() {
		return "admin/appversion";
	}

	@RequestMapping("save")
	@ResponseBody
	public JSONObject addOrUpdate(@RequestParam(value = "file", required = false) MultipartFile file, BimAppVersion bimAppVersion) {
		try {
			if(file.getSize() > 0){
				String fileBase = PropertiesUtil.getValue("resource");
				String requestPath = PropertiesUtil.getValue("requestPath");
				String fileName = file.getOriginalFilename();
				File targetFile = new File(fileBase+"/app", fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				file.transferTo(targetFile);
				bimAppVersion.setAndroidAddress(requestPath+"/app/"+fileName);
			}
			/*if(bimAppVersion.getCurrentVersion()){
				// UPDATE bim_app_version SET current_version = 0 
				bimAppVersionMapper.updateSetCurrent0();
			}*/
			if(null == bimAppVersion.getId())
				bimAppVersionMapper.insertSelective(bimAppVersion);
			else
				bimAppVersionMapper.updateByPrimaryKeySelective(bimAppVersion);
			return R.To(true, null,Code.C_200);
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
		
	}
	
	@RequestMapping("list")
	@ResponseBody
	public JSONObject colorList(int page, int rows){
		PageHelper.startPage(page, rows);
		List<BimAppVersion> list = bimAppVersionMapper.listAppVersion();
		return R.ToPage(true, list, Code.C_200, new PageInfo(list).getTotal());
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JSONObject delete(Integer id){
		int result = bimAppVersionMapper.deleteByPrimaryKey(id);
		if(0 < result)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
	
}
