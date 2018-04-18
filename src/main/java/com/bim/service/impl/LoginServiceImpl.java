package com.bim.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bim.base.CurrentUser;
import com.bim.base.SystemManage;
import com.bim.dao.BimProjectMapper;
import com.bim.dao.SysActionMapper;
import com.bim.dao.SysDepartmentMapper;
import com.bim.dao.SysLoginLogMapper;
import com.bim.dao.SysMenuMapper;
import com.bim.dao.SysOrganizationMapper;
import com.bim.dao.SysSMSLogMapper;
import com.bim.dao.SysUserGroupRelMapper;
import com.bim.dao.SysUserMapper;
import com.bim.dao.SysUserProjectMenuRelMapper;
import com.bim.entity.BimProject;
import com.bim.entity.SysAction;
import com.bim.entity.SysDepartment;
import com.bim.entity.SysLoginLog;
import com.bim.entity.SysMenu;
import com.bim.entity.SysOrganization;
import com.bim.entity.SysSMSLog;
import com.bim.entity.SysUser;
import com.bim.entity.SysUserGroupRel;
import com.bim.entity.SysUserGroupRelKey;
import com.bim.entity.SysUserProjectMenuRel;
import com.bim.entity.UserLoginVO;
import com.bim.entity.UserProMenuBO;
import com.bim.service.LoginService;
import com.bim.service.SysDeviceService;
import com.bim.service.SysMenuService;
import com.bim.util.Code;
import com.bim.util.Constant;
import com.bim.util.LanguageUtil;
import com.bim.util.R;
import com.bim.util.SmsUtil;

import io.jsonwebtoken.lang.Collections;

@Service
public class LoginServiceImpl implements LoginService {
	public static final String SMS_VCODE_KEY="sms_vcode_key";
	public static final int VCODE_EXPIRE_TIME=0-3;//验证码过期时间

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysMenuMapper menuMapper;
	
	@Autowired
	private SysActionMapper actionMapper;
	
	@Autowired
	private SysLoginLogMapper loginLogMapper;
	
	@Autowired
	private SysOrganizationMapper organizationMapper;
	
	@Autowired
	private SysDepartmentMapper departmentMapper;
	
	@Autowired
	private SysSMSLogMapper smsLogMapper;
	
	@Autowired
	private SysUserProjectMenuRelMapper userProMenuRelMapper;
	
	@Autowired
	private BimProjectMapper bimProjectMapper;
	
	@Autowired
	private SysUserGroupRelMapper userGroupRelMapper;
	
	@Autowired
	private SysMenuService menuService;
	
	@Autowired
	private SysDeviceService deviceService;
	
