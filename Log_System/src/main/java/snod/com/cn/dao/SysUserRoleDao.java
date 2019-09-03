/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package snod.com.cn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import snod.com.cn.entity.SysUserRole;

/**
 * 用户与角色对应关系.
 * @author lvjj
 */
public interface SysUserRoleDao  extends BaseMapper<SysUserRole> {
	
	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(@Param("roleIds") Long[] roleIds);

	/**
	 * 根据用户id删除用户与角色关系
	 * @param userId
	 */
	void deleteByUserId(Long userId);

	/**
	 * 根据用户id 批量添加用户角色关系
	 * @param userId
	 * @param roleIdList
	 */
	void insertUserAndUserRole(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);

	
	void deleteByUserIds(@Param("userIds")Long[] userIds);
}
