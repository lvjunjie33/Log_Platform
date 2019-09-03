package snod.com.cn.config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import cn.hutool.core.util.NumberUtil;
@Configuration
public class RabbitMqConfig {
	
	 @Value(value="${spring.rabbitmq.host}")
	 private String host;
	 @Value(value="${spring.rabbitmq.port}")
	 private String port;
	 @Value(value="${spring.rabbitmq.virtualHost}")
	 private String virtualHost;
	 @Value(value="${spring.rabbitmq.username}")
	 private String username;
	 @Value(value="${spring.rabbitmq.password}")
	 private String password;

	 @Bean
	public Connection createConnection() throws IOException, TimeoutException {
		//创建连接工厂
		ConnectionFactory connectionFactory = new ConnectionFactory();
		//设置主机或ip
		connectionFactory.setHost(host);
		//设置端口
		connectionFactory.setPort(NumberUtil.parseInt(port));
		
		connectionFactory.setVirtualHost(virtualHost);
		
		connectionFactory.setUsername(username);
		
		connectionFactory.setPassword(password);
		//创建连接
		Connection connection = connectionFactory.newConnection();
		return connection;
	}
	
}
