package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysMenu;

public interface SysMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

	List<SysMenu> selectBySign(@Param("type")Integer type);
	
	List<SysMenu> selectByUser(@Param("userId") Integer userId);
	
	List<SysMenu> selectAll();
	
	/**
	 * 获取用户系统菜单(存在则表示角色为系统管理员)
	 * @param userId
	 * @return
	 */
	List<SysMenu> getSystemMenus(@Param("userId") Integer userId);
	
	/**
	 * TODO 查询用户分配的系统权限， sign=9的
	 * @param userId
	 * @return
	 * List<SysMenu>
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	List<SysMenu> selectManagerMenu(Integer userId);
	
	/**
	 * TODO 根据管理员被分配的权限 查询用户分配的系统权限， sign=9的
	 * @param userId 用户id
	 * @param managerId 管理员id
	 * @return
	 * List<SysMenu>
	 * date:2017年11月15日
	 * user:BIM-10
	 */
	List<SysMenu> selectManagerMenuByManagerId(@Param("userId") Integer userId, @Param("managerId") Integer managerId);
	
}