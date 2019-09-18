package snod.com.cn.config;


import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * Created by dell on 2019/5/30.
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfig.class);

    /**
     * elk集群地址node-1
     */
    @Value("${spring.data.elasticsearch.node-1-ip}")
    private String nodeIpOne;
    /**
     * elk集群地址node-2
     */
    @Value("${spring.data.elasticsearch.node-2-ip}")
    private String nodeIpTwo;
    /**
     * elk集群地址node-3
     */
    @Value("${spring.data.elasticsearch.node-3-ip}")
    private String nodeIpThree;

    /**
     * 端口
     */
    @Value("${spring.data.elasticsearch.port}")
    private String port;

    /**
     * 集群名称
     */
    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    /**
     * 连接池
     */
    @Value("${spring.data.elasticsearch.pool}")
    private String poolSize;

    /**
     * Bean name default  函数名字
     *
     * @return
     */
    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        LOGGER.info("Elasticsearch初始化开始。。。。。");
        TransportClient transportClient = null;
        try {
            // 配置信息
            Settings esSetting = Settings.builder()
                    .put("cluster.name", clusterName) //集群名字
                    .put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))//增加线程池个数，暂时设为5
                    .build();
            //配置信息Settings自定义
            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddressOne = new TransportAddress(InetAddress.getByName(nodeIpOne), Integer.valueOf(port));
            TransportAddress transportAddressTwo = new TransportAddress(InetAddress.getByName(nodeIpTwo), Integer.valueOf(port));
            TransportAddress transportAddressThree = new TransportAddress(InetAddress.getByName(nodeIpThree), Integer.valueOf(port));
            transportClient.addTransportAddresses(transportAddressOne,transportAddressTwo,transportAddressThree);
        } catch (Exception e) {
            LOGGER.error("elasticsearch TransportClient create error!!", e);
        }
        return transportClient;
    }
}

