package snod.com.cn.jersey;


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.catalina.WebResource;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.URLUtil;
import io.netty.util.CharsetUtil;

public class TestJersey {
	
	
	
	public static void main(String[] args) {
		Map<String,String> param=new HashMap<String,String>();
//		Map<String,String> param=new HashMap<String,String>();
		param.put("sn", "1122232425268812");
		param.put("version", "1.0.0");
//		param.put("product", "3NOD-L1");
//		param.put("country", "US");
//		param.put("language", "cn");
//		String s=JerseyClientUtil.getWithHeader("/test/OtaUpdater/android", param,null);
//		String s=JerseyClientUtil.postWithHeader("/test/OtaUpdater/android/post", null, param);
//		String s=JerseyClientUtil.get("/test/OtaUpdater/android", param);
//		String s=JerseyClientUtil.getWith(URLUtil.encode("/test/OtaUpdater/android?sn=123&version=123"),param);
		String s=JerseyClientUtil.getWith("/test/OtaUpdater/android",param);
		System.out.println(s);
	}
}
