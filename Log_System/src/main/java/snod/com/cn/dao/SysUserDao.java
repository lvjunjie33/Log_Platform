package snod.com.cn.dao;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.GrantedAuthority;

import snod.com.cn.entity.SysUser;
/**
 * 用户
 * @author lvjj
 */
public interface SysUserDao {

	SysUser querySysUserInfo(String username);
	
	List<String> queryAllPerms(Long userId);

	SysUser getSysUserById(Long userId);

	List<SysUser> queryList(String username);

	List<SysUser> queryList(@Param("size")long size, @Param("current")long current,  @Param("username")String username);

	long queryCountList(String username);

	void updateUserAndUserRole(@Valid SysUser user);

	void updatePasswordByUserId(@Param("userId")long  userId, @Param("newPassword")String newPassword);

	void saveUserAndUserRole(@Valid SysUser user);

	void deleteBatch(@Param("userIds")Long[] userIds, @Param("shopId")Long shopId);
	
}
