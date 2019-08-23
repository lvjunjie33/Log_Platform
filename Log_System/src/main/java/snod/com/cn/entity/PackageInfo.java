package snod.com.cn.entity;

import java.util.Date;

import lombok.Data;


@Data
public class PackageInfo {
	
	private String id;
	private String sn;
	private String product;
	private String version;
	private String country;
	private String language;
	private Date createTime;
	private String PackageName;
	private String PackagePath;//文件路径
	private long packeageSize;
	
	
	
	

	
	
	
}
