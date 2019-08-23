/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package snod.com.cn.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import snod.com.cn.common.PageParam;
import snod.com.cn.dao.SysRoleDao;
import snod.com.cn.dao.SysRoleMenuDao;
import snod.com.cn.dao.SysUserRoleDao;
import snod.com.cn.entity.SysRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.hutool.core.date.DateUtil;




/**
 * 角色
 * @author lvjj
 */
@Service("sysRoleService")
public class SysRoleService{
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleDao sysRoleDao;

	
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleAndRoleMenu(SysRole role) {
		role.setCreateTime(new Date());
		sysRoleDao.inserRoleInfo(role);
		if (role.getMenuIdList()!=null) {
			if(role.getMenuIdList().size()>0) {
				//保存角色与菜单关系
				sysRoleMenuDao.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
			}
		}
	}

	
	@Transactional(rollbackFor = Exception.class)
	public void updateRoleAndRoleMenu(SysRole role) {
		// 更新角色
		sysRoleDao.updateRoleInfo(role);
		//先删除角色与菜单关系
		sysRoleMenuDao.deleteBatch(new Long[]{role.getRoleId()});
		if(role.getMenuIdList()!=null) {
			if(role.getMenuIdList().size()>0) {
				//保存角色与菜单关系
				sysRoleMenuDao.insertRoleAndRoleMenu(role.getRoleId(), role.getMenuIdList());
			}
	
		}
	}

	
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		sysRoleDao.deleteBatch(roleIds);

		//删除角色与菜单关联
		sysRoleMenuDao.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleDao.deleteBatch(roleIds);
	}
	
	public List<Long> listRoleIdByUserId(Long userId) {
		return sysRoleDao.listRoleIdByUserId(userId);
	}


	public IPage<SysRole> queryRoleList(PageParam<SysRole> page, String roleName) {
		page.setRecords(sysRoleDao.queryRoleList(page.getSize(),page.getCurrent()-1,roleName));
		page.setTotal(sysRoleDao.queryCountRoleList(roleName));
		return page;
	}


	public List<SysRole> queryRoleListAll() {
		// TODO Auto-generated method stub
		return sysRoleDao.queryRoleListAll();
	}


	public SysRole getroleInfoById(Long roleId) {
		// TODO Auto-generated method stub
		return sysRoleDao.getroleInfoById(roleId);
	}

}
