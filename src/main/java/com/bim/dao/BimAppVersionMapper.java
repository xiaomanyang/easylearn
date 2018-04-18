package com.bim.dao;

import java.util.List;

import com.bim.entity.BimAppVersion;

public interface BimAppVersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BimAppVersion record);

    int insertSelective(BimAppVersion record);

    BimAppVersion selectByPrimaryKey(Integer id);
    
    BimAppVersion selectCurrentVersion();
    
    List<BimAppVersion> listAppVersion();

    int updateByPrimaryKeySelective(BimAppVersion record);
    int updateSetCurrent0();

    int updateByPrimaryKey(BimAppVersion record);
}