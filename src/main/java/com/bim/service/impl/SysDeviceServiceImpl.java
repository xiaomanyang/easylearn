package com.bim.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bim.dao.SysDeviceMapper;
import com.bim.dao.SysUserMapper;
import com.bim.entity.SysDevice;
import com.bim.entity.UserLoginVO;
import com.bim.service.SysDeviceService;

@Service
public class SysDeviceServiceImpl implements SysDeviceService{
	
	@Autowired
	private SysDeviceMapper deviceMapper;
	
	@Autowired
	private SysUserMapper userMapper;

	/**
	 * 根据用户和udid,更新设备信息
	 * return 
	 */
	@Transactional
	@Override
	public int updateByUserAndUdid(UserLoginVO userLoginVO) {
		// TODO Auto-generated method stub
		SysDevice device= deviceMapper.findByUserIdAndUdid(userLoginVO.getUserId(),userLoginVO.getUdid());
		if(device==null){
			device=new SysDevice();
			device.setAppVersion(userLoginVO.getAppVersion());
			device.setPlatform(userLoginVO.getPlatform());
			device.setDeviceType(userLoginVO.getDeviceType());
			device.setUdid(userLoginVO.getUdid());
			device.setUserId(userLoginVO.getUserId());
			device.setCreateTime(new Date());
			device.setUpdateTime(new Date());
			deviceMapper.insertSelective(device);
			userMapper.addDeviceCount(userLoginVO.getUserId());
		}else{
			device.setUpdateTime(new Date());
			deviceMapper.updateByPrimaryKeySelective(device);
		}
		return 1;
	}
	
}
