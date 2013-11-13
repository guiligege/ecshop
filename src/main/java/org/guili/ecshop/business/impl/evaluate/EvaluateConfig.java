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
	
	
}