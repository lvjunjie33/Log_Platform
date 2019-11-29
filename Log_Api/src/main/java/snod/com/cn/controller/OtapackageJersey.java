package snod.com.cn.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import snod.com.cn.entity.LogOtaUpgrade;
import snod.com.cn.service.OtaPackageService;
import snod.com.cn.utils.EcpStackTrace;
import snod.com.cn.utils.ResultInfo;
import snod.com.cn.utils.ResultTools;
import snod.com.cn.utils.VersionCheckUtil;
@Component
@Path("/OtaUpdater")
public class OtapackageJersey {
	
	private final static Logger logger = LoggerFactory.getLogger(OtapackageJersey.class);
	@Autowired
	private OtaPackageService otaPackageService;
	


	/**
	 * ota 获取升级包信息
	 * @param email 用户对象
	 * @param passwd 密码
	 */	 
	 @GET 
	 @Path("/androidt/{sn}/{version}/{product}/{country}/{language}")
//	 @Path("/androidt/{sn}")
//	 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	 @Produces({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
	public ResultInfo getPackageInfos(@PathParam("sn") String sn,@PathParam("version") String version,
			@PathParam("product") String product,@PathParam("country") String country,
			@PathParam("language") String language){
//	 public ResultInfo getPackageInfos(@PathParam("sn") String sn){
		 Map<String,Object> data=new HashMap<String,Object>();
		 data.put("sn", sn);
		 data.put("version", version);
		 data.put("product", product);
		 data.put("country", country);
		 data.put("language", language);
		 return ResultTools.result(ResultTools.ERROR_CODE_0, null,data);
	}
	
	/**
	 * ota 获取升级包信息
	 * @param email 用户对象
	 * @param passwd 密码
	 */	 
	
	 @GET 
	 @Path("/android")
//	 @Path("/android/{sn}/{version}/{product}/{country}/{language}")
//	 @Path("/android/{sn}/{version}")
//	 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	 @Produces({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
//	public void getPackageInfo(@Context HttpServletResponse response, @PathParam("sn") String sn,@PathParam("version") String version,
//			@PathParam("product") String product,@PathParam("country") String country,
//			@PathParam("language") String language){
	 public ResultInfo getPackageInfo(@Context HttpServletRequest request,@Context HttpServletResponse response, @QueryParam("sn") String sn,@QueryParam("version") String version) {				
//	 public ResultInfo getPackageInfo(){0
		 String sns=request.getParameter("sn");
		try {
 			List<LogOtaUpgrade> list=otaPackageService.queryOtaPackage(sn);
 			if(list!=null) {
 				if(list.size()>0) {
//			    	List<PackageInfo> endTimeTempList = new ArrayList<PackageInfo>(list);
//			    	endTimeTempList=ListUtil.sortListPackageInfo(endTimeTempList);
			    	if(VersionCheckUtil.verCheck(version,list.get(0).getVersion())) {
				    	String originalName= URLEncoder.encode(list.get(0).getPackageName(),"utf8");
				    	response.setHeader("OtaPackageLength",list.get(0).getPackeageSize()+"");
				    	response.setHeader("OtaPackageName", originalName);
				    	response.setHeader("OtaPackageVersion", list.get(0).getVersion());
				    	response.setHeader("OtaPackageUri", "http://"+"/OtaUpdater/downLoadPackage?sn="+sn);
				    	
			    	}
 				}
 			}
 			
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
		}
		 return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
	}
	 
	 
	 /**
		 * ota 获取升级包信息
		 * @param email 用户对象
		 * @param passwd 密码
		 */	 
		
		 @POST
//		 @Path("/android/{sn}/{version}/{product}/{country}/{language}")
		 @Path("/android/post")
		 @Consumes(MediaType.APPLICATION_JSON)
		 @Produces({MediaType.TEXT_HTML,MediaType.APPLICATION_JSON})
//		public void getPackageInfo(@Context HttpServletResponse response, @PathParam("sn") String sn,@PathParam("version") String version,
//				@PathParam("product") String product,@PathParam("country") String country,
//				@PathParam("language") String language){
		 public ResultInfo getPackageInfoPost(@Context HttpServletResponse response,Map<String,String> map) {				
//		 public ResultInfo getPackageInfo(){0
			try {
	 			List<LogOtaUpgrade> list=otaPackageService.queryOtaPackage(map.get("sn"));
	 			if(list!=null) {
	 				if(list.size()>0) {
//				    	List<PackageInfo> endTimeTempList = new ArrayList<PackageInfo>(list);
//				    	endTimeTempList=ListUtil.sortListPackageInfo(endTimeTempList);
				    	if(VersionCheckUtil.verCheck(map.get("version"),list.get(0).getVersion())) {
					    	String originalName= URLEncoder.encode(list.get(0).getPackageName(),"utf8");
					    	response.setHeader("OtaPackageLength",list.get(0).getPackeageSize()+"");
					    	response.setHeader("OtaPackageName", originalName);
					    	response.setHeader("OtaPackageVersion", list.get(0).getVersion());
					    	response.setHeader("OtaPackageUri", "http://"+"/OtaUpdater/downLoadPackage?sn="+map.get("sn"));
					    	
				    	}
	 				}
	 			}
	 			
			}catch(Exception e) {
				logger.error(EcpStackTrace.getExceptionStackTrace(e));
			}
			 return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		}
}
