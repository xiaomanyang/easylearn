package com.bim.dao;

import java.util.List;

import com.bim.entity.SysServer;

public interface SysServerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysServer record);

    int insertSelective(SysServer record);

    SysServer selectByPrimaryKey(Integer id);
    
    List<SysServer> listServer();

    int updateByPrimaryKeySelective(SysServer record);

    int updateByPrimaryKey(SysServer record);
}