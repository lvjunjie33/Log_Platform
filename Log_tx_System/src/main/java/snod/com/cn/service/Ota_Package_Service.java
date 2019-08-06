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
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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
public class Ota_Package_Service {
	

	@Autowired
	private Ota_package_Repository OtaPackageRepository;
	@Autowired
	private RedisService redisService;
	
	

	
	
	
	
	
	
	
	public Page<PackageInfo> queryOtaPackage(String sn) throws ParseException {
		  // 构建查询条件
	    NativeSearchQueryBuilder macQueryBuilder = new NativeSearchQueryBuilder();
//		 BoolQueryBuilder qb = QueryBuilders.boolQuery();
//	    String [] str=new String[] {"device_id","mac"};
		if(sn!=null) {
		    // 添加基本分词查询
//			qb.must(QueryBuilders.matchQuery("log_file_name",searchInfo.getParam()));
//			qb.must(QueryBuilders.multiMatchQuery(searchInfo.getParam(),"deviceId","log_file_name"));
//			long current=System.currentTimeMillis();//当前时间毫秒数
//	        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
//	        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
	        //查询当天数据
			SortBuilder sortBuilder3 = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
			macQueryBuilder.withQuery(QueryBuilders.boolQuery()
					.must(QueryBuilders.matchQuery("sn", sn))
					.must(QueryBuilders.rangeQuery("createTime"))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
					).withSort(sortBuilder3);
			
//			macQueryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("port", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("ip", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_type", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("file_type", searchInfo));
			PageRequest pager=new PageRequest(0,1);
		    macQueryBuilder.withPageable(pager);
		}
//		Sort.by("price").descending();
//		Sort sort = new Sort(Direction.DESC, "createTime");
	    // 搜索，获取结果
	    Page<PackageInfo> items =  this.OtaPackageRepository.search(macQueryBuilder.build());
	    // 总条数
//	    long total = items.getTotalElements();

	return items;

	}
	public PackageInfo saveOtaFile(String fileName, String product, String version, String country, String sn,
			String language, String filePath, long packageSize) {
		PackageInfo packageInfo=new PackageInfo();
		packageInfo.setCreateTime(new Date());
		packageInfo.setCountry(country);
		packageInfo.setLanguage(language);
		packageInfo.setPackageName(fileName);
		packageInfo.setPackagePath(filePath);
		packageInfo.setProduct(product);
		packageInfo.setSn(sn);
		packageInfo.setVersion(version);
		packageInfo.setPackeageSize(packageSize);
		return OtaPackageRepository.save(packageInfo);
		
	}
}
