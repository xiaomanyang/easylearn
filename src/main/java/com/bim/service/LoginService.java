package com.bim.service;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.CurrentUser;
import com.bim.entity.SysUser;
import com.bim.entity.UserLoginVO;

/**
 * 系统服务
 * @author wangc
 * @date 2017年8月2日
 * @description SystemService.java
 */
public interface LoginService {
	/**
	 * 用户名密码登录
	 * @param account 账号
	 * @param pwd 密码
	 * @param ip ip地址
	 * @param udid 设备标识
	 * @return
	 */
	JSONObject doLogin(String account,String pwd,String ip,String udid);
	
	/**
	 * 退出登录
	 * @return
	 */
	JSONObject doLogout();
	
	/**
	 * 根据token退出登录
	 * @return
	 */
	boolean doLogout(String token);
	
	/**
	 * 发送验证短信
	 * @param mobile
	 * @return
	 */
	JSONObject sendVerifySMS(String mobile);
	
	/**
	 * 根据短信登录
	 * @param mobile
	 * @param vcode
	 * @param ip ip地址
	 * @param udid 设备唯一标识
	 * @return
	 */
	JSONObject loginBySms(UserLoginVO vo);
	
	/**
	 * pc端手机号+短信 登录
	 * @param mobile
	 * @param vcode
	 * @param ip
	 * @return
	 */
	JSONObject loginByPcSms(String mobile,String vcode,String ip);
	
	/**
	 * 登录-二维码扫描
	 * @param accessToken
	 * @param uuid
	 * @return
	 */
	JSONObject loginScan(String accessToken, String uuid);
	
	/**
	 * 扫码后 手机端确认登录
	 * @param accessToken 移动端凭据
	 * @param uuid 二维码唯一标识
	 * @return
	 */
	JSONObject loginConfirm(String accessToken,String uuid);
	
	/**
	 * 扫码登录
	 * @param uuid 二维码唯一标识
	 * @return
	 */
	JSONObject loginByQRCode(String uuid);
	
	/**
	 * 根据用户获取需要存储的当前用户数据
	 * @param user
	 * @return
	 */
	CurrentUser getCurrentUserByUser(SysUser user);
}
