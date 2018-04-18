package com.bim.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.BaseClass;
import com.bim.dao.SysOrganizationMapper;
import com.bim.entity.Combobox;
import com.bim.entity.SysOrganization;
import com.bim.service.SysOrganizationService;
import com.bim.util.R;

@Controller
@RequestMapping("/org")
public class SysOrganizationController extends BaseClass{
	
	@Autowired
	private SysOrganizationService sysOrganizationService;
	@Autowired
	private SysOrganizationMapper sysOrganizationMapper;
	
	/**
	 * 页面初始化
	 */
    @RequestMapping("/init")
    public String init(){
		return "admin/org";
    }
	
    /**
     * 组织维护
     * @param sysOrganization
     * @param bindingResult
     * @param loginUserId
     * @return
     */
	@RequestMapping(value="/addOrEditOrg", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOrEditOrg(@Valid SysOrganization sysOrganization,BindingResult bindingResult){
		//参数校验
		if (bindingResult.hasErrors()) {
            return R.To(false, null, bindingResult.getFieldError().getDefaultMessage());
        }
		return sysOrganizationService.addOrEditOrg(sysOrganization);
	}
	
	/**
	 * 组织查询
	 * @param pageNumber
	 * @param pageSize
	 * @param loginUserId
	 * @param orgName
	 * @return
	 */
	@RequestMapping(value="/listOrg", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject listOrg(int page,int rows,String orgName){
		return sysOrganizationService.listOrg(page,rows,orgName);
	}
	
	/**
	 * 根据用户id查询所有组织
	 * @param loginUserId
	 * @return
	 */
	@RequestMapping(value="/listOrgByUserId", method = RequestMethod.POST)
	@ResponseBody
	public List<Combobox> listOrgByUserId(){
		return sysOrganizationService.listOrgByUserId();
	}
	
	/**
	 * 批量删除机构
	 * 删除规则,组织下有人 有项目则不允许删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteOrgs", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteOrgs(@RequestParam(value = "ids[]") String[] ids){
		return sysOrganizationService.deleteOrgs(ids);
	}
	
	@RequestMapping("select")
	@ResponseBody
	public List<SysOrganization> selectAllOrderByName(){
		return sysOrganizationMapper.selectAllOrderByName();
	}
	
	/**
	 * 根据用户id查询所有组织
	 * @param loginUserId
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public List<Combobox> list(){
		return sysOrganizationService.listOrg();
	}
}
