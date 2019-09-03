package snod.com.cn.config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import cn.hutool.core.util.NumberUtil;
import snod.com.cn.constant.Constant;
@Configuration
public class RabbitMqConfig {
	private final static Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);
	 @Value(value="${spring.rabbitmq.host}")
	 private  String host;
	 @Value(value="${spring.rabbitmq.port}")
	 private  String port;
	 @Value(value="${spring.rabbitmq.virtualHost}")
	 private  String virtualHost;
	 @Value(value="${spring.rabbitmq.username}")
	 private  String username;
	 @Value(value="${spring.rabbitmq.password}")
	 private  String password;

	 @Bean
	public Connection createConnection() throws IOException, TimeoutException {
		//创建连接工厂
		ConnectionFactory connectionFactory = new ConnectionFactory();
		//设置主机或ip
		connectionFactory.setHost(host);
		//设置端口
		connectionFactory.setPort(NumberUtil.parseInt(port));
		//rabbitmq 权限标识
		connectionFactory.setVirtualHost(virtualHost);
		//用户
		connectionFactory.setUsername(username);
		//密码
		connectionFactory.setPassword(password);
		//创建连接
		Connection connection = connectionFactory.newConnection();
		return connection;
	}
	public void send(String msg)  {
		try {
		Connection connection=createConnection();
		Channel channel = connection.createChannel();
		/**
		 * 声明一个队列.该方法是幂等的.只有队列不存在时才会被创建
		 * 1.队列名
		 * 2.是否持久的
		 * 3.是否独占的(注册到该connection)
		 * 4. 是否自动删除(服务器将在不再使用时删除它)
		 * 5.Map<String, Object> 该队列的其他属性
		 */
		channel.queueDeclare(Constant.QUEUE_LOG, false, false, false, null);


		/**
		 * 将该通道声明为生产者确认模式
		 */
		channel.confirmSelect();

		//发送消息
		/**
		 * 1. 将消息发布到... (后面的发布订阅模式)
		 * 2. 路由key(此处是队列名)
		 * 3. 其他属性, route header(路由头)等
		 * 4. 消息
		 */
//		String message = "zhengxing..";

		//获取下一个发送的消息id,可用来在确认回调时判断是哪条消息没有成功
//		long nextPublishSeqNo = channel.getNextPublishSeqNo();

		channel.basicPublish("", Constant.QUEUE_LOG, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
		/**
		 * 如果是持久化的消息队列,需要如下传输消息
		 */
//		channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());


		//confirmSelect，进入confirm消息确认模式，确认方式：1、异步ConfirmListener；2、同步waitForConfirms 
//        channel.confirmSelect();
		//同步批量等待确认结果,就算只有一个失败了,也会返回false.
//		channel.waitForConfirmsOrDie();

		//开启监听器,异步等待确认结果
		channel.addConfirmListener(new ConfirmListener() {
			//成功确认结果
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				//此处的multiple和消费者确认中我们自己传递的参数是同一种,表示是否批量
				logger.info("发送成功.deliveryTag:{},multiple:{}",deliveryTag,multiple);
			}

			//未确认结果
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				logger.info("发送失败.deliveryTag:{},multiple:{}",deliveryTag,multiple);
			}
		});
		logger.info("发送消息:{},", msg);
		}catch(IOException e) {
			logger.error(e.getMessage(),e);
		} catch (TimeoutException e) {
			logger.error(e.getMessage(),e);
		}
	}
}
