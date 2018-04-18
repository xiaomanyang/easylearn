package com.bim.service;

import java.util.List;

import com.bim.entity.SysMenu;
import com.bim.entity.UserProMenuBO;

public interface SysMenuService {
	List<SysMenu> getListByUser(Integer userId);
	List<SysMenu> getAll();
	List<UserProMenuBO> getProDataMenu(int userId);
	List<UserProMenuBO> getMenu(int userId);
}
