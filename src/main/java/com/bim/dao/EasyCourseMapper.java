package com.bim.dao;

import java.util.List;

import com.bim.entity.EasyCourse;

public interface EasyCourseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EasyCourse record);

    int insertSelective(EasyCourse record);

    EasyCourse selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasyCourse record);

    int updateByPrimaryKey(EasyCourse record);

	List<EasyCourse> selectByClassid(Integer classId);
}