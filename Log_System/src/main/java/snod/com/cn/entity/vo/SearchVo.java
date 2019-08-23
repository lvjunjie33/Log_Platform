package snod.com.cn.entity.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class SearchVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String param;
	
	private int pageIndex;
	
	private int pageSize;
	
	private int total;
}
