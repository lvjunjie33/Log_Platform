package snod.com.cn.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import snod.com.cn.entity.SysUser;

@Configuration
public class UserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
    	
        // 模拟一个用户，替代数据库获取逻辑
        SysUser user = sysUserService.querySysUserInfo(username);
//        user.setUserName(username);
//        user.setPassword(this.passwordEncoder.encode("123456"));
        if(user==null) {
    		throw new UsernameNotFoundException("Username not found");
      	}
        // 输出加密后的密码
        System.out.println(user.getPassword());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase("mrbird", username)) {
        	authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        } else {
        	authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
        }
        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), authorities);
    }
}
