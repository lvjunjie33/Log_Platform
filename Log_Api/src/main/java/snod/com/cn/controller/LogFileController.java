package snod.com.cn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import snod.com.cn.config.OSSFileConfig;
import snod.com.cn.entity.LogFileInfo;
import snod.com.cn.entity.vo.FileVo;
import snod.com.cn.entity.vo.SearchVo;
import snod.com.cn.service.LogFileService;
import snod.com.cn.utils.EcpStackTrace;
import snod.com.cn.utils.FileUtil;
import snod.com.cn.utils.FtpUtil;
import snod.com.cn.utils.IPUtil;
import snod.com.cn.utils.ListUtil;
import snod.com.cn.utils.ResultInfo;
import snod.com.cn.utils.ResultTools;
import snod.com.cn.utils.StringUtil;

@Api(tags= {"腾讯日志接口"})
@RestController
@RequestMapping("/tx_log")
public class LogFileController {
	
	private final static Logger logger = LoggerFactory.getLogger(LogFileController.class);
	
	@Autowired
	private LogFileService logService;
	
	@Autowired
	private OSSFileConfig ossFileConfig;
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
			String logFilePath = request.getServletContext().getRealPath("/file/logFile");	
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
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR));
	        String month = String.valueOf(date.get(Calendar.MONTH)+1);
	        String filePath="device_log/"+deviceName+"/"+year+"/"+month+"/"+uploadFile.getOriginalFilename();
	        String fileLocalPath=logFilePath+"\\device_log\\"+deviceName+"\\"+year+"\\"+month+"\\";
	        LogFileInfo logFileInfo=logService.saveFile(uploadFile.getOriginalFilename(),deviceName,logTypeInt,fileTypeInt,sn,filePath,fileLocalPath,uploadFile.getSize());
	        //上传到服务器本地
	        if(!new File(fileLocalPath).exists()){       
				new File(fileLocalPath).mkdirs();    
	        }
	        File localFileContent = new File(fileLocalPath, uploadFile.getOriginalFilename());
	        //写文件
	        uploadFile.transferTo(localFileContent);
	        //发送异步消息
	        logService.sendFileMq(logFileInfo);
	        //上传到文件服务器
//	        ossFileConfig.uploadFile(FilePath, uploadFile.getInputStream());
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
	public ResultInfo logFileUpLoadHttpTest(HttpServletRequest request,
			@ApiParam(value ="日志文件",required=true)MultipartFile content
			){
		try {
			//接收头部参数
			String sn=request.getHeader("sn");
			String logType=request.getHeader("logType");
			String fileType=request.getHeader("fileType");
			String deviceName=request.getHeader("deviceName");
			String logFilePath = request.getServletContext().getRealPath("/file/logFile");	
//			Map<String, MultipartFile> fileMap = new LinkedHashMap<>(0);
//		     if (request instanceof MultipartHttpServletRequest) {
//		          fileMap = ((MultipartHttpServletRequest) request).getFileMap();
//		      }
			//IP
			int logTypeInt = 0;
		    int fileTypeInt = 0;
		    if(logType!=null && fileType!=null) {
			     if(StringUtil.isNumeric(logType) && StringUtil.isNumeric(fileType)) {
			    	 logTypeInt=Integer.parseInt(logType);
			    	 fileTypeInt=Integer.parseInt(fileType);
			     }
		     }
//			MultipartFile content=fileMap.get("uploadFile");//会议记录文件
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR));
	        String month = String.valueOf(date.get(Calendar.MONTH)+1);
	        String filePath="device_log/"+deviceName+"/"+year+"/"+month+"/"+content.getOriginalFilename();
	        String fileLocalPath=logFilePath+"\\device_log\\"+deviceName+"\\"+year+"\\"+month+"\\";
	        LogFileInfo logFileInfo=logService.saveFile(content.getOriginalFilename(),deviceName,logTypeInt,fileTypeInt,sn,filePath,fileLocalPath,content.getSize());
	        //上传到服务器本地
	        
	        if(!new File(fileLocalPath).exists()){       
				new File(fileLocalPath).mkdirs();    
	        }
	        File localFileContent = new File(fileLocalPath, content.getOriginalFilename());
	        //写文件
	        content.transferTo(localFileContent);
	        //发送异步消息
	        logService.sendFileMq(logFileInfo);
	        //上传到文件服务器
//	        ossFileConfig.uploadFile(FilePath, content.getInputStream());
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
	        logService.saveFileFtp(FileName,deviceName,logTypeInt,fileTypeInt,sn,FilePath+FileName,content.getSize());
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
			LogFileInfo logFileInfo=logService.queryLogFileInfo(sn);
//	    	List<LogFileInfo> list=page.getContent();
//	    	List<LogFileInfo> endTimeTempList = new ArrayList<LogFileInfo>(list);
//	    	endTimeTempList=ListUtil.sortList(endTimeTempList);
	        InputStream inputStream = ossFileConfig.downLoadFile(logFileInfo.getFilePath());
	        OutputStream outputStream = response.getOutputStream();
	        byte[] btImg = FileUtil.readInputStream(inputStream);//得到二进制数据
	        //指明为下载
	        response.setContentType("application/x-download");     
	        response.setContentLengthLong(btImg.length);
	        response.addHeader("Content-Disposition", "attachment;fileName=" + logFileInfo.getFileName());   // 设置文件名
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
			LogFileInfo logFileInfo=logService.queryLogFileInfo(sn);
//	    	List<LogFileInfo> list=page.getContent();
//	    	List<LogFileInfo> endTimeTempList = new ArrayList<LogFileInfo>(list);
//	    	endTimeTempList=ListUtil.sortList(endTimeTempList);
	    	String []str=logFileInfo.getFilePath().split("/");
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
	        InputStream inputStream = new FileInputStream(new File(folder,logFileInfo.getFileName()));
	        OutputStream outputStream = response.getOutputStream();
	        //指明为下载
	        response.setContentType("application/x-download");
	        response.addHeader("Content-Disposition", "attachment;fileName=" +logFileInfo.getFilePath());   // 设置文件名
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
        	Page<LogFileInfo> page=logService.queryLogFile(sn);
        	List<LogFileInfo> list=page.getContent();
        	List<LogFileInfo> endTimeTempList = new ArrayList<LogFileInfo>(list);
        	endTimeTempList=ListUtil.sortList(endTimeTempList);
            //jdk7新特性，可以直接写到try()括号里面，java会自动关闭
            InputStream inputStream = FtpUtil.downloadFile(endTimeTempList.get(0).getFilePath());
            OutputStream outputStream = response.getOutputStream();
            //指明为下载
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + endTimeTempList.get(0).getFileName());   // 设置文件名
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
 			Page<LogFileInfo> page= logService.queryLogtx(searchInfo);
			result.put("resultData",page.getContent());
			result.put("total",page.getTotalElements());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
