package snod.com.cn.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import snod.com.cn.entity.LogtxInfo;
import snod.com.cn.entity.PackageInfo;

public class ListUtil {
	
	/**
	 * list根据某个属性排序，逆序
	 * */
	 public static List<LogtxInfo> sortList(List<LogtxInfo> list){

	        Collections.sort(list, new Comparator<LogtxInfo>(){
	            @Override
	            public int compare(LogtxInfo o1, LogtxInfo o2) {

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
		 public static List<PackageInfo> sortListPackageInfo(List<PackageInfo> list){

		        Collections.sort(list, new Comparator<PackageInfo>(){
		            @Override
		            public int compare(PackageInfo o1, PackageInfo o2) {

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
