package org.guili.ecshop.business.credit;

import java.util.List;
import java.util.Map;

import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;
import org.springframework.ui.ModelMap;

/**
 * 
 * @ClassName:   IProductEvaluate 
 * @Description: 统一商品评价接口(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-13 上午10:50:29 
 *
 */
public interface IProductEvaluateService {
	/**
	 * 根据卖家id和卖家商品id查询卖家总体信誉
	 * @param sellerid		卖家id
	 * @param productid		产品id
	 * @return
	 */
	public double sellerTotalEvaluate(Object obj);
	
	/**
	 * 单个商品的评价
	 * @param sellerid		卖家id
	 * @param productid		产品id
	 * @return
	 */
	public double singleProductEvaluate(Object obj);
	
	/**
	 * 通过url活动需要的数据
	 * @param producturl
	 * @return
	 */
	public Map<String, String> analyzeUrl(String producturl);
	
	/**
	 * 计算商品评价
	 * @param url
	 * @return
	 */
	public  double evaluateCalculate(String url,ModelMap modelMap);
	
	/**
	 * 计算商品评价
	 * @param url
	 * @param modelMap
	 * @param tmallAnalyzeBeanList
	 * @return
	 */
	public double evaluateCalculate(String url, ModelMap modelMap,List<TmallAnalyzeBean> tmallAnalyzeBeanList) throws Exception;
	
	public void AnalyzeTmallBrand();
}
