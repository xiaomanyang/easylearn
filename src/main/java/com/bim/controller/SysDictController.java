package com.bim.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.SysDict;
import com.bim.service.SysDicService;
import com.bim.util.Code;
import com.bim.util.R;

/**
 * 
 * @author wang.c
 * @date 2017年7月20日
 */
@Controller
@RequestMapping("dictionary")
public class SysDictController {
	@Autowired
	private SysDicService sysDicService;
	
	/**
	 * 字典管理首页
	 */
    @RequestMapping("/init")
    public String init(){
		return "admin/dictionary";
    }
    
    /**
	 * 字典管理列表
	 */
    @RequestMapping("/getListByPage")
    @ResponseBody
    public JSONObject getListByPage(int page,int rows,String searchKey){
    	if(!StringUtils.isEmpty(searchKey)){
    		searchKey=searchKey.trim();
    	}
    	return sysDicService.getListByPage(searchKey, page, rows);
    }
    
    /**
	 * 提交
	 */
    @RequestMapping(value="/submitForm",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject submitForm(@Valid SysDict entity,BindingResult bindingResult){
    	if (bindingResult.hasErrors()) {
    		return R.To(false, null, bindingResult.getFieldError().getDefaultMessage());
    	}
    	return sysDicService.addOrEdit(entity);
    }
    
    /**
	 * 删除字典
	 */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(int id){
    	return sysDicService.delete(id);
    }
    
    
    /**
	 * 根据上级编码获取字典选项列表
	 */
    @RequestMapping(value="/getListByParentCode",method=RequestMethod.POST)
    @ResponseBody
    public List<SysDict> getListByParentCode(String parentCode){
    	return sysDicService.getListByParentCode(parentCode);
    }
    
    /**
	 * 批量删除(软)
	 */
    @RequestMapping(value="/batchDeleteByIds",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject batchDeleteByIds(int[] ids){
    	if(ids==null || ids.length<=0)
    		return R.To(false, null, "操作失败！请选择删除的记录...");
    	int i= sysDicService.batchDeleteByIds(ids);
    	return i>0?R.To(true, null, "操作成功！已成功删除"+i+"条记录..."):R.To(true, null, "操作失败！");
    }
    
    /**
	 * 批量禁用/启用
	 */
    @RequestMapping(value="/batchEnableByIds",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject batchEnableByIds(int[] ids,int isEnable){
    	if(ids==null || ids.length<=0)
    		return R.To(false, null, "操作失败！请选择操作的记录...");
    	if(isEnable!=0 && isEnable!=1)
    		return R.To(false, null, "操作失败！请确定是禁用还是启用...");
    	int i= sysDicService.batchEnableByIds(ids, isEnable);
    	return i>0?R.To(true, null, "操作成功！已成功"+(isEnable==0?"启用":"禁用")+i+"条记录..."):R.To(true, null, "操作失败！");
    }
}
