package com.bim.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.SystemManage;
import com.bim.dao.SysProGroupRelMapper;
import com.bim.entity.SysProGroupRel;
import com.bim.service.BimProjectGroupService;
import com.bim.util.Code;
import com.bim.util.R;

@Service
public class BimProjectGroupServiceImpl implements BimProjectGroupService {
	
	@Autowired
	private SysProGroupRelMapper sysProGroupRelMapper;

	@Transactional
	@Override
	public JSONObject saveProjectGroupRel(int[] projectIds, int groupId) {
		try {
			// 删除关系表中原有记录
			sysProGroupRelMapper.deleteByGroupId(groupId);
			
			// 增加新记录
			List<SysProGroupRel> proGroupRels = new ArrayList<SysProGroupRel>();
			for(int projectId : projectIds){
				SysProGroupRel r = new SysProGroupRel();
				r.setGroupId(groupId);
				r.setProjectId(projectId);
				int userId = SystemManage.getInstance().getCurrentUserId();
				r.setCreateUser(userId);
				r.setUpdateUser(userId);
				r.setCreateTime(new Date());
				r.setUpdateTime(new Date());
				proGroupRels.add(r);
			}
			sysProGroupRelMapper.insertBatch(proGroupRels);
		} catch (Exception e) {
			e.printStackTrace();
			return R.To(false, null, Code.C_500);
		}
		return R.To(true, null, Code.C_200);
	}

}
