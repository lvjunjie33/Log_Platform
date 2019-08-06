package snod.com.cn.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
@Configuration
public class OSSFileUtil implements EnvironmentAware{
	
	private static RelaxedPropertyResolver propertyResolver;
	private static String endpoint;
	private static String accessKeyId;
	private static String accessKeySecret;
	private static String bucket;
	
	public void setEnvironment(Environment env) {
	        propertyResolver = new RelaxedPropertyResolver(env, "aliyun.upload.");
	        endpoint=propertyResolver.getProperty("endpoint");
			accessKeyId=propertyResolver.getProperty("accessKeyId");
			accessKeySecret=propertyResolver.getProperty("accessKeySecret");
			bucket=propertyResolver.getProperty("bucket");
	}
	public static void uploadFile(String FileName,InputStream fileInputStream) throws FileNotFoundException {
		// Endpoint以杭州为例，其它Region请按实际情况填写。
//		String endpoint = propertyResolver.getProperty("endpoint");
		
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//		String accessKeyId = propertyResolver.getProperty("accessKeyId");
//		String accessKeySecret = propertyResolver.getProperty("accessKeySecret");

		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		// 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
		ossClient.putObject(bucket, FileName, fileInputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
	}
	
	public static InputStream downLoadFile(String FileName) {
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		OSSObject ossObject = ossClient.getObject(bucket, FileName);
		return ossObject.getObjectContent();
	}
}
