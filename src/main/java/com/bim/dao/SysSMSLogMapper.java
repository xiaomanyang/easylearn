package com.bim.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bim.entity.SysSMSLog;

public interface SysSMSLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysSMSLog record);

    int insertSelective(SysSMSLog record);

    SysSMSLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysSMSLog record);

    int updateByPrimaryKey(SysSMSLog record);
    
    /**
     * 查询指定时间内 手机号发送短信次数
     * @param mobile
     * @param sendTime
     * @return
     */
    int getCountByMobileAndTime(@Param("mobile") String mobile,@Param("sendTime") Date sendTime);
    
    /**
     * 根据手机号获取大于指定时间的最新记录
     * @param mobile
     * @param sendTime
     * @return
     */
    SysSMSLog getOneByMobile(@Param("mobile") String mobile,@Param("sendTime") Date sendTime);
}