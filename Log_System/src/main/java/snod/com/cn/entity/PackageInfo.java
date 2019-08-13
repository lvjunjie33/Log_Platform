package snod.com.cn.entity;

import java.util.Date;


//@Document(indexName= "ota_packeage",type="packeage")
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
	
	
	
	public long getPackeageSize() {
		return packeageSize;
	}
	public void setPackeageSize(long packeageSize) {
		this.packeageSize = packeageSize;
	}
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getPackagePath() {
		return PackagePath;
	}
	public void setPackagePath(String packagePath) {
		PackagePath = packagePath;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	
	
	
}
