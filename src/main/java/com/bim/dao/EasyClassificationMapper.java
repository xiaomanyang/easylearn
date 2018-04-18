package com.bim.dao;

import java.util.List;

import com.bim.entity.EasyClassification;

public interface EasyClassificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EasyClassification record);

    int insertSelective(EasyClassification record);

    EasyClassification selectByPrimaryKey(Integer id);
    
    List<EasyClassification> selectAll();

    int updateByPrimaryKeySelective(EasyClassification record);

    int updateByPrimaryKey(EasyClassification record);
}