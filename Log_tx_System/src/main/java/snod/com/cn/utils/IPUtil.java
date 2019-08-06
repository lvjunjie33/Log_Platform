package snod.com.cn.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lvjj
 * */

public class IPUtil {
	
	
	/**
	 * 获取IP地址
	 * */
	 public static String getIP(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for");
	        if (!checkIP(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	        }
	        if (!checkIP(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	        }
	        if (!checkIP(ip)) {
	            ip = request.getRemoteAddr();
	        }
	        return ip;
	    }
	 /**
	  * 检查IP地址是
	  * */
    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }
}
