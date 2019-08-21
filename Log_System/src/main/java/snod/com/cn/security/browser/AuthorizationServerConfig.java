package snod.com.cn.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import snod.com.cn.security.token.CustomizeRedisTokenStore;
import snod.com.cn.service.UserDetailService;

/**
 * @author MrBird
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	@Autowired
	private UserDetailService userDetailService;

	@Value("${security.oauth2.client.client-id}")
	String clientID;

	@Value("${security.oauth2.client.client-secret}")
	String clientSecret;

	@Bean
	public TokenStore tokenStore() {
		CustomizeRedisTokenStore tokenStore = new CustomizeRedisTokenStore(redisConnectionFactory);
		tokenStore.setPrefix("3nod_" + "oauth:");// access_token key
		return tokenStore;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm("oauth2-resources") // code授权添加
				.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()") // allow check token
				.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(clientID)// 配置文件clientID
				.secret(clientSecret)// 配置文件clientSecret密码
				.authorizedGrantTypes("authorization_code", "client_credentials", "password", "implicit",
						"refresh_token")
				.scopes("all").accessTokenValiditySeconds(1200)// 设置失效时间
				.refreshTokenValiditySeconds(50000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())// access_token保存到redis缓存中
				.userDetailsService(userDetailService);
	}

}
