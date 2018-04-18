package com.bim.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.bim.dao.SysMenuMapper;
import com.bim.dao.SysUserMenuRelMapper;
import com.bim.dao.SysUserProjectMenuRelMapper;
import com.bim.entity.SysMenu;
import com.bim.entity.SysUserMenuRelKey;
import com.bim.entity.SysUserProjectMenuRel;
import com.bim.entity.UserProMenuBO;
import com.bim.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuMapper menuMapper;
	
	@Autowired
	private SysUserMenuRelMapper userMenuRelMapper;
	
	@Autowired
	private SysUserProjectMenuRelMapper userProMenuRelMapper;
	
	@Override
	public List<SysMenu> getListByUser(Integer userId) {
		// TODO Auto-generated method stub
		return menuMapper.selectByUser(userId);
	}

	@Override
	public List<SysMenu> getAll(){
		// TODO Auto-generated method stub
		return menuMapper.selectBySign(0);
	}
	
	/**
	 * 获取用户项目菜单(前台)
	 * @param userId
	 * @return
	 */
	public List<UserProMenuBO> getMenu(int userId){
		List<UserProMenuBO> listUserProMenuBO=new ArrayList<>();
		boolean isEnUs=Locale.US.equals(LocaleContextHolder.getLocale());
		//获取前台菜单 普通用户
		List<SysMenu> menus=menuMapper.selectBySign(1);
		List<SysUserProjectMenuRel> listRel= userProMenuRelMapper.selectByUserId(userId);
		Map<Integer,List<SysUserProjectMenuRel>> map= listRel.stream().collect(Collectors.groupingBy(SysUserProjectMenuRel::getProjectId));
		/*Set<Entry<Integer, List<SysUserProjectMenuRel>>> set= map.entrySet();
		Iterator<Entry<Integer, List<SysUserProjectMenuRel>>> iterator=set.iterator();*/
		for (Iterator<Entry<Integer, List<SysUserProjectMenuRel>>> iterator=map.entrySet().iterator();iterator.hasNext();) {
			Map.Entry<Integer, List<SysUserProjectMenuRel>> entry = (Map.Entry<Integer, List<SysUserProjectMenuRel>>) iterator.next();
			UserProMenuBO entity=new UserProMenuBO();
			entity.setProId(entry.getKey());
			List<SysMenu> proMenus=new ArrayList<>();
			for(SysMenu menu : menus){
				for(SysUserProjectMenuRel item :entry.getValue()){
					if(menu.getId().equals(item.getMenuId())){
						SysMenu currMenu=new SysMenu();
						currMenu.setId(menu.getId());
						currMenu.setCode(menu.getCode());
						currMenu.setIcon(menu.getIcon());
						currMenu.setMenuName(isEnUs?menu.getMenuNameEn():menu.getMenuName());
						currMenu.setUrl(menu.getUrl());
						proMenus.add(currMenu);
						break;
					}
				}
			}
			
			/*for(SysUserProjectMenuRel item :entry.getValue()){
				SysMenu menu=getMenuById(item.getMenuId(), menus);
				if(menu!=null)
					proMenus.add(menu);
			}*/
			entity.setMenus(proMenus);
			listUserProMenuBO.add(entity);
		}

		/*
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
		}*/
		return listUserProMenuBO;
	}
	
	/**
	 * 从指定集合中 根据菜单id找出菜单对象
	 * @param menuId
	 * @param menus
	 * @return
	 */
	public SysMenu getMenuById(Integer menuId ,List<SysMenu> menus){
		SysMenu menu=null;
		for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();) {
			SysMenu sysMenu =  iterator.next();
			if(Objects.equals(menuId, sysMenu.getId())){
				menu=new SysMenu();
				menu.setId(sysMenu.getId());
				menu.setCode(sysMenu.getCode());
				menu.setIcon(sysMenu.getIcon());
				menu.setMenuName(sysMenu.getMenuName());
				menu.setUrl(sysMenu.getUrl());
				break;
			}
		}
		return menu;
	}
	
	
	/**
	 * 获取用户项目数据菜单(后台)
	 * @param userId
	 * @return
	 */
	public List<UserProMenuBO> getProDataMenu(int userId){
		List<UserProMenuBO> listUserProMenuBO=new ArrayList<>();
		//获取后台用户项目数据菜单 项目数据管理员
		List<SysMenu> menus=menuMapper.selectBySign(0);
		List<SysUserMenuRelKey> listRel= userMenuRelMapper.selectRelsByUserId(userId);
		Map<Integer,List<SysUserMenuRelKey>> map= listRel.stream().collect(Collectors.groupingBy(SysUserMenuRelKey::getProjectId));
		for (Iterator<Entry<Integer, List<SysUserMenuRelKey>>> iterator=map.entrySet().iterator();iterator.hasNext();) {
			Map.Entry<Integer, List<SysUserMenuRelKey>> entry = (Map.Entry<Integer, List<SysUserMenuRelKey>>) iterator.next();
			UserProMenuBO entity=new UserProMenuBO();
			entity.setProId(entry.getKey());
			List<SysMenu> proMenus=new ArrayList<>();
			for(SysUserMenuRelKey item :entry.getValue()){
				SysMenu menu=getMenuById(item.getMenuId(), menus);
				if(menu!=null)
					proMenus.add(menu);
			}
			entity.setMenus(proMenus);
			listUserProMenuBO.add(entity);
		}
		return listUserProMenuBO;
	}
}
