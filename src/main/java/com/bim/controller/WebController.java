package com.bim.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bim.base.BaseClass;
import com.bim.base.CurrentUser;
import com.bim.base.SystemManage;
import com.bim.util.Constant;
import com.bim.util.LanguageUtil;
import com.bim.util.PropertiesUtil;

import io.jsonwebtoken.lang.Collections;

@Controller
public class WebController extends BaseClass{
	
	
	
	@RequestMapping(value = "/web/{file}.do")
	public ModelAndView web1(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable("file") String file) throws Exception {
		return web(req,resp, null, file);
	}
	
	
	@RequestMapping(value = "/web/{dir}/{file}.do")
	public ModelAndView web2(HttpServletRequest req, HttpServletResponse resp, 
			@PathVariable("dir") String dir, @PathVariable("file") String file) throws Exception {  
	       return web(req,resp, dir, file);
	}
	
	private ModelAndView web(HttpServletRequest req, HttpServletResponse resp, String dir, String file){
		ModelAndView mv = new ModelAndView();  
		
		dir = dir == null?"":dir.trim();
		file = file == null?"":file.trim();
		
		if(!dir.isEmpty() && !file.isEmpty()){
			mv.setViewName("web/"+dir+"/"+file);
		}
		else if(dir.isEmpty() && !file.isEmpty()){
			mv.setViewName("web/"+file);
		}
		
		Map upMap = new HashMap();
		Map map = req.getParameterMap();  
        Set<String> keySet = map.keySet();  
        for (String key : keySet) {  
           String[] values = (String[]) map.get(key);
           String val = "";
           for(int i = 0; i < values.length; i++){
        	   if(values[i] != null){
	        	   val = values[i].trim();
	        	   if(i < values.length -1){
	        		   val += ",";
	        	   }
        	   }
           }
           upMap.put(key, val);
        }  
        
        mv.addObject("hvServer", PropertiesUtil.getValue("hvServer"));
        
        mv.addObject("basePath", req.getContextPath());
        mv.addObject("lang",LanguageUtil.getLocale().toString());
		mv.addObject("upMap", upMap);
		CurrentUser user=SystemManage.getInstance().getCurrentUser();
        mv.addObject("cUser", user.getUser());
        mv.addObject("token",user.getToken());
        List<String> roles=user.getRoles();
        mv.addObject("roles", roles);
        //mv.addObject("isProdataManager", roles.contains(Constant.SYSTEM_ROLE_2));
        mv.addObject("isManager", !Collections.isEmpty(roles));
        if(!Collections.isEmpty(roles)){
        	mv.addObject("managerUrl", (roles.size()>1 || roles.contains(Constant.SYSTEM_ROLE_2))?"/prolist.do":"/home.do?currRole="+roles.get(0));
        }
        if("index".equals(file)){
        	//判断是否是首次登录
            mv.addObject("isFirstLogin",StringUtils.isEmpty(user.getUser().getPassword()));
        }
	    return mv;  
	}
}
