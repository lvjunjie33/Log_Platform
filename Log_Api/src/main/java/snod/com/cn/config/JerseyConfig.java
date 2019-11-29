package snod.com.cn.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
@Component
@ApplicationPath("/test")
public class JerseyConfig extends ResourceConfig{
	  public JerseyConfig() {
	        // 注册类的方式
	        // egister(Demo.class);
	        // 注册包的方式
	        packages("snod.com.cn.controller");
//	        // 注册JSON转换器
//	        register(JacksonJsonProvider.class);
//	        //注册文件上传模块
//	        register(MultiPartFeature.class);
	    }
}
