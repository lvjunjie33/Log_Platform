package snod.com.cn.service.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import snod.com.cn.entity.LogtxInfo;

public interface Log_tx_Repository extends ElasticsearchRepository<LogtxInfo,String>{

}
