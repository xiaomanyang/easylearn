package com.bim.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.SysDict;
import com.bim.service.SysDicService;
import com.bim.util.Code;
import com.bim.util.R;

@Controller
@RequestMapping("api/dict")
public class ApiDictController {
	@Autowired
	private SysDicService sysDicService;
	
	@RequestMapping(value="/getByCode",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getByCode(String code){
		if(code==null){
			return R.ToJson(false,null,Code.C_404);
		}
		SysDict sysdict= sysDicService.selectByCode(code);
		return R.ToJson(true,sysdict,"获取成功!");
	}
	
	@RequestMapping(value="/getListByParentCode",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject getListByParentCode(String parentCode){
		if(parentCode==null){
			return R.ToJson(false,null,Code.C_404);
		}
		List<SysDict> listCode= sysDicService.getListByParentCode(parentCode);
		return R.ToJson(true,listCode,"获取成功!");
	}
}
