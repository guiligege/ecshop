package org.guili.ecshop.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class ResourceUtil {
	public static String FILEPATH="config/platform";
	public static String ALIPAYFILEPATH="config/alipay_config";
	public static String EVALUATEFILEPATH="config/evaluate_config";
	public static String TMALLBACKPATH="config/file_config";
	public static String PROXYPATH="config/proxy_config";
//	private static ResourceBundle rb = ResourceBundle.getBundle(FILEPATH, Locale.getDefault());
	private final static Logger	log	= Logger.getLogger(ResourceUtil.class);
	
	/**
	 * 通过名称获取值
	 * @param name
	 * @return
	 */
	public static String getValue(String baseName,String key){
		ResourceBundle resourceBundle = getResourceBundle(baseName);
		return getValue(resourceBundle, key);
	}
	public static ResourceBundle getResourceBundle(String baseName){
		// Locale enLoc = new Locale("en", "US"); // 表示美国地区
		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
		return resourceBundle;
	}
	/**
	 * 获取Properties配置文件键值 ,采用java.util.ResourceBundle类的getBundle()方法来读取
	 * 
	 * @param resourceBundle
	 *            配置文件的包+类全名(不要尾缀)
	 * @param key
	 *            Properties配置文件键名
	 * @return 该键的值,不存在key,返回null
	 */
	public static String getValue(ResourceBundle resourceBundle,String key){
		if (!resourceBundle.containsKey(key)){
			log.info("resourceBundle don't containsKey:{}"+key);
		}else{
			try{
				String value = resourceBundle.getString(key);
				if(value==null || value.equals("")){
					log.info("resourceBundle has the key:{},but value is null/empty"+key);
				}
				return value;
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(ResourceUtil.getValue(TMALLBACKPATH,"tmallbackurl"));
	}
	
}