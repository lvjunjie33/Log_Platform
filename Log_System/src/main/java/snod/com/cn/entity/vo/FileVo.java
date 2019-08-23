package snod.com.cn.entity.vo;

import lombok.Data;

@Data
public class FileVo {
	private String md5;

    private String uuid;

    private String date;

    private String name;

    private String size;

    private String total;

    private String index;

    private String action;

    private String partMd5;

    private String sn;
    
    private String deviceName;
    
    private int logType;
    
    private int fileType;
    
    private boolean iserror;
}
