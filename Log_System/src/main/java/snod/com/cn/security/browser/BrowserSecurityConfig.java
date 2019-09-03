package snod.com.cn.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import snod.com.cn.security.code.UserAuthenticationFilter;
import snod.com.cn.security.code.ValidateCodeFilter;
import snod.com.cn.security.handler.MyAuthenticationAccessDeniedHandler;
import snod.com.cn.security.handler.MyAuthenticationFailureHandler;
import snod.com.cn.security.handler.MyAuthenticationSucessHandler;
import snod.com.cn.security.handler.MyLogOutSuccessHandler;
import snod.com.cn.security.smscode.SmsAuthenticationConfig;
import snod.com.cn.security.smscode.SmsCodeFilter;
import snod.com.cn.service.UserDetailService;
/**
 * @author lvjj
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyAuthenticationSucessHandler authenticationSucessHandler;

	@Autowired
	private MyAuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();// 密码加密方式
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public UserAuthenticationFilter loginAuthenticationFilter() {
		UserAuthenticationFilter filter = new UserAuthenticationFilter();
		try {
			filter.setAuthenticationManager(authenticationManagerBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		filter.setAuthenticationSuccessHandler(authenticationSucessHandler);// 认证成功处理类
		filter.setAuthenticationFailureHandler(authenticationFailureHandler);// 认证失败处理类
		return filter;
	}

}