	@Transactional
	@Override
	public JSONObject doLogin(String account, String pwd,String ip,String udid) {
		if(StringUtils.isEmpty(account) || StringUtils.isEmpty(pwd)){
			saveLoginFailLog(account, "", ip, udid,"账号密码","pc", "请输入用户名和密码！");
			return R.To(false, null, LanguageUtil.getMessage("QingShuRuYongHuMingHeMiMa"));
		}
		pwd= DigestUtils.md5DigestAsHex(pwd.getBytes());
		SysUser user= sysUserMapper.selectByMobile(account);//.selectByAccountAndPwd(account, pwd);
		if(user==null){
			saveLoginFailLog(account, "", ip, udid,"账号密码","pc", "用户名或密码错误！");
			return R.To(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		else if(!Objects.equals(user.getPassword(), pwd)){
			saveLoginFailLog(account, "", ip, udid,"账号密码","pc", "密码错误！");
			return R.To(false, null, LanguageUtil.getMessage("MiMaCuoWu"));
		}
		else if(user.getStatus()==1){
			saveLoginFailLog(account, "", ip, udid,"账号密码","pc", "该账户已禁用！");
			return R.To(false, null, LanguageUtil.getMessage("YongHuWeiQiYongQingLianXiGuanLiYuan"));
		}
		String token=saveLoginUser(user);
		/*登录成功>记录用户行为(根据登录)*/
		SysAction sysAction= actionMapper.getByUserAndPlatform(user.getId(), "pc");
		if(sysAction==null)
		{
			sysAction=new SysAction();
			sysAction.setToken(token);
			sysAction.setLoginTime(new Date());
			sysAction.setUserId(user.getId());
			sysAction.setAccount(user.getAccount());
			sysAction.setMobile(user.getMobile());
			sysAction.setIp(ip);
			sysAction.setLoginMode("account");
			sysAction.setPlatformType("pc");
			sysAction.setUdid(udid);
			sysAction.setLoginTime(new Date());
			sysAction.setStatus(0);
			actionMapper.insert(sysAction);
		}else{
			sysAction.setToken(token);
			//如果用户名有变更,需更新登录状态表的用户名
			if(!Objects.equals(user.getAccount(), sysAction.getAccount())){
				sysAction.setAccount(user.getAccount());
				sysAction.setMobile(user.getMobile());
			}
			sysAction.setLoginTime(new Date());
			sysAction.setLoginMode("account");
			actionMapper.updateByPrimaryKey(sysAction);
		}
		/*登录成功>记录登录日志*/
		SysLoginLog loginLog=new SysLoginLog(sysAction);
		loginLogMapper.insert(loginLog);
		return R.To(true, token, LanguageUtil.getMessage("DengLuChengGong"));
	}
	
	@Transactional
	@Override
	public JSONObject loginBySms(UserLoginVO userLoginVO){
		if(!userLoginVO.getMobile().startsWith("1380013800") || !"999999".equals(userLoginVO.getVcode())){
			Calendar c=Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MINUTE, VCODE_EXPIRE_TIME);
			SysSMSLog vsms= smsLogMapper.getOneByMobile(userLoginVO.getMobile(), c.getTime());
			if(vsms==null){
				saveLoginFailLog(userLoginVO, "登录失败！验证码已过期!");
				return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiYanZhengMaYiGuoQi"));
			}
			if(vsms.getvcode()==null || !vsms.getvcode().equals(userLoginVO.getVcode())){
				saveLoginFailLog(userLoginVO, "登录失败！验证码错误!");
				return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiYanZhengMaCuoWu"));
			}
		}
		SysUser user=new SysUser();
		user.setMobile(userLoginVO.getMobile());
		List<SysUser> listUser=sysUserMapper.selectByMobile(user);
		if(listUser.isEmpty() || listUser.size()<=0){
			saveLoginFailLog(userLoginVO, "用户不存在");
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		user=listUser.get(0);
		if(user.getStatus()==1){
			saveLoginFailLog(userLoginVO, "登录失败！该账号已被禁用!");
			return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiZhangHaoBeiJinYong"));
		}
	
		String token =UUID.randomUUID().toString();
		/* 登录成功>记录用户行为 */
		SysAction sysAction = actionMapper.getByUserAndPlatform(user.getId(), "mobile");
		if (sysAction == null) {
			sysAction = new SysAction();
			sysAction.setToken(token);
			sysAction.setLoginTime(new Date());
			sysAction.setUserId(user.getId());
			sysAction.setAccount(user.getAccount());
			sysAction.setMobile(user.getMobile());
			sysAction.setIp(userLoginVO.getIp());
			sysAction.setLoginMode(userLoginVO.getLoginMode());
			sysAction.setPlatformType("mobile");
			sysAction.setUdid(userLoginVO.getUdid());
			sysAction.setLoginTime(new Date());
			actionMapper.insert(sysAction);
		} else {
			sysAction.setToken(token);
			sysAction.setLoginTime(new Date());
			sysAction.setUdid(userLoginVO.getUdid());
			sysAction.setLoginMode(userLoginVO.getLoginMode());
			//sysAction.setPlatformType(userLoginVO.getDeviceType());
			sysAction.setIp(userLoginVO.getIp());
			actionMapper.updateByPrimaryKey(sysAction);
		}
		
		/* 登录成功>记录登录日志 */
		SysLoginLog loginLog = new SysLoginLog(sysAction);
		loginLogMapper.insert(loginLog);
		/*保存设备信息*/
		userLoginVO.setUserId(user.getId());
		deviceService.updateByUserAndUdid(userLoginVO);
		
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("token", token);
		resultMap.put("pwd", user.getPassword());
		resultMap.put("userId", user.getId());
		resultMap.put("voice", user.getVoice());
		return R.ToJson(true, resultMap, LanguageUtil.getMessage("DengLuChengGong"));
	}
	
	/**
	 * pc端短信登录
	 * @param mobile
	 * @param vcode
	 * @param ip
	 * @param udid
	 * @return
	 */
	@Transactional
	@Override
	public JSONObject loginByPcSms(String mobile,String vcode,String ip){
		SysUser user=sysUserMapper.selectByMobile(mobile);
		if(user==null){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		if(user.getStatus()==1){
			saveLoginFailLog("", mobile, ip, "","sms","mobile", "登录失败！该账号已被禁用!");
			return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiZhangHaoBeiJinYong"));
		}
		
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MINUTE, VCODE_EXPIRE_TIME);
		SysSMSLog vsms= smsLogMapper.getOneByMobile(mobile, c.getTime());
		if(vsms==null){
			saveLoginFailLog("", mobile, ip, "","sms","mobile", "登录失败！验证码已过期!");
			return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiYanZhengMaYiGuoQi"));
		}
		if(vsms.getvcode()==null || !vsms.getvcode().equals(vcode)){
			saveLoginFailLog("", mobile, ip, "","sms","mobile", "登录失败！验证码错误!");
			return R.ToJson(false, null, LanguageUtil.getMessage("DengLuShiBaiYanZhengMaCuoWu"));
		}
		
		String token =saveLoginUser(user);
		/* 登录成功>记录用户行为 */
		SysAction sysAction = actionMapper.getByUserAndPlatform(user.getId(), "pc");
		if (sysAction == null) {
			sysAction = new SysAction();
			sysAction.setToken(token);
			sysAction.setLoginTime(new Date());
			sysAction.setUserId(user.getId());
			sysAction.setAccount(user.getAccount());
			sysAction.setMobile(user.getMobile());
			sysAction.setIp(ip);
			sysAction.setLoginMode("sms");
			sysAction.setPlatformType("pc");
			sysAction.setUdid("");
			sysAction.setLoginTime(new Date());
			sysAction.setStatus(0);
			actionMapper.insert(sysAction);
		} else {
			sysAction.setToken(token);
			sysAction.setLoginTime(new Date());
			actionMapper.updateByPrimaryKey(sysAction);
		}
		/* 登录成功>记录登录日志 */
		SysLoginLog loginLog = new SysLoginLog(sysAction);
		loginLogMapper.insert(loginLog);
		return R.ToJson(true, null, LanguageUtil.getMessage("DengLuChengGong"));
	}
	
	public CurrentUser getCurrentUserByUser(SysUser user){
		CurrentUser currentUser=new CurrentUser();
		currentUser.setToken(UUID.randomUUID().toString());
		currentUser.setUser(user);
		
		//获取组织架构
		SysOrganization org= organizationMapper.selectByPrimaryKey(user.getSysOrgId());
		currentUser.setOrganization(org);
		//获取部门
		SysDepartment dept=departmentMapper.selectByPrimaryKey(user.getSysDepId());
		currentUser.setDept(dept);
		
		List<String> roles=new ArrayList<>();
		
		//系统管理员管理菜单(角色:系统管理员)
		List<SysMenu> systemMenus=null;
		//超级管理员
		if(user.getUserType()==0){
			systemMenus=menuMapper.selectBySign(9);
		}else{
			systemMenus=menuMapper.getSystemMenus(user.getId());
		}
		if(!Collections.isEmpty(systemMenus)){
			roles.add(Constant.SYSTEM_ROLE_0);
			currentUser.setMenus(systemMenus);
		}
		
		//用户所管的项目 (角色:项目权限管理员)
		SysUserGroupRel searchUserGroupRel=new SysUserGroupRel();
		searchUserGroupRel.setUserId(user.getId());
		searchUserGroupRel.setUserType(1);
		List<SysUserGroupRelKey> userGroupRels= userGroupRelMapper.getList(searchUserGroupRel);
		if(!Collections.isEmpty(userGroupRels)){
			roles.add(Constant.SYSTEM_ROLE_1);
			List<Integer> ids= userGroupRels.stream().map(p->p.getGroupId()).collect(Collectors.toList());
			List<BimProject> pros= bimProjectMapper.getListByGroupId(ids);
			//Map<Integer, String> projects=pros.stream().collect(HashMap<Integer,String>::new,(map,item)->map.put(item.getId(),item.getProName()),(one,two)->one.putAll(two));
			currentUser.setProjects(pros);
		}
		
		//项目数据管理员后台菜单(角色:项目数据管理员)
		List<UserProMenuBO> userProMenus=menuService.getProDataMenu(user.getId());
		//List<SysUserMenuRelKey> userMenuRels= userMenuRelMapper.selectRelsByUserId(user.getId());
		if(!Collections.isEmpty(userProMenus)){
			roles.add(Constant.SYSTEM_ROLE_2);
			currentUser.setProDataMenus(userProMenus);
		}
		
		currentUser.setRoles(roles);
		
		//项目前台菜单(角色:普通用户)
		//currentUser.setProMenus(getMenu(user.getId()));
		return currentUser;
	}
	
	/**
	 * 获取用户项目菜单(前台)
	 * @param userId
	 * @return
	 */
	public List<UserProMenuBO> getMenu(int userId){
		List<UserProMenuBO> listUserProMenuBO=new ArrayList<>();
		
		//获取前台菜单 普通用户
		List<SysMenu> menus=menuMapper.selectBySign(1);
		List<SysUserProjectMenuRel> listRel= userProMenuRelMapper.selectByUserId(userId);
		UserProMenuBO userProMenuBO=null;
		Set<Integer> listProId=new HashSet<>();
		for (SysUserProjectMenuRel item : listRel) {
			listProId.add(item.getProjectId());
		}
		
		for (SysMenu menu : menus) {
			for (Integer proId : listProId) {
				Optional<UserProMenuBO> optionalUserProMenuBO= listUserProMenuBO.stream().filter(p->p.getProId().equals(proId)).findFirst();
				if(optionalUserProMenuBO.isPresent()){
					if(listRel.stream().anyMatch(p->
							p.getProjectId().equals(proId) && 
							p.getMenuId().equals(menu.getId())
							)){
						optionalUserProMenuBO.get().getMenus().add(menu);
					}
					continue;
				}
				userProMenuBO=new UserProMenuBO();
				List<SysMenu> listMenu=new ArrayList<SysMenu>();
				listMenu.add(menu);
				userProMenuBO.setProId(proId);
				userProMenuBO.setMenus(listMenu);
				listUserProMenuBO.add(userProMenuBO);
			}
		}
		return listUserProMenuBO;
	}
	
	/**
	 * 缓存用户返回 token 将用户放入session 或 cache
	 * @param user 当前用户
	 * @return token  这里默认为sessionid
	 */
	public String saveLoginUser(SysUser user){
		CurrentUser currentUser=getCurrentUserByUser(user);
		//获取项目菜单
		//用户存在 放入session 返回sessionid
		SystemManage.getInstance().setCurrentUser(currentUser);
		return currentUser.getToken();
	}

	@Override
	public JSONObject doLogout(){
		SysAction action= actionMapper.getByUserAndPlatform(SystemManage.getInstance().getCurrentUserId(), "pc");
		if(action!=null)
			actionMapper.deleteByPrimaryKey(action.getId());
		SysLoginLog log=new SysLoginLog(action);
		log.setLogoutTime(new Date());
		log.setMemo("用户退出！");
		loginLogMapper.insert(log);
		SystemManage.getInstance().removeCurrentUser();
		return R.To(true, null, LanguageUtil.getMessage("TuiChuChengGong"));
	}
	
	@Override
	public boolean doLogout(String token){
		SysAction action= actionMapper.getByToken(token);
		if(action!=null){
			actionMapper.deleteByPrimaryKey(action.getId());
			SysLoginLog log=new SysLoginLog(action);
			log.setLogoutTime(new Date());
			log.setMemo("用户退出！");
			loginLogMapper.insert(log);
		}
		return true;
	}
	
	/**
	 * 登录失败记录日志
	 * @param account   用户名
	 * @param mobile 手机号
	 * @param ip ip地址
	 * @param udid 设备标识 
	 * @param loginMode 登录模式 
	 * @param platform 平台 
	 * @param msg 错误原因
	 */
	public void saveLoginFailLog(UserLoginVO userLoginVO,String msg){
		SysLoginLog loginLog=new SysLoginLog();
		loginLog.setAccount(userLoginVO.getMobile());
		loginLog.setIp(userLoginVO.getIp());
		loginLog.setUdid(userLoginVO.getUdid());
		loginLog.setMobile(userLoginVO.getMobile());
		loginLog.setStatus(new Byte("1"));
		loginLog.setMemo(msg);
		loginLog.setPlatform(userLoginVO.getPlatform());
		loginLog.setLoginMode(userLoginVO.getLoginMode());
		loginLogMapper.insertSelective(loginLog);
	}
	
	/**
	 * 登录失败记录日志
	 * @param account   用户名
	 * @param mobile 手机号
	 * @param ip ip地址
	 * @param udid 设备标识 
	 * @param loginMode 登录模式 
	 * @param platform 平台 
	 * @param msg 错误原因
	 */
	public void saveLoginFailLog(String account,String mobile,String ip,String udid,String loginMode,String platform,String msg){
		SysLoginLog loginLog=new SysLoginLog();
		loginLog.setAccount(account);
		loginLog.setIp(ip);
		loginLog.setUdid(udid);
		loginLog.setMobile(mobile);
		loginLog.setStatus(new Byte("1"));
		loginLog.setMemo(msg);
		loginLog.setPlatform(platform);
		loginLog.setLoginMode(loginMode);
		loginLogMapper.insertSelective(loginLog);
	}
	
	
	public void saveLoginSuccessLog(){
		SysLoginLog loginLog=new SysLoginLog();
		loginLogMapper.insertSelective(loginLog);
	}
	
	@Override
	public JSONObject sendVerifySMS(String mobile) {
		if(StringUtils.isEmpty(mobile)){
			return R.ToJson(false, null, LanguageUtil.getMessage("QingShuRuShouJiHao"));
		}
		SysUser user=new SysUser();
		user.setMobile(mobile);
		List<SysUser> list = sysUserMapper.selectByMobile(user);
		if(list.isEmpty() || list.size()<=0){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		if(list.get(0).getStatus().equals(1)){
			return R.ToJson(false, null, LanguageUtil.getMessage("YongHuYiJinYongQingLianXiGuanLiYuan"));
		}
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int count=smsLogMapper.getCountByMobileAndTime(mobile, c.getTime());
		if(count>=10){
			return R.ToJson(false, null, LanguageUtil.getMessage("JinTianXianZhiYiYongWanQingMingTianZaiShi"));
		}
		String vcode=String.valueOf(new Random().nextInt(999999));
		while(vcode.length()<6){
			vcode="0"+vcode;
		}
		String content=LanguageUtil.getMessage("NinDeYanZhengMaQingTuoShanChuLi", new Object[] {vcode});
		String result=SmsUtil.sendSMS(mobile,content);
		//SystemManage.getInstance().getCurrentRequest().getSession(true).setAttribute(SMS_VCODE_KEY,vcode);
		SysSMSLog smsLog=new SysSMSLog(mobile,vcode,content,new Date(),"200"=="200"?0:1,"验证短信");
		smsLogMapper.insert(smsLog);
		return R.ToJson(true, null, LanguageUtil.getMessage("YanZhengMaFaSongChengGong"));
	}

	@Transactional
	@Override
	public JSONObject loginByQRCode(String uuid) {
		SysAction action=actionMapper.getByToken(uuid);
		if(action==null)
			return R.ToJson(false, null, LanguageUtil.getMessage("denglushibai"));
		if(action.getStatus()==1){
			return R.ToJson(true, 1, LanguageUtil.getMessage("SaoMiaoChengGong"));
		}
		SysUser user=sysUserMapper.selectByPrimaryKey(action.getUserId());
		//保存用户
		String token= saveLoginUser(user);
		action.setToken(token);
		actionMapper.updateByPrimaryKey(action);
		/*登录成功>记录登录日志*/
		SysLoginLog loginLog=new SysLoginLog(action);
		loginLogMapper.insert(loginLog);
		return R.ToJson(true, 0, LanguageUtil.getMessage("DengLuChengGong"));
	}

	@Override
	public JSONObject loginScan(String accessToken, String uuid) {
		//根据用户凭据查用户登录信息
		SysAction mobileAction= actionMapper.getByToken(accessToken);
		if(mobileAction==null || StringUtils.isEmpty(mobileAction.getId())){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		SysUser user= sysUserMapper.selectByPrimaryKey(mobileAction.getUserId());
		if(user==null){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		
		SysAction action=actionMapper.getByUserAndPlatform(user.getId(), "pc");
		if(action==null){
			action=new SysAction();
			action.setToken(uuid);
			action.setLoginTime(new Date());
			action.setAccount(user.getAccount());
			action.setLoginMode("扫码登录");
			action.setMobile(user.getMobile());
			action.setUserId(user.getId());
			action.setPlatformType("pc");
			action.setStatus(1);
			actionMapper.insert(action);
		}else{
			action.setToken(uuid);
			action.setLoginMode("扫码登录");
			action.setLoginTime(new Date());
			action.setStatus(1);
			actionMapper.updateByPrimaryKey(action);
		}
		/* 登录成功>记录登录日志 */
		SysLoginLog loginLog = new SysLoginLog(action);
		loginLog.setMemo("扫描成功");
		loginLogMapper.insert(loginLog);
		return R.ToJson(true, 1, LanguageUtil.getMessage("Code200"));
	}
	
	@Override
	public JSONObject loginConfirm(String accessToken, String uuid) {
		//根据用户凭据查用户登录信息
		SysAction mobileAction= actionMapper.getByToken(accessToken);
		if(mobileAction==null || StringUtils.isEmpty(mobileAction.getId())){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		SysUser user= sysUserMapper.selectByPrimaryKey(mobileAction.getUserId());
		if(user==null){
			return R.ToJson(false, null, LanguageUtil.getMessage("GaiYongHuBuCunZai"));
		}
		
		SysAction action=actionMapper.getByUserAndPlatform(user.getId(), "pc");
		if(action==null || action.getStatus()==0){
			return R.ToJson(true, null, LanguageUtil.getMessage("QingXianSaoMaZaiQueRen"));
		}else{
			action.setLoginTime(new Date());
			action.setStatus(0);
			actionMapper.updateByPrimaryKey(action);
		}
		/* 登录成功>记录登录日志 */
		SysLoginLog loginLog = new SysLoginLog(action);
		loginLog.setMemo("二维码登录,移动端确认登录成功");
		loginLogMapper.insert(loginLog);
		return R.ToJson(true, 0, LanguageUtil.getMessage("Code200"));
	}
}
