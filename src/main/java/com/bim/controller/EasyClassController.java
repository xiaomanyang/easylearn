package com.bim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.dao.EasyClassificationMapper;
import com.bim.entity.EasyClassification;
import com.bim.util.Code;
import com.bim.util.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Controller
@RequestMapping("classification")
public class EasyClassController {
	
	@Autowired
	private EasyClassificationMapper easyClassificationMapper;

	@RequestMapping("init")
	public String inti() {
		return "admin/classification";
	}
	
	@RequestMapping("list")
	@ResponseBody
	public JSONObject list(int page,int rows){
		Page<EasyClassification> pageObj=PageHelper.startPage(page, rows);
		List<EasyClassification> list= easyClassificationMapper.selectAll();
		return R.ToPage(true, list, Code.C_200, pageObj.getTotal());
	}
	
	@RequestMapping("add")
	@ResponseBody
	public JSONObject add(EasyClassification clsss){
		int result = easyClassificationMapper.insertSelective(clsss);
		if(result>0)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JSONObject delete(int id){
		int result = easyClassificationMapper.deleteByPrimaryKey(id);
		if(result>0)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
}
