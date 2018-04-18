package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysUserMenuRel;
import com.bim.entity.SysUserMenuRelKey;

public interface SysUserMenuRelMapper {
    int deleteByPrimaryKey(SysUserMenuRelKey key);

    int insert(SysUserMenuRel record);

    int insertSelective(SysUserMenuRel record);

    SysUserMenuRel selectByPrimaryKey(SysUserMenuRelKey key);

    int updateByPrimaryKeySelective(SysUserMenuRel record);

    int updateByPrimaryKey(SysUserMenuRel record);

	List<SysUserMenuRel> selectByUserId(@Param("userId")Integer userId);

	void deleteByUserIdAndManagerId(@Param("userId")Integer userId, @Param("managerId")Integer managerId);

	void betchAdd(List<SysUserMenuRel> list);
	
	List<SysUserMenuRelKey> selectRelsByUserId(@Param("userId")Integer userId);
}