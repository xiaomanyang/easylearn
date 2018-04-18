package com.bim.service;

import com.alibaba.fastjson.JSONObject;

public interface BimProjectGroupService {
	JSONObject saveProjectGroupRel(int[] projectIds,int groupId);
}
