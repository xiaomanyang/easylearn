package com.bim.dao;

import java.util.List;

import com.bim.entity.SysProGroupRel;
import com.bim.entity.SysProGroupRelKey;

public interface SysProGroupRelMapper {
    int deleteByPrimaryKey(SysProGroupRelKey key);
    
    int deleteByGroupId(Integer groupId);

    int insert(SysProGroupRel record);
    int insertBatch(List<SysProGroupRel> records);

    int insertSelective(SysProGroupRel record);

    SysProGroupRel selectByPrimaryKey(SysProGroupRelKey key);

    int updateByPrimaryKeySelective(SysProGroupRel record);

    int updateByPrimaryKey(SysProGroupRel record);
}