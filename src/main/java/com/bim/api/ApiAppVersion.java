package com.bim.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.dao.BimAppVersionMapper;
import com.bim.entity.BimAppVersion;
import com.bim.util.Code;
import com.bim.util.LanguageUtil;
import com.bim.util.R;

@Controller
@RequestMapping("api/appversion")
public class ApiAppVersion {

	@Autowired
	private BimAppVersionMapper bimAppVersion;
	
	@ResponseBody
	@RequestMapping("current")
	public JSONObject getCurrentVersion(){
		BimAppVersion appVersion = bimAppVersion.selectCurrentVersion();
		return R.ToJson(true, appVersion, LanguageUtil.getMessage("Code200"));
	}
}
