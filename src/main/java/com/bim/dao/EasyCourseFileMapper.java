package com.bim.dao;

import com.bim.entity.EasyCourseFile;

public interface EasyCourseFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EasyCourseFile record);

    int insertSelective(EasyCourseFile record);

    EasyCourseFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasyCourseFile record);

    int updateByPrimaryKey(EasyCourseFile record);
}