package snod.com.cn.jersey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.catalina.WebResource;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.URLUtil;

public class JerseyClientUtil {

    private static final int SUCCESS = 200;
    private static final int TOKEN_INVALID = 401;
    private static final String REQUEST_URL = "http://localhost:8066";
    
    /**
     * 带头部信息的get请求
     * 
     * @param url
     * @param headers
     * @return
     */
    public static String getWith(String path, Map<String, String> queryParam) {
        String readEntity = null;
        Client client = ClientBuilder.newClient();
        String s="";
//        for (Map.Entry<String, String> entry : queryParam.entrySet()) {
//        	s=s+"\""+entry.getKey()+"\",\""+entry.getValue()+"\",";
//        }
 
        WebTarget webTarget = client.target(REQUEST_URL).path(path).queryParam("sn",queryParam.get("sn")).queryParam("version",queryParam.get("version"));
//        String ss=URLUtil.decode(webTarget.getUri().toString());
//        System.out.println(ss);
        
        Response response = webTarget.request().get();   
//        MultivaluedMap<String, Object> h=response.getHeaders();
//        System.out.println(h.get("OtaPackageUri"));
        readEntity = response.readEntity(String.class);
        if (response.getStatus() == SUCCESS) {
            return readEntity;
        }
        return null;
    }
    /**
     * 带头部信息的get请求
     * 
     * @param url
     * @param headers
     * @return
     */
    public static String getWithHeader(String path, Map<String, Object> queryParam,
            MultivaluedMap<String, Object> headers) {
        String readEntity = null;
        Client client = ClientBuilder.newClient();
//        for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
        	path=path+"/"+queryParam.get("sn")+"/"+queryParam.get("version");
//        }
        WebTarget webTarget = client.target(REQUEST_URL).path(path);
       
        
        Response response = null;
        if(headers!=null) {
        	   response = webTarget.request().headers(headers).get();
        }else {
        	response = webTarget.request().get();
        }
        MultivaluedMap<String, Object> h=response.getHeaders();
        System.out.println(h.get("OtaPackageUri"));
        readEntity = response.readEntity(String.class);
        if (response.getStatus() == SUCCESS) {
            return readEntity;
        }
        return null;
    }

    /**
     * 带头部信息的post请求
     * 
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postWithHeader(String path, MultivaluedMap<String, Object> headers, Map<String, String> params){
        String readEntity = null;
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(REQUEST_URL).path(path);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).headers(headers)
                .post(Entity.entity(params, MediaType.APPLICATION_JSON_TYPE));
        readEntity = response.readEntity(String.class);
        if (response.getStatus() == SUCCESS) {
            return readEntity;
        } 
        return null;

    }
    

    /**
     * 带头部信息的delete请求
     * 
     * @param url
     * @param headers
     * @return
     * @throws HXServerException
     */
    public static String deleteWithHeader(String path, Map<String, Object> queryParam,
            MultivaluedMap<String, Object> headers){
        String readEntity = null;
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(REQUEST_URL).path(path);
        Response response = webTarget.request().headers(headers).delete();
        readEntity = response.readEntity(String.class);
        if (response.getStatus() == SUCCESS) {
            return readEntity;
        } 
        return null;
    }
    
    
    
    
    /**
     * 带头部信息的PUT请求
     * 
     * @param url
     * @param headers
     * @return
     * @throws HXServerException
     */
    public static String putWithHeader(String path, Map<String, Object> queryParam,
            MultivaluedMap<String, Object> headers) {
        
        String readEntity = null;
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(REQUEST_URL).path(path);
        Response response = webTarget
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .headers(headers)
                            .put(Entity.entity(queryParam, MediaType.APPLICATION_JSON_TYPE));
        readEntity = response.readEntity(String.class);
        if (response.getStatus() == SUCCESS) {
            return readEntity;
        }
        return null;
        
    }
    
    public static String get(String path, Map<String, Object> params){
    	WebTarget target=null;
		if (REQUEST_URL != null) {
			target = ClientBuilder.newClient().target(REQUEST_URL);
		}
		WebTarget pathTarget = target.path(path);
		Set<String> keys = params.keySet();
		for (String s : keys) {
			pathTarget = pathTarget.queryParam(s, params.get(s));
		}
		
		return pathTarget.request().get(String.class);
	}



}
	