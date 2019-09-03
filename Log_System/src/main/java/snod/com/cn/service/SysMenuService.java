package snod.com.cn.service;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import snod.com.cn.dao.SysMenuDao;
import snod.com.cn.dao.SysRoleMenuDao;
import snod.com.cn.entity.SysMenu;

/**
 * @author lvjj
 */
@Service("sysMenuService")
public class SysMenuService{
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;

	public List<SysMenu> listMenuByUserId(Long userId) {
		// 用户的所有菜单信息
		List<SysMenu> sysMenus ;
		//系统管理员，拥有最高权限
		if(userId == 1){
			sysMenus = sysMenuDao.listMenu();
		}else {
			sysMenus = sysMenuDao.listMenuByUserId(userId);
		}
		
		Map<Long, List<SysMenu>> sysMenuLevelMap = sysMenus.stream()
				.sorted(Comparator.comparing(SysMenu::getOrderNum))
				.collect(Collectors.groupingBy(SysMenu::getParentId));
		
		// 一级菜单
		List<SysMenu> rootMenu = sysMenuLevelMap.get(0L);
		if (CollectionUtil.isEmpty(rootMenu)) {
			return Collections.emptyList();
		}
		// 二级菜单
		for (SysMenu sysMenu : rootMenu) {
			sysMenu.setList(sysMenuLevelMap.get(sysMenu.getMenuId()));
		}
		return rootMenu;
	}

	
//	public List<SysMenu> queryListSysMenu() {
//		return sysMenuDao.queryListSysMenu();
//	}
	

	public List<Long> listMenuIdByRoleId(Long roleId) {
		return sysMenuDao.listMenuIdByRoleId(roleId);
	}


	
	public List<SysMenu> listSimpleMenuNoButton() {
		return sysMenuDao.listSimpleMenuNoButton();
	}


	public List<SysMenu> listRootMenu() {
		return sysMenuDao.listRootMenu();
	}

	
	public List<SysMenu> listChildrenMenuByParentId(Long parentId) {
		return sysMenuDao.listChildrenMenuByParentId(parentId);
	}


	public List<SysMenu> listMenuAndBtn() {
		return sysMenuDao.listMenuAndBtn();
	}

	public SysMenu queryMenuInfo(Long menuId) {
		return sysMenuDao.queryMenuInfo(menuId);
	}

	public void insertMenu(@Valid SysMenu menu) {
		 sysMenuDao.insertMenu(menu);
	}

	public SysMenu queryMenuInfoByParentId(Long parentId) {
		return sysMenuDao.queryMenuInfoByParentId(parentId);
	}

	public void updateById(@Valid SysMenu menu) {
		 sysMenuDao.updateById(menu);
	}

	public void deleteMenuAndRoleMenu(Long menuId) {
		//删除菜单
		sysMenuDao.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuDao.deleteByMenuId(menuId);
		
	}
	
}
