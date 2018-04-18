package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysOrgProjectRel;
import com.bim.entity.SysOrgProjectRelKey;

public interface SysOrgProjectRelMapper {
    int deleteByPrimaryKey(SysOrgProjectRelKey key);

    int insert(SysOrgProjectRel record);

    int insertSelective(SysOrgProjectRel record);

    SysOrgProjectRel selectByPrimaryKey(SysOrgProjectRelKey key);

    int updateByPrimaryKeySelective(SysOrgProjectRel record);

    int updateByPrimaryKey(SysOrgProjectRel record);

	List<SysOrgProjectRel> selectByOrgId(@Param("orgId")Integer orgId);
	
	List<SysOrgProjectRel> selectByProId(@Param("proId")Integer proId);
	
	int betchAdd(List<SysOrgProjectRel> list);
	
	/**
	 * 根据项目id删除 关联关系
	 * @param proId
	 * @return
	 */
	int deleteByProId(@Param("proId")Integer proId);

	List<SysOrgProjectRel> selectIsHaveInOrgIds(@Param("ids")String[] ids);
}