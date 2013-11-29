package org.guili.ecshop.business.impl.evaluate;

import org.guili.ecshop.util.ResourceUtil;

/**
 * 商品评价配置
 * @ClassName:   EvaluateConfig 
 * @Description: 商品评价配置(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-13 下午2:26:14 
 *
 */
public class EvaluateConfig {
	/**
	 * 淘宝常量
	 */
	/**
	 * 淘宝商家总评请求
	 */
	public static String taobao_evaluate_total_url=ResourceUtil.getValue(ResourceUtil.EVALUATEFILEPATH,"taobao_evaluate_total_url");
	
	/**
	 * 淘宝单个商品评论请求
	 */
	public static String taobao_evaluate_url=ResourceUtil.getValue(ResourceUtil.EVALUATEFILEPATH,"taobao_evaluate_url");
	
	/**
	 * 天猫单个商品评论请求
	 */
	public static String tmall_evaluate_url=ResourceUtil.getValue(ResourceUtil.EVALUATEFILEPATH,"tmall_evaluate_url");
	
	/**
	 * 天猫抓取url备份
	 */
	public static String tmallbackurl=ResourceUtil.getValue(ResourceUtil.TMALLBACKPATH,"tmallbackurl");
	
	/**
	 * 天猫抓取url line备份
	 */
	public static String tmallbackurlline=ResourceUtil.getValue(ResourceUtil.TMALLBACKPATH,"tmallbackline");
	
	/**
	 * 错误url日志
	 */
	public static String tmalllogurl=ResourceUtil.getValue(ResourceUtil.TMALLBACKPATH,"tmalllogurl");
	
	
}
