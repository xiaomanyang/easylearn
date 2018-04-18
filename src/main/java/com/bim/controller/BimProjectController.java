package com.bim.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.entity.BimProject;
import com.bim.service.BimProjectService;
import com.bim.util.Constant;
import com.bim.util.FileUtil;
import com.bim.util.PropertiesUtil;
import com.bim.util.R;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("project")
public class BimProjectController {
	private final Logger log=Logger.getLogger(BimProjectController.class);
	
	@Autowired
	private BimProjectService bimProjectService;
	
	@RequestMapping("/init")
	public String init(){
		return "admin/project";
	}
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value="/getListByPage")
	@ResponseBody
	public JSONObject getListByPage(int page,int rows,String searchKey){
		return bimProjectService.getListByPage(page, rows, searchKey);
	}
	
	/**
	 * 提交表单
	 * @return
	 */
	@RequestMapping(value="/submitForm",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject submitForm(@RequestParam(value = "proImgfile", required = false) MultipartFile file, BimProject entity){
		if(StringUtils.isEmpty(entity.getProCode()))
			return R.To(false, null, "操作失败！项目编码不能为空...");
		BimProject p= bimProjectService.getByProCode(entity.getProCode().trim());
		if(p!=null && !p.getId().equals(entity.getId())){
			return R.To(false, null, String.format("操作失败！项目编码【%s】已存在", entity.getProCode()));
		}
		int result = 0;
		try {
			System.out.println("proId:"+entity.getId());
			if(file!=null && FileUtil.isImage(file)){
				String orgFileName =file.getOriginalFilename().replaceAll(" ", "-");
				if(!"".equals(orgFileName)){
					String path=Constant.PROJECT_PRICTURE+"/"+orgFileName;
					log.info("imgUrl:"+path);
					/*File f=new File(PropertiesUtil.getValue("resource")+path);
					if(!f.exists()){ f.mkdirs(); }*/
					/*file.transferTo(f);*/
					Thumbnails.of(file.getInputStream()).size(408, 314).toFile(PropertiesUtil.getValue("resource")+path);
					entity.setImgUrl(path);
				}
			}
			result = bimProjectService.addOrEdit(entity);
		} catch (DataIntegrityViolationException e){
			log.error(e);
			return R.To(false, null, "项目编码重复");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			return R.To(false, null, "图片保存失败！");
		}
		return result>0?R.To(true, null, "操作成功！"):R.To(true, null, "操作失败！");
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(int id){
		return bimProjectService.delete(id);
	}
	
	/**
	 * 启用/禁用
	 * @return
	 */
	@RequestMapping(value="/enabled",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject enabled(int id,int status){
		return bimProjectService.enabled(id, status);
	}
	
	/**
	 * 根据用户id查询项目
	 * @return
	 */
	@RequestMapping(value="/getListByUserId",method=RequestMethod.POST)
	@ResponseBody
	public List<BimProject> getListByUserId(int userId){
		return bimProjectService.getListByUserId(userId);
	}
	
	/**
	 * TODO 查询登录用户的项目列表
	 * @return
	 * List<BimProject>
	 * date:2017年8月7日
	 * user:BIM-10
	 */
	@RequestMapping("getUserProjectList")
	@ResponseBody
	public List<BimProject> getUserProjectList(){
		return bimProjectService.getListByUserId(SystemManage.getInstance().getCurrentUserId());
	}
	
}
