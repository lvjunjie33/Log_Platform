/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package snod.com.cn.utils;



import lombok.experimental.UtilityClass;
import snod.com.cn.common.UnauthorizedExceptionBase;
import snod.com.cn.entity.SecurityUser;
import snod.com.cn.entity.SysUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	    
	/**
	 * 获取SysUser
	 */
	public SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		return (SecurityUser) principal;
	}
}
