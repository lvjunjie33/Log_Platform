package snod.com.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;

import snod.com.cn.entity.SysRole;

/**
 * 角色管理
 * @author lvjj
 */
public interface SysRoleDao{

	void deleteBatch(@Param("roleIds") Long[] roleIds);

	List<Long> listRoleIdByUserId(Long userId);

	List<SysRole> queryRoleList(@Param("size")long size, @Param("current")long current,  @Param("roleName")String roleName);

	List<SysRole> queryRoleListAll();

	SysRole getroleInfoById(Long roleId);

	void inserRoleInfo(SysRole role);

	void updateRoleInfo(SysRole role);

	long queryCountRoleList(String roleName);

}
