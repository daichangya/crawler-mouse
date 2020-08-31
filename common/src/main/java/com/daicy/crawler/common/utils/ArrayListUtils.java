package com.daicy.crawler.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by daichangya
 * @site: zthinker.com
 * @date 创建时间：2017年4月5日 下午3:56:05
 */
public class ArrayListUtils {

	@SafeVarargs
	public static <T> List<T> asList(T... a) {
		if (null == a) {
			throw new NullPointerException();
		}
		List<T> list = new ArrayList<>();
		for (T t : a) {
			list.add(t);
		}
		return list;
	}
}
