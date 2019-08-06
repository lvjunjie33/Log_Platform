package snod.com.cn.service.repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import snod.com.cn.entity.PackageInfo;

public interface Ota_package_Repository extends ElasticsearchRepository<PackageInfo,String>{

}
