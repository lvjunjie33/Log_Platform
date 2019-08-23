package snod.com.cn.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * lvjj 2019-06-14
indexName索引名称 可以理解为数据库名 必须为小写 不然会报错
type类型 可以理解为表名
 * */
@Data
public class LogtxInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;//id
	private String deviceName;//设备ID
	private String mac;//mac地址
	private String sn;//序列号
	private int port;//端口
	private String ip;//设备IP地址
	private String logFileName;//日志文件名称
	private int logType;//日志类型(1,错误日志；2，操作日志，3，系统日志...)
	private int fileType;//文件类型(1,压缩包；2,文本文件；...)
	private Date createTime;//上传时间
	private String logFilePath;//文件路径
	

	
	
	
}
