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
import com.bim.dao.SysDepartmentMapper;
import com.bim.entity.SysDepartment;
import com.bim.service.SysDepartmentService;
import com.bim.util.R;

@Controller
@RequestMapping("/dept")
public class SysDepartmentController extends BaseClass{
	
	@Autowired
	private SysDepartmentService sysDepartmentService;
	@Autowired
	private SysDepartmentMapper sysDepartmentMapper;
	
	/**
	 * 页面初始化
	 */
    @RequestMapping("/init")
    public String init(){
		return "admin/dept";
    }
	
    /**
     * 部门维护
     * @param sysDepartment
     * @param bindingResult
     * @param loginUserId
     * @return
     */
	@RequestMapping(value="/addOrEditDept", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOrEditDept(@Valid SysDepartment sysDepartment,BindingResult bindingResult){
		//参数校验
		if (bindingResult.hasErrors()) {
            return R.To(false, null, bindingResult.getFieldError().getDefaultMessage());
        }
		return sysDepartmentService.addOrEditDept(sysDepartment);
	}
	
	/**
	 * 部门查询
	 * @param pageNumber
	 * @param pageSize
	 * @param loginUserId
	 * @param deptName
	 * @return
	 */
	@RequestMapping(value="/listDept")
	@ResponseBody
	public JSONObject listDept(int page, int rows,String deptName,Integer orgId){
		return sysDepartmentService.listDept(page,rows,deptName,orgId);
	}
	
	/**
	 * 批量删除部门
	 * 删除规则,部门下有人则该部门不能删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteDepts", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteDepts(@RequestParam(value = "ids[]") String[] ids){
		return sysDepartmentService.deleteDepts(ids);
	}
	
	@RequestMapping("selectDep")
	@ResponseBody
	public List<SysDepartment> getDepByOrgId(Integer orgId){
		return sysDepartmentMapper.selectDeptByOrgIdOrderByName(orgId);
	}
}
