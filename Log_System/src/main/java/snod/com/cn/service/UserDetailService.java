package snod.com.cn.service;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.hutool.core.util.StrUtil;
import snod.com.cn.entity.SecurityUser;
import snod.com.cn.entity.SysMenu;
import snod.com.cn.entity.SysUser;

@Configuration
public class UserDetailService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
    	
        // 查询用户信息，这里没有设计账号表，用户表和账号公用同一张表，这里的username可以理解为账号名称，不是用户名称
        SysUser user = sysUserService.querySysUserInfo(username);
//    	SysUser user=new SysUser();
//        user.setUsername(username);
//        user.setPassword(this.passwordEncoder.encode("123456"));
        if(user==null) {
    		throw new UsernameNotFoundException("Username not found");
      	}
        // 输出加密后的密码
        System.out.println("123456加密后的密码---->"+this.passwordEncoder.encode("123456"));
        Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(getUserPermissions(user.getUserId()).toArray(new String[0]));
        return new SecurityUser(user.getUserId(),user.getShopId(),username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), authorities);
    }
    private Set<String> getUserPermissions(Long userId) {
		List<String> permsList;

		//系统管理员，拥有最高权限
		if(userId == 1){
			List<SysMenu> menuList = sysMenuService.listMenuAndBtn();


			permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
		}else{
			permsList = sysUserService.queryAllPerms(userId);
		}


		Set<String> permsSet = permsList.stream().flatMap((perms)->{
					if (StrUtil.isBlank(perms)) {
						return null;
					}
					return Arrays.stream(perms.trim().split(","));
				}
		).collect(Collectors.toSet());
		return permsSet;
	}
}
