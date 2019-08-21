package snod.com.cn.dao;

import snod.com.cn.entity.SysUser;

public interface SysUserDao {

	SysUser querySysUserInfo(String username);
	
}
