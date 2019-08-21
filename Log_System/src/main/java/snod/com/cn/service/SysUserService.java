package snod.com.cn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import snod.com.cn.dao.SysUserDao;
import snod.com.cn.entity.SysUser;

@Service
public class SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	
	public SysUser querySysUserInfo(String username) {
		return sysUserDao.querySysUserInfo(username);
	}
}
