package snod.com.cn.dao;

import java.util.List;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import snod.com.cn.entity.SysMenu;


/**
 * 菜单管理
 * @author lvjj
 */
public interface SysMenuDao{
	
	/**
	 * 根据角色id获取菜单列表
	 * @param roleId 角色id
	 * @return 菜单id列表
	 */
	List<Long> listMenuIdByRoleId(Long roleId);

	/**
	 * 查询用户的所有菜单ID
	 * @param userId 用户id
	 * @return 该用户所有可用的菜单
	 */
	List<SysMenu> listMenuByUserId(Long userId);
	
	/**
	 * 获取系统的所有菜单
	 * @return 系统的所有菜单
	 */
	List<SysMenu> listMenu();

	/**
	 * 获取简单的menu tree 用于在 ele-ui tree中显示，根据orderNum排序
	 * @return ztreeDto列表
	 */
	List<SysMenu> listSimpleMenuNoButton();

	/**
	 * 获取一级菜单
	 * @return 一级菜单列表
	 */
	List<SysMenu> listRootMenu();
	
	/**
	 * 根据一级菜单id 获取二级菜单
	 * @return 二级菜单列表
	 */
	List<SysMenu> listChildrenMenuByParentId(Long parentId);

	List<SysMenu> listMenuAndBtn();

	SysMenu queryMenuInfo(Long menuId);

	void insertMenu(@Valid SysMenu menu);

	SysMenu queryMenuInfoByParentId(Long parentId);

	void updateById(@Valid SysMenu menu);

	void removeById(Long menuId);

}
