package com.daicy.crawler.core.exception;


/**
 *@author six    
 *@date 2016年8月26日 下午4:24:25  
*/
public class TimeoutException extends AbstractHttpException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507844940537119834L;


	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
