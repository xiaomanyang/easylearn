package com.bim.dao;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysAction;

public interface SysActionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAction record);

    int insertSelective(SysAction record);

    SysAction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAction record);

    int updateByPrimaryKey(SysAction record);
    
    SysAction getByToken(String token);
    
    SysAction getByUserAndPlatform(@Param("userId") int userId,@Param("platform") String platform);
}