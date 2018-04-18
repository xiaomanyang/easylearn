package com.bim.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bim.entity.BimProject;

@Repository
public interface BimProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BimProject record);

    int insertSelective(BimProject record);

    BimProject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BimProject record);

    int updateByPrimaryKey(BimProject record);
    
    List<BimProject> getList(@Param("orgId") Integer orgId, @Param("searchKey") String searchKey);

	List<BimProject> selectAll();
	
	List<BimProject> getListByOrgId(@Param("orgId") Integer orgId);
	
	List<BimProject> getListByUserId(@Param("userId") Integer userId);
	
	List<BimProject> getAllProject();
	
	/**
	 * 根据项目编码获取单个项目
	 * @param proCode 项目编码
	 * @return
	 */
	BimProject getByProCode(@Param("proCode") String proCode);
	/**
	 * 根据组id列表 获取项目
	 * @param groupId
	 * @return
	 */
	List<BimProject> getListByGroupId(List<Integer> groupId);
	
	/**
	 * 查询项目数据管理员的后台项目列表
	 * @param userId
	 * @return
	 */
	List<BimProject> getProListByUserId(Integer userId);
	
	/**
	 * TODO 项目组分配的项目查询
	 * @param groupId
	 * @return 所有项目，group_id不为空的项目是项目组中的项目
	 * List<BimProject>
	 * date:2017年11月13日
	 * user:BIM-10
	 */
	List<BimProject> getProjectOfGroupRel(Integer groupId);
	
	/**
	 * TODO 获得管理员管理的项目
	 * @param userId
	 * @return
	 * List<BimProject>
	 * date:2017年11月21日
	 * user:BIM-10
	 */
	List<BimProject> getProjectOfManager(Integer userId);
}