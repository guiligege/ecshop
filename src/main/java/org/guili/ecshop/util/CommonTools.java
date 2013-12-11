package org.guili.ecshop.util;


import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class CommonTools {
	
	public static String proxyip=ResourceUtil.getValue(ResourceUtil.PROXYPATH,"proxyip");
	public static String proxyport=ResourceUtil.getValue(ResourceUtil.PROXYPATH,"proxyport");
	
	private static Logger logger=Logger.getLogger(CommonTools.class);
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
	
	/**
	 * 请求url，获取json
	 * @param url		url
	 * @param encode	url 编码
	 * @param param		参数
	 * @return
	 */
	public static String requestUrl(String url,String encode,String param){
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		//String encodeType = URLEncoder.encode(type.trim(), encode);
		getMethod.setQueryString(""+param);
		String jsonString="";
		try {
			int statusCode = client.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			jsonString = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("CommonTools-->加载url请求失败 ：", e);
			return null;
		} finally {
			getMethod.releaseConnection();
		}
		return jsonString;
	}
	
	/**
	 * 请求url，获取json
	 * @param url		url
	 * @param encode	url 编码
	 * @param param		参数
	 * @return
	 */
	public static String requestUrl(String url,String encode){
		HttpClient client = new HttpClient();
		// 代理的主机  
        //ProxyHost proxy = new ProxyHost("openproxy.huawei.com", 8080);  
        // 使用代理  
       // client.getHostConfiguration().setProxyHost(proxy);
       //client.getHostConfiguration().setProxy(proxyip, Integer.parseInt(proxyport));
      
		GetMethod getMethod = new GetMethod(url);
		//String encodeType = URLEncoder.encode(type.trim(), encode);
		String jsonString="";
		try {
			int statusCode = client.executeMethod(getMethod);
			logger.info("url:-->"+url);
			logger.info("Proxy success!");
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			jsonString = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("CommonTools-->加载url请求失败 ：", e);
			return null;
		} finally {
			getMethod.releaseConnection();
		}
		return jsonString;
	}
	/**
	 * double类型格式化
	 * @param result	需要格式化的数据
	 * @return
	 */
	public static Double doubleFormat(double result){
		DecimalFormat r=new DecimalFormat();
		r.applyPattern("#0.00");
		return new Double(r.format(result));
	}

	public static void main(String[] args) {
		String html;
		html = CommonTools.requestUrl("http://www.converse.com.cn", "UTF-8");
		logger.info(html);
	}
}
