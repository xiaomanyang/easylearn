package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysDepartment;

public interface SysDepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDepartment record);

    int insertSelective(SysDepartment record);

    SysDepartment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDepartment record);

    int updateByPrimaryKey(SysDepartment record);

	List<SysDepartment> selectDeptByOrgId(SysDepartment select);
	List<SysDepartment> selectDeptByOrgIdOrderByName(Integer orgId);

	void betchDelete(@Param("ids")String[] ids);

	List<SysDepartment> selectByDeptName(@Param("departmentName")String departmentName, @Param("orgId")Integer orgId);
}