package com.daicy.crawler.common.utils;

import java.util.Map;
import java.util.Objects;

/** 
* @author: create by daichangya
* @site: zthinker.com
* @date 创建时间：2017年4月12日 上午9:29:44 
*/
public class MapUtils {

	public static <T>T toObject(Map<String,Object> map,Class<T> clz) throws InstantiationException, IllegalAccessException{
		Objects.requireNonNull(map);
		Objects.requireNonNull(clz);
		T result=clz.newInstance();
		Object value=null;
		for(String key:map.keySet()){
			value=map.get(key);
			if(isBaseType(value)){
				
			}
		}
		return result;
	}
	
	private static boolean isBaseType(Object ob){
		return false;
	}
}
