package snod.com.cn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.http.HttpResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import snod.com.cn.entity.LogtxInfo;
import snod.com.cn.entity.PackageInfo;
import snod.com.cn.entity.vo.FileVo;
import snod.com.cn.entity.vo.SearchVo;
import snod.com.cn.service.Log_tx_Service;
import snod.com.cn.utils.EcpStackTrace;
import snod.com.cn.utils.FileUtil;
import snod.com.cn.utils.FtpUtil;
import snod.com.cn.utils.IPUtil;
import snod.com.cn.utils.ListUtil;
import snod.com.cn.utils.OSSFileUtil;
import snod.com.cn.utils.ResultInfo;
import snod.com.cn.utils.ResultTools;
import snod.com.cn.utils.StringUtil;

@Api(tags= {"日志接口"})
@RestController
@RequestMapping("/tx_log")
public class Log_tx_Controller {
	
	private final static Logger logger = LoggerFactory.getLogger(Log_tx_Controller.class);
	
	@Autowired
	private Log_tx_Service logService;
	
	
//	/**
//	 * es test
//	 * @param email 用户对象
//	 * @param passwd 密码
//	 */
//	@RequestMapping("/esTest")
//	@ApiOperation(notes="esTest",value="esTest",httpMethod="POST")
//	 
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="email",value="邮箱",paramType="query",dataType="string"),
//		@ApiImplicitParam(name="passwd",value="密码",paramType="query",dataType="string")
//	})
//	public ResultInfo esTest(String email,String passwd){
//		try {
////			logger.info("981723773aaa");
////			System.out.println(1/0);
//			Map<String,Object>result=new HashMap<String,Object>();
////			LogtxInfo resultData=logService.addTest(email);
////			result.put("result",resultData);
//			return ResultTools.result(ResultTools.ERROR_CODE_0, null, result);	
//		}catch(Exception e) {
//			logger.error(EcpStackTrace.getExceptionStackTrace(e));
////			logger.info(e.getMessage());
//			return ResultTools.result(ResultTools.ERROR_CODE_404, e.getMessage(), null);
//		}
//	}
	

	
	
	
	/**
	 * 上传日志文件测试接口(HTTP上传大文件)
	 * @param deviceName 设备名称
	 * @param logType 日志类型(1,错误日志；2，操作日志，3，系统日志；...)
	 * @param fileType 文件类型(1,压缩包；2,文本文件；...)
	 * @param SN SN
	 */
	@RequestMapping("/logBigFileUpLoadHttpTest")
	@ApiOperation(notes="上传日志文件测试接口(HTTP上传大文件)",value="上传日志文件测试接口(HTTP上传大文件)",httpMethod="POST")
	 
	@ApiImplicitParams({
		@ApiImplicitParam(name="form",value="form",paramType="query",dataType="FileVo",required=true),
	})
	public ResultInfo logBigFileUpLoadHttpTest(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "data", required = false)@ApiParam(value ="data",required=true)MultipartFile content,FileVo form){
		try {
			//IP
			String requestIp=IPUtil.getIP(request);
			//端口
			int remote=request.getRemotePort();
			Map<String,Object> map=logService.realUpload(form, content,requestIp,remote);
			if("1".equals(map.get("flag"))) {
				 return ResultTools.result(ResultTools.ERROR_CODE_8001, null,null);
			}
			else {	
				 return ResultTools.result(ResultTools.ERROR_CODE_8002, null,null);
			}
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	/**
	 * 上传日志文件接口(HTTP普通文件上传)
	 * @param deviceName 设备名称
	 * @param logType 日志类型(1,错误日志；2，操作日志，3，系统日志；...)
	 * @param fileType 文件类型(1,压缩包；2,文本文件；...)
	 * @param SN SN
	 */
	@RequestMapping("/logFileUpLoadHttp")
	@ApiOperation(notes="上传日志文件接口(HTTP普通文件上传)",value="上传日志文件接口(HTTP普通文件上传)",httpMethod="POST")
	public ResultInfo logFileUpLoadHttp(HttpServletRequest request
			){
		try {
			String sn=request.getHeader("sn");
			String logType=request.getHeader("logType");
			String fileType=request.getHeader("fileType");
			String deviceName=request.getHeader("deviceName");
			Map<String, MultipartFile> fileMap = new LinkedHashMap<>(0);
		     if (request instanceof MultipartHttpServletRequest) {
		          fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		      }
		     int logTypeInt = 0;
		     int fileTypeInt = 0;
		     if(logType!=null && fileType!=null) {
			     if(StringUtil.isNumeric(logType) && StringUtil.isNumeric(fileType)) {
			    	 logTypeInt=Integer.parseInt(logType);
			    	 fileTypeInt=Integer.parseInt(fileType);
			     }
		     }
		    MultipartFile uploadFile=fileMap.get("uploadFile");//会议记录文件
			//IP
			String requestIp=IPUtil.getIP(request);
			//端口
			int remote=request.getRemotePort();
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR))+"/";
	        String month = String.valueOf(date.get(Calendar.MONTH)+1)+"/";
	        String FileName="device_log/"+deviceName+"/"+year+month+uploadFile.getOriginalFilename();
	        logService.saveFile(uploadFile.getOriginalFilename(),deviceName,requestIp,remote,logTypeInt,fileTypeInt,sn,FileName);
	        //上传到文件服务器
	        OSSFileUtil.uploadFile(FileName, uploadFile.getInputStream());
	        //上传文件本地-------------------------------------------------------------------------
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	/**
	 * 上传日志文件测试接口(HTTP普通文件上传)
	 * @param deviceName 设备名称
	 * @param logType 日志类型(1,错误日志；2，操作日志，3，系统日志；...)
	 * @param fileType 文件类型(1,压缩包；2,文本文件；...)
	 * @param SN SN
	 */
	@RequestMapping("/logFileUpLoadHttpTest")
	@ApiOperation(notes="上传日志文件测试接口(HTTP普通文件上传)",value="上传日志文件测试接口(HTTP普通文件上传)",httpMethod="POST")
	public ResultInfo logFileUpLoadHttpTest(HttpServletRequest request
			){
		try {
			//接收头部参数
			String sn=request.getHeader("sn");
			String logType=request.getHeader("logType");
			String fileType=request.getHeader("fileType");
			String deviceName=request.getHeader("deviceName");
			Map<String, MultipartFile> fileMap = new LinkedHashMap<>(0);
		     if (request instanceof MultipartHttpServletRequest) {
		          fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		      }
			//IP
			int logTypeInt = 0;
		    int fileTypeInt = 0;
		    if(logType!=null && fileType!=null) {
			     if(StringUtil.isNumeric(logType) && StringUtil.isNumeric(fileType)) {
			    	 logTypeInt=Integer.parseInt(logType);
			    	 fileTypeInt=Integer.parseInt(fileType);
			     }
		     }
			String requestIp=IPUtil.getIP(request);
			//端口
			int remote=request.getRemotePort();
			MultipartFile content=fileMap.get("uploadFile");//会议记录文件
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR))+"/";
	        String month = String.valueOf(date.get(Calendar.MONTH)+1)+"/";
	        String FileName="device_log/"+deviceName+"/"+year+month+content.getOriginalFilename();
	        logService.saveFile(content.getOriginalFilename(),deviceName,requestIp,remote,logTypeInt,fileTypeInt,sn,FileName);
	        //上传到文件服务器
	        OSSFileUtil.uploadFile(FileName, content.getInputStream());
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}
	
	/**
	 * 上传日志文件测试接口(FTP上传本地)
	 * @param deviceName 设备名称
	 * @param logType 日志类型(1,错误日志；2，操作日志，3，系统日志；...)
	 * @param fileType 文件类型(1,压缩包；2,文本文件；...)
	 * @param SN SN
	 */
	@RequestMapping("/logFileUpLoadFtpTest")
	@ApiOperation(notes="上传日志文件测试接口(FTP上传本地)",value="上传日志文件测试接口(FTP上传本地)",httpMethod="POST")
	 
	@ApiImplicitParams({
//		@ApiImplicitParam(name="deviceName",value="设备名称",paramType="query",dataType="string",required=true),
//		@ApiImplicitParam(name="logType",value="日志类型(1,错误日志；2，操作日志，3，系统日志；...)",paramType="query",dataType="string",required=true),
//		@ApiImplicitParam(name="fileType",value="文件类型(1,压缩包；2,文本文件；...)",paramType="query",dataType="string",required=true),
//		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
		
	})
	public ResultInfo logFileUpLoadFtpTest(HttpServletRequest request,
			@ApiParam(value ="日志文件",required=true)MultipartFile content
//			String deviceName,String logType,String fileType,String sn
			){
		try {
			String sn=request.getHeader("sn");
			String logType=request.getHeader("logType");
			String fileType=request.getHeader("fileType");
			String deviceName=request.getHeader("deviceName");
			//IP
			String requestIp=IPUtil.getIP(request);
			//端口
			int remote=request.getRemotePort();
			int logTypeInt = 0;
		    int fileTypeInt = 0;
		    if(logType!=null && fileType!=null) {
			     if(StringUtil.isNumeric(logType) && StringUtil.isNumeric(fileType)) {
			    	 logTypeInt=Integer.parseInt(logType);
			    	 fileTypeInt=Integer.parseInt(fileType);
			     }
		     }
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR))+"/";
	        String month = String.valueOf(date.get(Calendar.MONTH)+1)+"/";
	        String FilePath="/device_log/"+deviceName+"/"+year+month;
	        String FileName=content.getOriginalFilename();
	        logService.saveFile(FileName,deviceName,requestIp,remote,logTypeInt,fileTypeInt,sn,FilePath+FileName);
	        //FTP的方式上传到文件服务器
	        FtpUtil.uploadToFtp(FileName, content.getInputStream(),FilePath);
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		}catch(Exception e) {
			logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
		}
	}

	
	 /**
     * 下载日志文件(HTTP下载OSS)
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
   */
	@RequestMapping("logFileDownLoadHttpOSSTest")
	@ApiOperation(notes="下载日志文件(HTTP下载OSS)",value="下载日志文件(HTTP下载OSS)",httpMethod="POST")
	@ApiImplicitParams({
		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
	})
    public ResultInfo logFileDownLoadHttpOSSTest(HttpServletRequest request,HttpServletResponse response,String sn) throws IOException{
		try{
			Page<LogtxInfo> page=logService.queryLogFile(sn);
	    	List<LogtxInfo> list=page.getContent();
	    	List<LogtxInfo> endTimeTempList = new ArrayList<LogtxInfo>(list);
	    	endTimeTempList=ListUtil.sortList(endTimeTempList);
	        InputStream inputStream = OSSFileUtil.downLoadFile(endTimeTempList.get(0).getLogFilePath());
	        OutputStream outputStream = response.getOutputStream();
	        byte[] btImg = FileUtil.readInputStream(inputStream);//得到二进制数据
	        //指明为下载
	        response.setContentType("application/x-download");     
	        response.setContentLengthLong(btImg.length);
	        response.addHeader("Content-Disposition", "attachment;fileName=" + endTimeTempList.get(0).getLogFileName());   // 设置文件名
	        //把输入流copy到输出流
	        IOUtils.copy(inputStream, outputStream);
	        outputStream.flush();
	        outputStream.close();
	        inputStream.close();
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		} catch (Exception e) {
	       	logger.error(EcpStackTrace.getExceptionStackTrace(e));
       		return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
        }
	}
	
	 /**
     * 下载日志文件(HTTP下载本地)
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
   */
	@RequestMapping("logFileDownLoadHttpTest")
	@ApiOperation(notes="下载日志文件测试接口(HTTP下载本地)",value="下载日志文件测试接口(HTTP下载本地)",httpMethod="POST")
	@ApiImplicitParams({
		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
	})
    public ResultInfo logFileDownLoadHttpTest(HttpServletRequest request,HttpServletResponse response,String sn) throws IOException{
		try{
			Page<LogtxInfo> page=logService.queryLogFile(sn);
	    	List<LogtxInfo> list=page.getContent();
	    	List<LogtxInfo> endTimeTempList = new ArrayList<LogtxInfo>(list);
	    	endTimeTempList=ListUtil.sortList(endTimeTempList);
	    	String []str=endTimeTempList.get(0).getLogFilePath().split("/");
	    	String folderStr="";
	    	for(int i=0;i<str.length;i++) {
	    		if(i==str.length-1) {
	    			continue;
	    		}
	    		folderStr=folderStr+"/"+str[i];
	    	}
	    	String folder = request.
	                getServletContext().getRealPath(folderStr);
	        //jdk7新特性，可以直接写到try()括号里面，java会自动关闭
	        InputStream inputStream = new FileInputStream(new File(folder,endTimeTempList.get(0).getLogFileName()));
	        OutputStream outputStream = response.getOutputStream();
	        //指明为下载
	        response.setContentType("application/x-download");
	        response.addHeader("Content-Disposition", "attachment;fileName=" + endTimeTempList.get(0).getLogFileName());   // 设置文件名
	        //把输入流copy到输出流
	        IOUtils.copy(inputStream, outputStream);
	        outputStream.flush();
	        return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
		} catch (Exception e) {
	       	logger.error(EcpStackTrace.getExceptionStackTrace(e));
       		return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
        }
	}
	
	
	
	
	/**
	 * 下载日志文件(FTP下载本地)
	 * @param deviceName 设备名称
	 * @param logType 日志类型(1,错误日志；2，操作日志，3，系统日志；...)
	 * @param fileType 文件类型(1,压缩包；2,文本文件；...)
	 * @param SN SN
	 */
	@RequestMapping("/logFileDownLoadFtpTest")
	@ApiOperation(notes="下载日志文件测试接口(FTP下载本地)",value="下载日志文件测试接口(FTP下载本地)",httpMethod="POST")
	 
	@ApiImplicitParams({
		@ApiImplicitParam(name="sn",value="sn",paramType="query",dataType="string",required=true),
	})
	public ResultInfo logFileDownLoadFtpTest(HttpServletResponse response,String sn){
        try{
        	Page<LogtxInfo> page=logService.queryLogFile(sn);
        	List<LogtxInfo> list=page.getContent();
        	List<LogtxInfo> endTimeTempList = new ArrayList<LogtxInfo>(list);
        	endTimeTempList=ListUtil.sortList(endTimeTempList);
            //jdk7新特性，可以直接写到try()括号里面，java会自动关闭
            InputStream inputStream = FtpUtil.downloadFile(endTimeTempList.get(0).getLogFilePath());
            OutputStream outputStream = response.getOutputStream();
            //指明为下载
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + endTimeTempList.get(0).getLogFileName());   // 设置文件名
            //把输入流copy到输出流
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
            return ResultTools.result(ResultTools.ERROR_CODE_0, null,null);
        } catch (Exception e) {
        	logger.error(EcpStackTrace.getExceptionStackTrace(e));
			return ResultTools.result(ResultTools.ERROR_CODE_404, EcpStackTrace.getExceptionStackTrace(e), null);
        }
	}
	
	

	
	/**
	 * 查询
	 * @param email 用户对象
	 * @param passwd 密码
	 */
	@RequestMapping("/angularjsTest")
	@ApiOperation(notes="查询",value="angular js test",httpMethod="POST")
	 
	@ApiImplicitParams({
		@ApiImplicitParam(name="param",value="查询条件",paramType="query",dataType="string")
	})
	public Map<String,Object> angularjsTest(@RequestBody SearchVo searchInfo){
		Map<String,Object>result=new HashMap<String,Object>();
		try {
 			Page<LogtxInfo> page= logService.queryLogtx(searchInfo);
			result.put("resultData",page.getContent());
			result.put("total",page.getTotalElements());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
