package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bim.entity.SysDict;


public interface SysDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);
    
    List<SysDict> getListByKey(@Param("searchKey") String searchKey);
    
    List<SysDict> getListByParentCode(@Param("parentCode") String parentCode);

	SysDict getSingleByCode(@Param("userLevel")String userLevel);
	
	/**
     * 批量更新删除状态
     * @param isDelete
     * @param ids
     * @return
     */
    int batchUpdateStatusByIds(@Param("isDelete") int isDelete,@Param("ids") int[] ids);
    
    /**
     * 根据编码查询字典
     * @param code
     * @param excludeId 排除的id
     * @return
     */
    SysDict getByCode(@Param("code")String code,@Param("excludeId") Integer excludeId);
}