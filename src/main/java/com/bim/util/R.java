package com.bim.util;

import com.alibaba.fastjson.JSONObject;
/**
 * 统一返回对象
 *@Description: 
 *@date 2017年7月20日
 *@version V1.0
 *@author yang.y
 */
public class R {
	
	public static JSONObject To(boolean success,Object data,String Code) {
		JSONObject js = new JSONObject();
		js.put("req", success);
		js.put("rows", data);//分页数据
		js.put("msg", Code);
		return js;
	}
	//分页使用
	public static JSONObject ToPage(boolean success,Object data,String Code,Long total) {
		JSONObject js = new JSONObject();
		js.put("req", success);
		js.put("rows", data);//分页数据
		js.put("msg", Code);
		js.put("total", total);//总条数
		return js;
	}
	
	public static JSONObject ToJson(boolean success,Object data,String message) {
		JSONObject js = new JSONObject();
		js.put("success", success);
		js.put("data", data);
		js.put("msg", message);
		return js;
	}
}
