package com.bim.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.bim.dao.EasyCourseMapper;
import com.bim.entity.EasyClassification;
import com.bim.entity.EasyCourse;
import com.bim.util.Code;
import com.bim.util.PropertiesUtil;
import com.bim.util.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Controller
@RequestMapping("course")
public class EasyCourseController {
	
	@Autowired
	private EasyCourseMapper easyCourseMapper;

	@RequestMapping("init")
	public String inti() {
		return "admin/course";
	}
	
	@RequestMapping("list")
	@ResponseBody
	public JSONObject list(int page,int rows, int classId){
		Page<EasyCourse> pageObj=PageHelper.startPage(page, rows);
		List<EasyCourse> list= easyCourseMapper.selectByClassid(classId);
		return R.ToPage(true, list, Code.C_200, pageObj.getTotal());
	}
	
	@RequestMapping("add")
	@ResponseBody
	public JSONObject add(@RequestParam(value = "courseImgfile", required = false) MultipartFile file, EasyCourse course){
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
				course.setImage(requestPath+"/app/"+fileName);
			}
			int result = 0;
			if(null == course.getId()){
				course.setCreateTime(new Date());
				result = easyCourseMapper.insertSelective(course);
				
			}else{
				result = easyCourseMapper.updateByPrimaryKeySelective(course);
			}
			if(result>0)
				return R.To(true, null, Code.C_200);
			return R.To(false, null, Code.C_500);
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JSONObject delete(Integer id){
		int result = easyCourseMapper.deleteByPrimaryKey(id);
		if(result>0)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
}
