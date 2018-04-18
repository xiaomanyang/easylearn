package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysUserProjectMenuRel;
import com.bim.entity.SysUserProjectMenuRelKey;

public interface SysUserProjectMenuRelMapper {
    int deleteByPrimaryKey(SysUserProjectMenuRelKey key);

    int insert(SysUserProjectMenuRel record);

    int insertSelective(SysUserProjectMenuRel record);

    SysUserProjectMenuRel selectByPrimaryKey(SysUserProjectMenuRelKey key);

    int updateByPrimaryKeySelective(SysUserProjectMenuRel record);

    int updateByPrimaryKey(SysUserProjectMenuRel record);

	List<SysUserProjectMenuRel> selectByUserId(@Param("userId")Integer userId);

	void deleteByUserIdAndManagerId(@Param("userId")Integer userId, @Param("managerId")Integer managerId);

	void betchAdd(List<SysUserProjectMenuRel> list);
	
	int getCountByProId(@Param("proId")Integer proId);
}