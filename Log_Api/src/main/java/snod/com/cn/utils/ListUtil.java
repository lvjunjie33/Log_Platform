package snod.com.cn.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import snod.com.cn.entity.LogFileInfo;

public class ListUtil {
	
	/**
	 * list根据某个属性排序，逆序
	 * */
	 public static List<LogFileInfo> sortList(List<LogFileInfo> list){

	        Collections.sort(list, new Comparator<LogFileInfo>(){
	            @Override
	            public int compare(LogFileInfo o1, LogFileInfo o2) {

	                if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime()){
	                    return -1;
	                }else{
	                    return 1;
	                }
	            }
	        });
	        return list;
	    }
	 
	 
	 /**
		 * list根据某个属性排序，逆序
		 * */
		 public static List<LogFileInfo> sortListPackageInfo(List<LogFileInfo> list){

		        Collections.sort(list, new Comparator<LogFileInfo>(){
		            @Override
		            public int compare(LogFileInfo o1, LogFileInfo o2) {

		                if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime()){
		                    return -1;
		                }else{
		                    return 1;
		                }
		            }
		        });
		        return list;
		    }
}
