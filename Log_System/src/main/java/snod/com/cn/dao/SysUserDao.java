package snod.com.cn.dao;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import snod.com.cn.entity.SysUser;

public interface SysUserDao {

	SysUser querySysUserInfo(String username);
	
	List<String> queryAllPerms(Long userId);

	SysUser getSysUserById(Long userId);
	
}
