package com.bim.service;

import com.bim.entity.UserLoginVO;

public interface SysDeviceService {
	/**
	 * 保存用户设备信息 根据用户登录信息及设备id
	 * @param userLoginVO
	 * @return
	 */
	int updateByUserAndUdid(UserLoginVO userLoginVO);
}
