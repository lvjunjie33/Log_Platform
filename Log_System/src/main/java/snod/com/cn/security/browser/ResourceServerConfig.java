package snod.com.cn.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import snod.com.cn.security.code.ValidateCodeFilter;
import snod.com.cn.security.handler.MyAuthenticationAccessDeniedHandler;
import snod.com.cn.security.handler.MyLogOutSuccessHandler;
import snod.com.cn.security.smscode.SmsAuthenticationConfig;
import snod.com.cn.security.smscode.SmsCodeFilter;

/**
 * @author lvjj
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
    @Autowired
    private MyAuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    
    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Autowired
    private SmsCodeFilter smsCodeFilter;
    
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;
    
    @Autowired
    private MyLogOutSuccessHandler logOutSuccessHandler;
    
   
    
    @Override
	public void configure(HttpSecurity http) throws Exception {
    	 //配置自定义过滤器 增加post json 支持
//            http.addFilterAt(UserAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class)
    	http.exceptionHandling()
        .accessDeniedHandler(authenticationAccessDeniedHandler)//权限不足自定义处理
        .and()
    	.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
            .addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class)// 添加短信验证码校验过滤器
//            .addFilterBefore(userAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)// 添加用户获取body数据
//                .formLogin() // 表单登录
//                     http.httpBasic() // HTTP Basic
//                    .loginPage("/login") // 登录跳转 URL
//                    .loginProcessingUrl("/login") // 处理表单登录 URL
//                    .successHandler(authenticationSucessHandler) // 处理登录成功
//                    .failureHandler(authenticationFailureHandler) // 处理登录失败
//                .and()
                    .authorizeRequests() // 授权配置
                    .antMatchers("/login", 
                    		"/login.html", 
                    		"/code/image",
                    		"/code/sms",
                    		"/v2/api-docs",
                    		"/configuration/ui", 
                    		"/swagger-resources", 
                    		"/configuration/security", 
                    		"/swagger-ui.html", 
                    		"/webjars/**",
                    		"/swagger-resources/configuration/ui",
                    		"/swagge‌​r-ui.html",
                    		"/session/invalid"
                    		).permitAll() // 无需认证的请求路径
                    .and()
                    .authorizeRequests()
                    .antMatchers("/**").authenticated()//所有请求都需要认证
                    .and()
                    .sessionManagement() // 添加 Session管理器
//                    .invalidSessionStrategy(customizeInvalidSessionStrategy)
//                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                    .invalidSessionUrl("/session/invalid") // Session失效后跳转到这个链接
                    .and()
                    .logout()
                    .logoutUrl("/signout")
                    // .logoutSuccessUrl("/signout/success")
                    .logoutSuccessHandler(logOutSuccessHandler)//退出登录自定义处理
                    .deleteCookies("JSESSIONID")
                .and()
                    .csrf().disable()
                .apply(smsAuthenticationConfig); // 将短信验证码认证配置加到 Spring Security 中
//            http.addFilterAt(UserAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
