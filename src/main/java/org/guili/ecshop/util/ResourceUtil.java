package org.guili.ecshop.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtil {
	private static String FILEPATH="/resources/platform.properties";
	private static ResourceBundle rb = ResourceBundle.getBundle(FILEPATH, Locale.getDefault());
	
	/**
	 * 通过名称获取值
	 * @param name
	 * @return
	 */
	public static String getValueByName(String name){
		return rb.getString(name);
	}
	public static void main(String[] args) {
		System.out.println(ResourceUtil.getValueByName("imagepath"));
	}
}
