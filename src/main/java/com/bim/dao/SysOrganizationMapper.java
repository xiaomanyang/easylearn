package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysOrganization;
import com.bim.entity.SysUser;

public interface SysOrganizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysOrganization record);

    int insertSelective(SysOrganization record);

    SysOrganization selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysOrganization record);

    int updateByPrimaryKey(SysOrganization record);

	List<SysOrganization> selectAllOrById(SysUser user);

	void betchDelete(@Param("ids")String[] ids);

	List<SysOrganization> selectByOrgNameOrShortName(SysOrganization name);
	
	List<SysOrganization> selectAllOrderByName();
	
	/**
	 * TODO 查询管理员管理的用户所在的组织机构部门
	 * @param userId
	 * @return
	 * List<SysOrganization>
	 * date:2017年11月20日
	 * user:BIM-10
	 */
	List<SysOrganization> selectOrgDepByManagerId(Integer userId);
	
	List<SysOrganization> selectAll();
}