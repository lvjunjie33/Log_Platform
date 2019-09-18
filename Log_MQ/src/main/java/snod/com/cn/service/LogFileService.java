package snod.com.cn.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.io.FileUtil;
import snod.com.cn.config.RabbitMqConfig;
import snod.com.cn.constant.Constant;
import snod.com.cn.constant.Esconstant;
import snod.com.cn.dao.LogFileDao;
import snod.com.cn.entity.EsLogFileInfo;
import snod.com.cn.entity.LogFileInfo;
import snod.com.cn.util.EncodingDetect;
import snod.com.cn.util.elasticsearch.ElasticsearchUtil;

//import snod.com.cn.entity.UploadFile;

//import snod.com.cn.service.repository.Log_tx_Repository;
//import snod.com.cn.service.repository.Ota_package_Repository;


@Service
public class LogFileService {
	
	@Autowired
	private LogFileDao logFileDao;
	/**
	 * 查询
	 * */
	public LogFileInfo queryLogFileInfo(Long id) {
		return logFileDao.queryLogFileInfo(id);
	}
	public EsLogFileInfo saveEs(LogFileInfo logFileInfo, List<File> fileList) throws IOException {
		/**组装es实体对象：EsLogFileInfo*/
		EsLogFileInfo esLogFileInfo=new EsLogFileInfo();
		esLogFileInfo.setCreateTime(logFileInfo.getCreateTime());
		esLogFileInfo.setDeviceName(logFileInfo.getDeviceName());
		esLogFileInfo.setFileIndex(logFileInfo.getFileIndex());
		esLogFileInfo.setFileLocalPath(logFileInfo.getFileLocalPath());
		esLogFileInfo.setFileName(logFileInfo.getFileName());
		esLogFileInfo.setFilePath(logFileInfo.getFilePath());
		esLogFileInfo.setFileSize(logFileInfo.getFileSize());
		esLogFileInfo.setFileTotal(logFileInfo.getFileTotal());
		esLogFileInfo.setFileType(logFileInfo.getFileType());
		esLogFileInfo.setLogType(logFileInfo.getLogType());
		esLogFileInfo.setSn(logFileInfo.getSn());
		Map<String,StringBuilder> map=new HashMap<String,StringBuilder>();
		/**循环所有文件，把文件中log文本存储map,key:fileName value:log文本*/
		FileInputStream fin=null;
		InputStreamReader is=null;
		BufferedReader readerlineContent=null;
		for(File f:fileList) {
			fin=new FileInputStream(f);
			//文件不能为空
			if(!FileUtil.isEmpty(f)) {
				//根据文件内容编码格式设置解压后文件的编码格式
				is=new InputStreamReader(fin,EncodingDetect.getJavaEncode(f));
			}else {
				//文件为空，默认UTF-8编码格式
				is=new InputStreamReader(fin,"UTF-8");
			}
			StringBuilder content = new StringBuilder();
			readerlineContent=new BufferedReader(is);
		    String lineContent="";
		    while ((lineContent = readerlineContent.readLine()) != null) {
		    	content.append(lineContent + "/n");
	        }
		    if(f.getName().contains(".")) {
		    	map.put(f.getName().substring(0,f.getName().indexOf(".")), content);
		    }else {
		    	map.put(f.getName(), content);
		    }
			
			fin.close();
			is.close();
			readerlineContent.close();
		}			
		esLogFileInfo.setFileMap(map);
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(esLogFileInfo);
		String id=ElasticsearchUtil.addData(jsonObject, Esconstant.TX_INDEXNAME, Esconstant.TX_TYPE);
		esLogFileInfo.setId(id);
		return esLogFileInfo;
		
	}
		

}
