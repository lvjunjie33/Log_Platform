package snod.com.cn.service;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.hutool.core.collection.CollUtil;
import snod.com.cn.common.PageParam;
import snod.com.cn.dao.SysUserDao;
import snod.com.cn.dao.SysUserRoleDao;
import snod.com.cn.entity.SysUser;
import snod.com.cn.utils.SecurityUtils;

@Service
public class SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	public SysUser querySysUserInfo(String username) {
		return sysUserDao.querySysUserInfo(username);
	}


	public List<String> queryAllPerms(Long userId) {
		// TODO Auto-generated method stub
		return sysUserDao.queryAllPerms(userId);
	}


	public SysUser getSysUserById(Long userId) {
		// TODO Auto-generated method stub
		return sysUserDao.getSysUserById(userId);
	}


	public IPage<SysUser> queryList(PageParam<SysUser> page, String username) {
		page.setRecords(sysUserDao.queryList(page.getSize(),page.getCurrent()-1,username));
		page.setTotal(sysUserDao.queryCountList(username));
		return page;
	}


	public SysUser getByUserName(String username) {
		// TODO Auto-generated method stub
		return sysUserDao.querySysUserInfo(username);
	}


	public void updateUserAndUserRole(@Valid SysUser user) {
		//修改用户
		sysUserDao.updateUserAndUserRole(user);
		//删除用户角色关系
		sysUserRoleDao.deleteByUserId(user.getUserId());
		//保存用户角色关系
		sysUserRoleDao.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());
	}


	public void updatePasswordByUserId(Long userId, String newPassword) {
		sysUserDao.updatePasswordByUserId(userId,newPassword);
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveUserAndUserRole(@Valid SysUser user) {
		user.setCreateTime(new Date());
		user.setCreateUserId(SecurityUtils.getSecurityUser().getUserId());
		sysUserDao.saveUserAndUserRole(user);
		if(CollUtil.isEmpty(user.getRoleIdList())){
			return ;
		}
		//保存用户与角色关系
		sysUserRoleDao.insertUserAndUserRole(user.getUserId(), user.getRoleIdList());
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] userIds, Long shopId) {
		//删除用户信息
		sysUserDao.deleteBatch(userIds,shopId);
		//删除用户角色关系
		sysUserRoleDao.deleteByUserIds(userIds);
	}
}
