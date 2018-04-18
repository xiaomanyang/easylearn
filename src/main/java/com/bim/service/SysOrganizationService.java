package com.bim.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bim.entity.Combobox;
import com.bim.entity.SysOrganization;

public interface SysOrganizationService {

	JSONObject addOrEditOrg(SysOrganization sysOrganization);

	JSONObject listOrg(int page,int rows,String orgName);

	List<Combobox> listOrgByUserId();

	JSONObject deleteOrgs(String[] ids);

	List<Combobox> listOrg();
}
