package com.bim.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.BaseClass;
import com.bim.base.SystemManage;
import com.bim.dao.SysServerMapper;
import com.bim.entity.SysServer;
import com.bim.util.Code;
import com.bim.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("server")
public class SysServerController extends BaseClass {

	@Autowired
	private SysServerMapper sysServerMapper;
	
	/**
	 * TODO 颜色页面初始化
	 * 
	 * @return String date:2017年7月26日 user:BIM-10
	 */
	@RequestMapping("init")
	public String inti() {
		return "admin/server";
	}

	@RequestMapping("save")
	@ResponseBody
	public JSONObject addOrUpdate(SysServer server) {
		Integer userId = SystemManage.getInstance().getCurrentUserId();
		try {
			server.setUpdateTime(new Date());
			server.setUpdateUser(userId);
			if(null == server.getId()){
				server.setCreateTime(new Date());
				server.setCreateUser(userId);
				sysServerMapper.insertSelective(server);
			}
			else{
				sysServerMapper.updateByPrimaryKeySelective(server);
			}
			return R.To(true, null,Code.C_200);
		}
		catch (DataIntegrityViolationException e){
			return R.To(false, null, "服务器名称重复");
		}
		catch (Exception e) {
			e.printStackTrace();
			if(e.getCause().toString().indexOf("MySQLIntegrityConstraintViolationException")>0){
				return R.To(false, null, "服务器名称重复");
			}
			return R.To(false, null, Code.C_500);
		}
		
	}
	
	@RequestMapping("list")
	@ResponseBody
	public JSONObject serverList(int page, int rows){
		PageHelper.startPage(page, rows);
//		List<BimAppVersion> list = bimAppVersionMapper.listAppVersion();
		List<SysServer> list = sysServerMapper.listServer();
		return R.ToPage(true, list, Code.C_200, new PageInfo(list).getTotal());
	}
	
	@RequestMapping("listAll")
	@ResponseBody
	public List<SysServer> listAll(){
		return sysServerMapper.listServer();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public JSONObject delete(Integer id){
		int result = 0;
		try {
			result = sysServerMapper.deleteByPrimaryKey(id);
		} catch (DataIntegrityViolationException  e) {
			return R.To(false, null, "不允许删除，请先取消项目关联");
		}
		if(0 < result)
			return R.To(true, null, Code.C_200);
		return R.To(false, null, Code.C_500);
	}
	
}
