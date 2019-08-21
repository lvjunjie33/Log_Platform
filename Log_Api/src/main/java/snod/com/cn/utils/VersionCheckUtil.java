package snod.com.cn.utils;

public class VersionCheckUtil {
	
	public static boolean verCheck(String deviceVersion,String version) {
		if(!version.contains(".") || !deviceVersion.contains(".")) {
			return false;
		}
		String []stDevice=deviceVersion.split("\\.");
		String []st=version.split("\\.");
		String s="";
		for(int i=0;i<st.length;i++) {
			int stDeviceInt=Integer.parseInt(stDevice[i]);
			int stInt=Integer.parseInt(st[i]);	
			if(stInt>stDeviceInt) {
				s=s+"1";
			}
		}
		if(s.contains("1")) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
}
