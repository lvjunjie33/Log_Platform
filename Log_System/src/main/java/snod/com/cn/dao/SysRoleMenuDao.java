package snod.com.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 角色与菜单对应关系
 * @author lvjj
 */
public interface SysRoleMenuDao  {
	
	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);

	/**
	 * 根据菜单id 删除菜单关联角色信息
	 * @param menuId
	 */
	void deleteByMenuId(Long menuId);

	/**
	 * 根据角色id 批量添加角色与菜单关系
	 * @param roleId
	 * @param menuIdList
	 */
	void insertRoleAndRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") List<Long> menuIdList);
}
