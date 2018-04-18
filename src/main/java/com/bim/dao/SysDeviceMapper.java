package com.bim.dao;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysDevice;

public interface SysDeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDevice record);

    int insertSelective(SysDevice record);

    SysDevice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDevice record);

    int updateByPrimaryKey(SysDevice record);
    
    SysDevice findByUserIdAndUdid(@Param("userId") Integer id,@Param("udid") String udid);
}