package com.bim.dao;

import java.util.List;

import com.bim.entity.SysUserGroupRel;
import com.bim.entity.SysUserGroupRelKey;

public interface SysUserGroupRelMapper {
    int deleteByPrimaryKey(SysUserGroupRelKey key);
    
    int deleteByUserId(Integer userId);

    int insert(SysUserGroupRel record);
    int insertBatch(List<SysUserGroupRel> records);

    int insertSelective(SysUserGroupRel record);

    SysUserGroupRel selectByPrimaryKey(SysUserGroupRelKey key);

    int updateByPrimaryKeySelective(SysUserGroupRel record);

    int updateByPrimaryKey(SysUserGroupRel record);
    
    List<SysUserGroupRelKey> getList(SysUserGroupRel record);
    
    /**
     * TODO 查询管理员关联的项目组的其他管理员的id
     * @param managerId
     * @return
     * List<Integer>
     * date:2017年11月22日
     * user:BIM-10
     */
    List<Integer> getManagerIdsByManagerId(Integer managerId);
    
    /**
     * TODO 查询管理员关联的项目组的id
     * @param managerId
     * @return
     * List<Integer>
     * date:2017年11月23日
     * user:BIM-10
     */
    List<Integer> getGroupIdsByManagerId(Integer managerId);
}