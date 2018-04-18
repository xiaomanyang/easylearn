package com.bim.service;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.SysDepartment;

public interface SysDepartmentService {

	JSONObject addOrEditDept(SysDepartment sysDepartment);

	JSONObject listDept(int page, int rows, String deptName,Integer orgId);

	JSONObject deleteDepts(String[] ids);

}
