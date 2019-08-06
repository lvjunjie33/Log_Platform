package snod.com.cn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import snod.com.cn.common.Constant;
import snod.com.cn.entity.LogtxInfo;
import snod.com.cn.entity.PackageInfo;
import snod.com.cn.entity.UploadFile;
import snod.com.cn.entity.vo.FileVo;
import snod.com.cn.entity.vo.SearchVo;
import snod.com.cn.redis.RedisService;
import snod.com.cn.service.repository.Log_tx_Repository;
import snod.com.cn.service.repository.Ota_package_Repository;
import snod.com.cn.utils.FileMd5Util;
import snod.com.cn.utils.KeyUtil;
import snod.com.cn.utils.NameUtil;

@Service
public class Log_tx_Service {
	
	@Autowired
	private Log_tx_Repository logRepository;
	@Autowired
	private Ota_package_Repository OtaPackageRepository;
	@Autowired
	private RedisService redisService;
	/**
	 * 查询
	 * */
	public LogtxInfo findTest(String email) {
		return null;
		
		
	}
	public LogtxInfo saveFile(String fileName, String deviceName,String requestIp, 
			 int remote, int logType, int fileType, String sn, String FilePath) {
		LogtxInfo tx=new LogtxInfo();
		tx.setCreateTime(new Date());
		tx.setDeviceName(deviceName);
		tx.setFileType(fileType);
		tx.setIp(requestIp);
		tx.setLogFileName(fileName);
		tx.setLogType(logType);
		tx.setSn(sn);
		tx.setPort(remote);
		tx.setLogFilePath(FilePath);
		return logRepository.save(tx);
		
	}
	
	
//	//es测试
//	public Map<String, Object> esTest(String email) {
//		 // 查找所有
//        //Iterable<Item> list = this.itemRepository.findAll();
//        // 对某字段排序查找所有 Sort.by("price").descending() 降序
//        // Sort.by("price").ascending():升序
//		Sort sort = new Sort(Direction.DESC, "@timestamp");
//        Iterable<LogtxInfo> list = this.logRepository.findAll(sort);
//
//        for (LogtxInfo item:list){
//            System.out.println(item.getId());
//        }
//		return null;
//	}
	public Page<LogtxInfo> queryLogtx(SearchVo searchInfo) throws ParseException {
		  // 构建查询条件
	    NativeSearchQueryBuilder macQueryBuilder = new NativeSearchQueryBuilder();
//		 BoolQueryBuilder qb = QueryBuilders.boolQuery();
//	    String [] str=new String[] {"device_id","mac"};

		if(StringUtils.isEmpty(searchInfo.getParam())) {
			macQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
		}else {
		    // 添加基本分词查询
//			qb.must(QueryBuilders.matchQuery("log_file_name",searchInfo.getParam()));
//			qb.must(QueryBuilders.multiMatchQuery(searchInfo.getParam(),"deviceId","log_file_name"));
//			macQueryBuilder.withQuery(QueryBuilders.boolQuery()
//					.must(QueryBuilders.wildcardQuery("deviceName", searchInfo.getParam()))
//					.should(QueryBuilders.multiMatchQuery("mac", searchInfo.getParam()))
//					.should(QueryBuilders.multiMatchQuery("sn", searchInfo.getParam()))
////					.should(QueryBuilders.wildcardQuery("port", "*"+searchInfo.getParam()+"*"))
////					.should(QueryBuilders.wildcardQuery("ip", "*"+searchInfo.getParam()+"*"))
//					.should(QueryBuilders.multiMatchQuery("logFileName",searchInfo.getParam()))
//					.should(QueryBuilders.multiMatchQuery("logType", searchInfo.getParam()))
//					.should(QueryBuilders.multiMatchQuery("fileType", searchInfo.getParam()))
//					.should(QueryBuilders.multiMatchQuery("logFileName", "*c*"))
//					);
//			Sort.by("price").descending();
//			Sort sort = new Sort(Direction.DESC, "createTime");
//			macQueryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
			macQueryBuilder.withQuery(QueryBuilders.boolQuery()
					.should(QueryBuilders.matchQuery("deviceName", searchInfo.getParam()))
					.should(QueryBuilders.matchQuery("mac", searchInfo.getParam()))	
					.should(QueryBuilders.matchQuery("sn", searchInfo.getParam()))
					.should(QueryBuilders.wildcardQuery("logFileName", "*"+searchInfo.getParam()+"*"))
					.should(QueryBuilders.matchQuery("logFilePath", searchInfo.getParam()))
							);
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("ip", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_type", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("file_type", searchInfo));
		}
	    PageRequest pager=new PageRequest(searchInfo.getPageIndex()-1,searchInfo.getPageSize());
	    macQueryBuilder.withPageable(pager);
	    // 搜索，获取结果
	    Page<LogtxInfo> items =  this.logRepository.search(macQueryBuilder.build());
	    // 总条数
//	    long total = items.getTotalElements();
//	    for(LogtxInfo logtxInfo:items.getContent()) {
//	    	SimpleDateFormat sim= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    	String createTime=sim.format(logtxInfo.getCreateTime());
//	    	logtxInfo.setCreateTime(sim.parse(createTime));
//	    }
	return items;

	}
	
	
	public Page<LogtxInfo> queryLogFile(String sn) throws ParseException {
		  // 构建查询条件
	    NativeSearchQueryBuilder macQueryBuilder = new NativeSearchQueryBuilder();
//		 BoolQueryBuilder qb = QueryBuilders.boolQuery();
//	    String [] str=new String[] {"device_id","mac"};
		if(sn!=null) {
		    // 添加基本分词查询
//			qb.must(QueryBuilders.matchQuery("log_file_name",searchInfo.getParam()));
//			qb.must(QueryBuilders.multiMatchQuery(searchInfo.getParam(),"deviceId","log_file_name"));
			long current=System.currentTimeMillis();//当前时间毫秒数
	        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
	        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
	        //查询当天数据
			macQueryBuilder.withQuery(QueryBuilders.boolQuery()
					.must(QueryBuilders.matchQuery("sn", sn))
					.must(QueryBuilders.rangeQuery("createTime").from(zero).to(twelve))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
					);
			
//			macQueryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("port", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("ip", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_type", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("file_type", searchInfo));
		}
//		Sort.by("price").descending();
//		Sort sort = new Sort(Direction.DESC, "createTime");
	    // 搜索，获取结果
	    Page<LogtxInfo> items =  this.logRepository.search(macQueryBuilder.build());
	    // 总条数
//	    long total = items.getTotalElements();

	return items;

	}
	
	
	
	
	public Page<PackageInfo> queryOtaPackage(String sn) throws ParseException {
		  // 构建查询条件
	    NativeSearchQueryBuilder macQueryBuilder = new NativeSearchQueryBuilder();
//		 BoolQueryBuilder qb = QueryBuilders.boolQuery();
//	    String [] str=new String[] {"device_id","mac"};
		if(sn!=null) {
		    // 添加基本分词查询
//			qb.must(QueryBuilders.matchQuery("log_file_name",searchInfo.getParam()));
//			qb.must(QueryBuilders.multiMatchQuery(searchInfo.getParam(),"deviceId","log_file_name"));
			long current=System.currentTimeMillis();//当前时间毫秒数
	        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
	        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
	        //查询当天数据
			macQueryBuilder.withQuery(QueryBuilders.boolQuery()
					.must(QueryBuilders.matchQuery("sn", sn))
					.must(QueryBuilders.rangeQuery("createTime").from(zero).to(twelve))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
					);
			
//			macQueryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("port", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("ip", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_type", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("file_type", searchInfo));
		}
//		Sort.by("price").descending();
//		Sort sort = new Sort(Direction.DESC, "createTime");
	    // 搜索，获取结果
	    Page<PackageInfo> items =  this.OtaPackageRepository.search(macQueryBuilder.build());
	    // 总条数
//	    long total = items.getTotalElements();

	return items;

	}
	
//	public Map<String, Object> findByFileMd5(String md5) {
//		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        UploadFile uploadFile = (UploadFile) redisService.get(md5);
//
//        Map<String, Object> map = null;
//        if (uploadFile == null) {
//            //没有上传过文件
//            map = new HashMap<>();
//            map.put("flag", 0);
//            map.put("fileId", KeyUtil.genUniqueKey());
//            map.put("date", simpleDateFormat.format(new Date()));
//        } else {
//            //上传过文件，判断文件现在还存在不存在
//            File file = new File(uploadFile.getFilePath());
//
//            if (file.exists()) {
//                if (uploadFile.getFileStatus() == 1) {
//                    //文件只上传了一部分
//                    map = new HashMap<>();
//                    map.put("flag", 1);
//                    map.put("fileId", uploadFile.getFileId());
//                    map.put("date", simpleDateFormat.format(new Date()));
//                } 
////                else if (uploadFile.getFileStatus() == 2) {
////                    //文件早已上传完整
////                    map = new HashMap<>();
////                    map.put("flag" , 2);
////                }
//            } 
//                else {
//
//                map = new HashMap<>();
//                map.put("flag", 0);
//                map.put("fileId", uploadFile.getFileId());
//                map.put("date", simpleDateFormat.format(new Date()));
//            }
//        }
//        return map;
//    }
    public Map<String, Object> realUpload(FileVo form, MultipartFile multipartFile, String requestIp, int remote) throws Exception {
    	String sn=form.getSn();
    	String deviceName=form.getDeviceName();
    	int logType=form.getLogType();
    	int fileType=form.getFileType();
    	String action = form.getAction();
    	int index = 0;
    	int total = 0;
    	long size = 0;
    	String fileName = null;
    	//报错后，查询缓存
//    	if(form.isIserror()) {
    		//查询缓存
//    		UploadFile uploadFileQuery=(UploadFile) redisService.get(sn+"_deviceLog");
//	    	if(uploadFileQuery!=null) {
//	            index = uploadFileQuery.getFileIndex();
//	            total = uploadFileQuery.getFileTotal();
//	            fileName = uploadFileQuery.getFileName();
//	           	size = uploadFileQuery.getFileSize();
//	        }
//    	}else {
	        index = Integer.valueOf(form.getIndex());
	        total = Integer.valueOf(form.getTotal());
	        fileName = form.getName();
	       if(form.getSize()!=null) {
	       	 size = Integer.parseInt(form.getSize());
	       }
//    	}
//        String suffix = NameUtil.getExtensionName(fileName);
		Calendar date = Calendar.getInstance();
	    String year = String.valueOf(date.get(Calendar.YEAR))+"/";
        String month = String.valueOf(date.get(Calendar.MONTH)+1)+"/";
        String saveDirectory = Constant.PATH + File.separator + "device_log/"+deviceName+"/"+year+month;
        String filePath = saveDirectory+fileName;
        //验证路径是否存在，不存在则创建目录
        File path = new File(saveDirectory);
        if (!path.exists()) {
            path.mkdirs();
        }
        //文件分片位置
        File file = new File(saveDirectory, fileName + "_" + index);

        //根据action不同执行不同操作. check:校验分片是否上传过; upload:直接上传分片
        Map<String, Object> map = null; 
        if("upload".equals(action)) {
            //分片上传过程中出错,有残余时需删除分块后,重新上传
            if (file.exists()) {
                file.delete();
            }
            multipartFile.transferTo(file);
            map = new HashMap<>();
            map.put("flag", "1");
//            map.put("fileId", fileId);
            if(index != total) {
            	//第一片，数据库创建文件信息
            	if(index==1) {
            		 //文件第一个分片上传时记录到数据库
                    UploadFile uploadFile = new UploadFile();
                    uploadFile.setFileName(fileName);
//                    uploadFile.setFileSuffix(suffix);
//                    uploadFile.setFileId(fileId);
                    uploadFile.setFilePath(filePath);
                    uploadFile.setFileSize(size);
                    uploadFile.setFileStatus(1);
                    uploadFile.setFileIndex(index);
                    uploadFile.setFileTotal(total);
                    uploadFile.setCreateTime(new Date());
                    uploadFile.setDeviceName(deviceName);
                    uploadFile.setSn(sn);
                    redisService.set(sn+"_deviceLog",uploadFile);
            	}
            	if(index>1 && index<total){
            	  //更新记录到数据库
                  UploadFile uploadFile = new UploadFile();
                  uploadFile.setFileName(fileName);
//                  uploadFile.setFileSuffix(suffix);
//                  uploadFile.setFileId(fileId);
                  uploadFile.setFilePath(filePath);
                  uploadFile.setFileSize(size);
                  uploadFile.setFileStatus(1);
                  uploadFile.setFileIndex(index);
                  uploadFile.setFileTotal(total);
                  uploadFile.setCreateTime(new Date());
                  uploadFile.setDeviceName(deviceName);
                  uploadFile.setSn(sn);
                  uploadFile.setUpdateTime(new Date());
                  redisService.set(sn+"_deviceLog",uploadFile);
            	}
                return map;
            }
        }
        //合并
        if (path.isDirectory()) {
        	File newFile = new File(saveDirectory,fileName);
        	//文件存在删除文件
	       	 if (newFile.exists()) {
	       		 newFile.delete();
	            }
            File[] fileArray = path.listFiles();
            if (fileArray != null) {
            	if(fileArray[0].getName().contains(fileName)) {
		            //分块全部上传完毕,合并
		//                  File newFile = new File(saveDirectory, fileId + "." + suffix);    
		            FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入
		            byte[] byt = new byte[10 * 1024 * 1024];
		            int len;
		            
		            for (int i = 0; i < total; i++) {
		                int j = i + 1;
		                File f=new File(saveDirectory, fileName + "_" + j);
		                FileInputStream temp = new FileInputStream(f);
		                while ((len = temp.read(byt)) != -1) {
		                    outputStream.write(byt, 0, len);
		                }
		                //关闭流
		                temp.close();
		                //删除分片文件
		                f.delete();
		            }       
		              outputStream.close();
		                //修改FileRes记录为上传成功
		              UploadFile uploadFile = new UploadFile();
		              uploadFile.setFileName(fileName);
		//              uploadFile.setFileSuffix(suffix);
		//              uploadFile.setFileId(fileId);
		              uploadFile.setFilePath(filePath);
		              uploadFile.setFileSize(size);
		              uploadFile.setFileStatus(2);
		              uploadFile.setFileIndex(index);
		              uploadFile.setFileTotal(total);
		              uploadFile.setCreateTime(new Date());
		              uploadFile.setDeviceName(deviceName);
		              uploadFile.setSn(sn);
		              uploadFile.setUpdateTime(new Date());
		              redisService.set(sn+"_deviceLog",uploadFile);
		
		              map=new HashMap<>();
		//               map.put("fileId", fileId);
		              map.put("flag", "2");
		              return map;
			        
            	}
            }
        }
        	//保存日志信息
        saveFile(fileName,deviceName,requestIp,remote,logType,fileType,sn,filePath);
        return map;
    }
	public Map<String, Object> findByFiletxInfo(String sn) {
		Map<String, Object> map=new HashMap<String,Object>();
		UploadFile uploadFileQuery=(UploadFile) redisService.get(sn+"_deviceLog");
		map.put("index", uploadFileQuery.getFileIndex());
		return map;
	}
	public PackageInfo saveOtaFile(String fileName, String product, String version, String country, String sn,
			String language, String filePath) {
		PackageInfo packageInfo=new PackageInfo();
		packageInfo.setCreateTime(new Date());
		packageInfo.setCountry(country);
		packageInfo.setLanguage(language);
		packageInfo.setPackageName(fileName);
		packageInfo.setPackagePath(filePath);
		packageInfo.setProduct(product);
		packageInfo.setSn(sn);
		packageInfo.setVersion(version);
		return OtaPackageRepository.save(packageInfo);
		
	}
}
