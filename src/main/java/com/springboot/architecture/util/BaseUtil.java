package com.springboot.architecture.util;

import javax.servlet.http.HttpServletRequest;

public class BaseUtil {

	  /**
	   * 获取真实ip
	   * 
	   * @param request
	   * @return
	   */
	  public static String getRemoteIP(HttpServletRequest request) {
	    if (request.getHeader("x-forwarded-for") == null) {
	      return request.getRemoteAddr();
	    }
	    return request.getHeader("x-forwarded-for");
	  }
}
