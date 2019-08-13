package snod.com.cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * lvjj 2019-06-14
indexName索引名称 可以理解为数据库名 必须为小写 不然会报错
type类型 可以理解为表名
 * */
//@Document(indexName= "tx_logs",type="log")
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}



	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}


	public String getLogFileName() {
		return logFileName;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}

	
	
	
}
