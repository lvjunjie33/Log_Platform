package snod.com.cn.receive;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import snod.com.cn.config.OSSFileConfig;
import snod.com.cn.config.RabbitMqConfig;
import snod.com.cn.constant.Constant;
import snod.com.cn.entity.EsLogFileInfo;
import snod.com.cn.entity.LogFileInfo;
import snod.com.cn.service.LogFileService;
import snod.com.cn.util.ContextUtil;

@Configuration
public class LogReceive {
	private final static Logger logger = LoggerFactory.getLogger(LogReceive.class);
	@Autowired
	private RabbitMqConfig rabbitMqConfig;
	@Autowired
	private OSSFileConfig ossFileConfig;
	@Autowired
	private LogFileService logService;
	 @Bean
	 public void receive() throws IOException, TimeoutException {
			//创建连接
			Connection connection = rabbitMqConfig.createConnection();
			//创建通道
			Channel channel = connection.createChannel();
			//声明队列
			channel.queueDeclare(Constant.QUEUE_LOG, false, false, false, null);

			/**
			 * 通道上允许的最大的未确认任务数.
			 * 达到顶峰后,mq会停止向该通道传递消息,直到有消息被确认或拒绝
			 * 0表示无限
			 *
			 * 1. 数量
			 * 2. 是否全局.如果是true,该设置将被整个通道使用,而不是单个消费者
			 * 不传,默认false,表示每个消费者的最大未确认数都为1,
			 * 为true,则表示多个消费者的最大未确认数之和为1.
			 *
			 * 如下设置.表示每个消费者的数是10,但多个消费者最大数之和不能超过15
			 * channel.basicQos(10,false);
			 * channel.basicQos(10,true);
			 */
			channel.basicQos(1);

//			//创建消费者
//			DefaultConsumer consumer = new DefaultConsumer(channel){
//				/**
//				 * 处理交付 ,也就是消费消息
//				 * @param consumerTag 消费者标签
//				 * @param envelope 信封类,包含了交付者标签(确认了,是每个消费者独立的id,递增,重启后重新累加)/是否重新交付/路由key等信息
//				 * @param properties 基本属性
//				 * @param body 消息
//				 * @throws IOException
//				 */
//				@Override
//				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//					String message = new String(body,"utf-8");
//
//					logger.info("id:{},接收到消息:{},consumerTag:{},envelope:{}",message,consumerTag,envelope);
//					
//
//					/**
//					 * 自动确认模式下,消息发送后自动默认为确认,
//					 * 可以提高吞吐量.
//					 * 手动确认可以限制每次的预先获取数量,但自动确认没有.
//					 * 如果消息过多,可能导致服务器宕机.
//					 * 所以需要保证生产者的消息的稳定性
//					 */
//
//					/**
//					 * 手动确认.
//					 * 1. 该交付标签.也就是消息id
//					 * 2. 是该消费者缓存的全部消息,还是只是当前消息.(也就是批量操作)
//					 * 还有就是.如果之前处理了交付标签为1,2,3的任务,还未确认,该次处理为4,那么将该multiple
//					 * 设置为true,将会将1,2,3和4都设置为确认
//					 */
//					channel.basicAck(envelope.getDeliveryTag(),false);
//
//					/**
//					 * 手动拒绝
//					 * 1.消息id
//					 * 2.是该消费者缓存的全部消息,还是只是当前消息.
//					 * 3.是否删除消息,还是让mq重发消息. true:重发
//					 */
////					channel.basicNack(envelope.getDeliveryTag(),false,true);
//					/**
//					 * 手动拒绝单个消息
//					 * 1.消息id
//					 * 3.是否删除消息,还是让mq重发消息. true:重发
//					 */
////					channel.basicReject(envelope.getDeliveryTag(),true);
//
//					logger.info("id:{},消息:{},任务执行完成",message);
//
//				}
//			};
			
			
			/**
			 * 在通道中注入消费者,
			 * 中间的参数为 是否自动确认(告诉队列,消费成功)
			 * 此处设置为手动提交
			 */
			channel.basicConsume(Constant.QUEUE_LOG, false,new LogExcute(channel));


			logger.info("id:{},消费者开始");
		}
	 
	 public class LogExcute extends DefaultConsumer {

			
		 private Logger logger = LoggerFactory.getLogger(LogExcute.class);


			

