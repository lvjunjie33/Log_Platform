package snod.com.cn.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import snod.com.cn.constant.Esconstant;
import snod.com.cn.entity.PackageInfo;
import snod.com.cn.redis.RedisService;
//import snod.com.cn.service.repository.Ota_package_Repository;
import snod.com.cn.utils.elasticsearch.ElasticsearchUtil;
import snod.com.cn.utils.elasticsearch.EsPage;

@Service
public class Ota_Package_Service {
	

//	@Autowired
//	private Ota_package_Repository OtaPackageRepository;
	@Autowired
	private RedisService redisService;
	
	

	
	
	
	
	
	
	
	public List<PackageInfo> queryOtaPackage(String sn) throws ParseException {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		  // 构建查询条件
//	    NativeSearchQueryBuilder macQueryBuilder = new NativeSearchQueryBuilder();
//		 BoolQueryBuilder qb = QueryBuilders.boolQuery();
//	    String [] str=new String[] {"device_id","mac"};
		 EsPage esPage = null;
		if(sn!=null) {
		    // 添加基本分词查询
//			qb.must(QueryBuilders.matchQuery("log_file_name",searchInfo.getParam()));
//			qb.must(QueryBuilders.multiMatchQuery(searchInfo.getParam(),"deviceId","log_file_name"));
//			long current=System.currentTimeMillis();//当前时间毫秒数
//	        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
//	        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
	        //查询当天数据
//			SortBuilder sortBuilder3 = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
			boolQuery.must(QueryBuilders.matchQuery("sn", sn))
					.must(QueryBuilders.rangeQuery("createTime"));
		  esPage=ElasticsearchUtil.searchDataPage(Esconstant.OTA_INDEXNAME, Esconstant.OTA_TYPE, 
							 0, 1, 
							boolQuery, null, "createTime", null);
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
//					.should(QueryBuilders.wildcardQuery("logFileName", "*c*"))
//					).withSort(sortBuilder3);
			
//			macQueryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("port", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("ip", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_file_name", searchInfo.getParam()));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("log_type", searchInfo));
//		    queryBuilder.withQuery(QueryBuilders.matchQuery("file_type", searchInfo));
//			PageRequest pager=new PageRequest(0,1);
//		    macQueryBuilder.withPageable(pager);
		}
//		Sort.by("price").descending();
//		Sort sort = new Sort(Direction.DESC, "createTime");
	    // 搜索，获取结果
//	    Page<PackageInfo> items =  this.OtaPackageRepository.search(macQueryBuilder.build());
	    // 总条数
//	    long total = items.getTotalElements();
		 List<PackageInfo> listPackageInfo=new ArrayList<PackageInfo>();
		 List<Map<String, Object>> list= esPage.getRecordList();
		 for (Map<String, Object> map : list) {
			 JSONObject jsonObject=(JSONObject) JSONObject.toJSON(map);
			 listPackageInfo.add(JSON.toJavaObject(jsonObject,PackageInfo.class));
		}
		 
//		 System.out.println(list.size());
	return listPackageInfo;

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
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(packageInfo);
		String id=ElasticsearchUtil.addData(jsonObject, Esconstant.OTA_INDEXNAME, Esconstant.OTA_TYPE);
		packageInfo.setId(id);
		return packageInfo;
		
	}
}
