package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysProGroup;

public interface SysProGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysProGroup record);

    int insertSelective(SysProGroup record);

    SysProGroup selectByPrimaryKey(Integer id);
    
    List<SysProGroup> listGroup(@Param("searchKey") String searchKey);
    
    List<SysProGroup> listAllOfUserRel(Integer userId);

    int updateByPrimaryKeySelective(SysProGroup record);

    int updateByPrimaryKey(SysProGroup record);
}