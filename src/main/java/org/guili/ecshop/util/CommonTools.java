package org.guili.ecshop.util;

import javax.servlet.http.HttpServletRequest;

public class CommonTools {
	
	/**
	 * 获得ip地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) { 
		String ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getRemoteAddr(); 
		}
		if(ip == null){
			ip = "127.0.0.1";
		}
		return ip; 
		} 

}
