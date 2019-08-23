package snod.com.cn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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


	public List<String> queryAllPerms(Long userId) {
		// TODO Auto-generated method stub
		return sysUserDao.queryAllPerms(userId);
	}


	public SysUser getSysUserById(Long userId) {
		// TODO Auto-generated method stub
		return sysUserDao.getSysUserById(userId);
	}
}
