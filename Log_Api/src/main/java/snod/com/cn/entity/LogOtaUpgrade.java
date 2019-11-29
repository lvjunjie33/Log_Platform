package snod.com.cn.entity;

import java.util.Date;

import lombok.Data;
@Data
public class LogOtaUpgrade {
	/**
	 * 主键
	 * */
	private String id;
	/**
	 * sn序列号
	 * */
	private String sn;
	/**
	 * 产品名称
	 * */
	private String product;
	/**
	 * 版本号
	 * */
	private String version;
	/**
	 * 国家
	 * */
	private String country;
	/**
	 * 语言
	 * */
	private String language;
	/**
	 * 上传时间
	 * */
	private Date createTime;
	/**
	 * 升级包名称
	 * */
	private String packageName;
	/**
	 * 升级包路径
	 * */
	private String packagePath;
	/**
	 * 升级包大小
	 * */
	private Long packeageSize;
	
	
	

	
	
	
}