			private Channel channel;

			public LogExcute(Channel channel) {
				super(channel);
				this.channel = channel;
			}

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body){
				try {
					/**获取消息*/
					String message = new String(body, "utf-8");
					/**转成jsonObject*/
					JSONObject jsonObject = (JSONObject) JSONObject.parse(message);
					/**装成实体对象*/
					LogFileInfo logFileInfo=jsonObject.toJavaObject(LogFileInfo.class);
					/**解压压缩包*/
					logger.info("--------------解压文件------------------");
					List<File> fileList=null;
					/**
					 * 压缩包不存在直接退出，手动确认该条消息
					 * */
					if(!new File(logFileInfo.getFileLocalPath()+"\\"+logFileInfo.getFileName()).exists()) {
						channel.basicAck(envelope.getDeliveryTag(), false);
						return;
					}
					/**
					 * 目前只支持zip
					 * 原因rar压缩算法是不开源的，导致这块资料相对较少，对高版本没有兼容性；
					         为避免不必要的如此文件解压异常，实际项目中若需要上传文件执行导入等相关功能时，可以强制限定文件格式为非rar格式，
					         如zip开源算法，使用比较成熟就不会有这种尴尬的问题。
					 * */
					if(logFileInfo.getFileName().contains(".zip")) {
						ZipUtil.unzip(logFileInfo.getFileLocalPath()+"\\"+logFileInfo.getFileName(),CharsetUtil.systemCharset());
						fileList=FileUtil.loopFiles(logFileInfo.getFileLocalPath()+"\\"+logFileInfo.getFileName().substring(0,logFileInfo.getFileName().indexOf(".")),new FileFilter(){

							@Override
							public boolean accept(File pathname) {//文件过滤
								  if(pathname.getName().contains(".")) {
									  if(pathname.getName().endsWith(".txt") || pathname.getName().endsWith(".log")) {
							                return true;  
									  }else {  
							                return false;
								  	  }
								  }else {
									  return true;
								  }
							}					             
					   });
					}else {
						fileList=FileUtil.loopFiles(logFileInfo.getFileLocalPath(),null);
					} 
					/**获取解压后所有文件*/
					
					logger.info("--------------保存elasticSearch------------------");
					EsLogFileInfo esLogFileInfo=logService.saveEs(logFileInfo,fileList);
					logger.info("--------------上传文件服务器------------------");
					/**上传文件服务器*/
					if(esLogFileInfo!=null) {
						ossFileConfig.uploadFile(logFileInfo.getFilePath(), new FileInputStream(logFileInfo.getFileLocalPath()+"\\"+logFileInfo.getFileName()));
					}
					logger.info("--------------清空目录------------------");
					/**清空目录*/
					FileUtil.clean(logFileInfo.getFileLocalPath().substring(0,logFileInfo.getFileLocalPath().indexOf("file")));
					
					logger.info("接收到消息:{},consumerTag:{},envelope:{}", message, consumerTag, envelope);
					
					/**
					 * 自动确认模式下,消息发送后自动默认为确认, 可以提高吞吐量. 手动确认可以限制每次的预先获取数量,但自动确认没有. 如果消息过多,可能导致服务器宕机.
					 * 所以需要保证生产者的消息的稳定性
					 */

					/**
					 * 手动确认. 1. 该交付标签.也就是消息id 2. 是该消费者缓存的全部消息,还是只是当前消息.(也就是批量操作)
					 * 还有就是.如果之前处理了交付标签为1,2,3的任务,还未确认,该次处理为4,那么将该multiple 设置为true,将会将1,2,3和4都设置为确认
					 */
					channel.basicAck(envelope.getDeliveryTag(), false);

					/**
					 * 手动拒绝 1.消息id 2.是该消费者缓存的全部消息,还是只是当前消息. 3.是否删除消息,还是让mq重发消息. true:重发
					 
					 channel.basicNack(envelope.getDeliveryTag(),false,true);*/
					/**
					 * 手动拒绝单个消息 1.消息id 3.是否删除消息,还是让mq重发消息. true:重发
					 
					 channel.basicReject(envelope.getDeliveryTag(),true);*/

					logger.info("消息:{},任务执行完成", message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 }

}
