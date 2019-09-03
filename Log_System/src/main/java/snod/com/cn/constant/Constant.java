package snod.com.cn.constant;

import java.io.File;

public class Constant {
	public static final String PATH = System.getProperty("user.dir") + File.separator + "file";

	/** 用户名 */
	public static final String PRINCIPAL = "principal";
	/** 超级管理员ID */
	public static final int SUPER_ADMIN_ID = 1;
	/** 系统菜单最大id */
	public static final int SYS_MENU_MAX_ID = 1;
}
