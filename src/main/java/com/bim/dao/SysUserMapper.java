package com.bim.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysUser;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

	List<SysUser> selectByOrgId(SysUser record);

	List<SysUser> selectByMobile(SysUser user);

	List<SysUser> selectIsHaveInOrgIds(@Param("ids")String[] ids);

	List<SysUser> selectIsHaveInDeptIds(@Param("ids")String[] ids);
	
	SysUser selectByAccountAndPwd(@Param("account") String account,@Param("password") String password);
	
	SysUser selectByMobile(@Param("mobile") String mobile);
	
	List<Map<String,Object>> getListByProAndKey(@Param("proId") Integer proId,@Param("key") String key);
	
	List<SysUser> getListByIds(@Param("ids") Integer[] ids);
	
	/**
	 * TODO 查询项目权限管理员所管理的用户
	 * @param userId
	 * @return
	 * List<SysUser>
	 * date:2017年11月17日
	 * user:BIM-10
	 */
	List<SysUser> getUsersOfManager(@Param("userId") Integer userId, @Param("sysDepId") Integer sysDepId, @Param("mobile") String mobile);
	
	/**
	 * 获取所有用户名
	 * @return
	 */
	List<SysUser> getAll();
	
	/**
	 * 设备数量+1
	 * @return
	 */
	int addDeviceCount(Integer id);
}