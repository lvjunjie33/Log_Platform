package snod.com.cn.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import snod.com.cn.config.OSSFileConfig;
import snod.com.cn.entity.LogFileInfo;
import snod.com.cn.entity.LogOtaUpgrade;
import snod.com.cn.service.OtaPackageService;
import snod.com.cn.utils.EcpStackTrace;
import snod.com.cn.utils.FileUtil;
import snod.com.cn.utils.ResultInfo;
import snod.com.cn.utils.ResultTools;
import snod.com.cn.utils.VersionCheckUtil;


@Api(tags= {"ota升级包接口"})
@RestController
@RequestMapping("/OtaUpdater")
public class OtapackageController {
	private final static Logger logger = LoggerFactory.getLogger(OtapackageController.class);
	@Autowired
	private OtaPackageService otaPackageService;
	@Autowired
	private OSSFileConfig ossFileConfig;
	/**
	 * ota 上传升级包测试接口
	 * @param email 用户对象
	 * @param passwd 密码
	 */
	@RequestMapping("/upgradePackageTest")
	@ApiOperation(notes="ota 上传升级包测试接口",value="ota 上传升级包测试接口",httpMethod="POST")
	 
	@ApiImplicitParams({
		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="version",value="版本号",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="product",value="产品名称",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="country",value="国家",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="language",value="语言",paramType="query",dataType="string",required=true),
	})
	public ResultInfo upgradePackageTest(HttpServletRequest request,
			@ApiParam(value ="升级包",required=true)MultipartFile content,String sn,
			String version,String product,String country,String language){
		try {
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR))+"/";
	        String month = String.valueOf(date.get(Calendar.MONTH)+1)+"/";
	        String filePath="ota/"+product+"/"+year+month+content.getOriginalFilename();
	        String fileName=content.getOriginalFilename();
	        long packageSize=content.getSize();
	        otaPackageService.saveOtaFile(fileName,product,version,country,sn,language,filePath,packageSize);
	        //上传到文件服务器
	    	ossFileConfig.uploadFile(filePath, content.getInputStream());
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	/**
	 * ota 获取升级包信息
	 * @param email 用户对象
	 * @param passwd 密码
	 */
	@RequestMapping("/android")
	@ApiOperation(notes="ota 获取升级包信息",value="ota 获取升级包信息",httpMethod="POST")
	 
	@ApiImplicitParams({
		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="version",value="版本号",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="product",value="产品名称",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="country",value="国家",paramType="query",dataType="string",required=true),
		@ApiImplicitParam(name="language",value="语言",paramType="query",dataType="string",required=true),
	})
	public void getPackageInfo(HttpServletRequest request,HttpServletResponse response, String sn,String version,String product,String country,String language){

		try {
 			List<LogOtaUpgrade> list=otaPackageService.queryOtaPackage(sn);
 			if(list!=null) {
 				if(list.size()>0) {
//			    	List<PackageInfo> endTimeTempList = new ArrayList<PackageInfo>(list);
//			    	endTimeTempList=ListUtil.sortListPackageInfo(endTimeTempList);
			    	if(VersionCheckUtil.verCheck(version,list.get(0).getVersion())) {
				    	String originalName= URLEncoder.encode(list.get(0).getPackageName(),"utf8");
				    	response.addHeader("OtaPackageLength",list.get(0).getPackeageSize()+"");
				    	response.addHeader("OtaPackageName", originalName);
				    	response.addHeader("OtaPackageVersion", list.get(0).getVersion());
				    	response.addHeader("OtaPackageUri", "http://"+request.getRequestURL().toString().split("\\/")[2]+"/OtaUpdater/downLoadPackage?sn="+sn);
			    	}
 				}
 			}
	    
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
		}
	}
	
	
	/**
	 * ota 升级包下载
	 * @param email 用户对象
	 * @param passwd 密码
	 */
	@RequestMapping("/downLoadPackage")
	@ApiOperation(notes="ota 升级包下载",value="ota 升级包下载",httpMethod="GET")
	public ResultInfo downLoadPackage(HttpServletRequest request,HttpServletResponse response,String sn){
		try {
	    	List<LogOtaUpgrade> list=otaPackageService.queryOtaPackage(sn);
//	    	List<PackageInfo> endTimeTempList = new ArrayList<PackageInfo>(list);
//	    	endTimeTempList=ListUtil.sortListPackageInfo(endTimeTempList);
	    	InputStream inputStream = ossFileConfig.downLoadFile(list.get(0).getPackagePath());
	    	FileUtil.getDownload(inputStream,request,response,list);
//	        //jdk7新特性，可以直接写到try()括号里面，java会自动关闭
//	        OutputStream outputStream = response.getOutputStream();
////	        //指明为下载
//	        response.setContentType("application/x-download");
//	        response.addHeader("Content-Disposition", "attachment;fileName=" + endTimeTempList.get(0).getPackageName());   // 设置文件名
//	        response.setContentLengthLong(endTimeTempList.get(0).getPackeageSize());
////	        //把输入流copy到输出流
//	        IOUtils.copy(inputStream, outputStream);
//	        outputStream.flush();
//	        outputStream.close();
//	        inputStream.close();
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		} catch (Exception e) {
	       	logger.error(EcpStackTrace.getExceptionStackTrace(e));
       		return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
        }
	}
}
