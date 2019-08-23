package snod.com.cn.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUser extends User{
	/**
	 * 用户ID
	 */
	@Getter
	private Long userId;

	/**
	 * 租户ID
	 */
	@Getter
	private Long shopId;
	
	public SecurityUser(Long userId,Long shopId,String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
		this.shopId = shopId;
	}	
}
