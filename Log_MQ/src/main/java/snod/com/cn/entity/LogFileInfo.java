package snod.com.cn.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * lvjj 2019-06-14
 * */
@Data
public class LogFileInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 * */
	private Long id;
	/**
	 * 设备名称
	 * */
	private String deviceName;
	/**
	 * sn序列号
	 * */
	private String sn;
	/**
	 * 日志类型(1,错误日志；2，操作日志，3，系统日志...)
	 * */
	private int logType;
	/**
	 * 文件类型(1,压缩包；2,文本文件；...)
	 * */
	private int fileType;
	/**
	 * 文件名称
	 * */
	private String fileName;
	/**
	 * 文件路径
	 * */
	private String filePath;
	/**
	 * 服务器本地路径（作用于从本地下载文件直接取值）
	 * */
	private String fileLocalPath;
	/**
	 * 文件分片序列号
	 * */
	private int fileIndex;
	/**
	 * 文件总分片
	 * */
	private int fileTotal;
	/**
	 * 文件总大小 
	 * */
	private Long fileSize;
	/**
	 * 上传时间
	 * */
	private Date createTime;

	
}
