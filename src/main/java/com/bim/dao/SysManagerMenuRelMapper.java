package com.bim.dao;

import java.util.List;

import com.bim.entity.SysManagerMenuRel;
import com.bim.entity.SysManagerMenuRelKey;

public interface SysManagerMenuRelMapper {
    int deleteByPrimaryKey(SysManagerMenuRelKey key);
    
    /**
     * TODO 根据用户id删除系统管理权限
     * @param userId
     * @return
     * int
     * date:2017年11月16日
     * user:BIM-10
     */
    int deleteByUserId(Integer userId);

    int insert(SysManagerMenuRel record);
    
    /**
     * TODO 批量插入
     * @param records
     * @return
     * int
     * date:2017年11月16日
     * user:BIM-10
     */
    int insertBatch(List<SysManagerMenuRel> records);

    int insertSelective(SysManagerMenuRel record);

    SysManagerMenuRel selectByPrimaryKey(SysManagerMenuRelKey key);

    int updateByPrimaryKeySelective(SysManagerMenuRel record);

    int updateByPrimaryKey(SysManagerMenuRel record);
}