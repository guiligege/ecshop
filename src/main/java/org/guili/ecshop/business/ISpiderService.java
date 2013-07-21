package org.guili.ecshop.business;

import java.util.List;

import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.util.SpiderRegex;

/**
 * spider接口
 * @author guili
 */
public interface ISpiderService {
	/**
	 * 分析网站内容
	 * @param url
	 * @return
	 */
	public List<Semiconductor> analysisContent(String url);
	
	/**
	 * 获取单价列表
	 * @param url
	 * @return
	 */
	public List<String> analysisPrices(String url);
	/**
	 * 获取单价字符串
	 * @param url
	 * @return
	 */
	public String analysisPricesToString(String url);
	/**
	 * 组装功能描述
	 * @param headlist
	 * @param class2
	 * @return
	 */
	public String buildDiscription(List<String> headlist,String[] class2);
	
	/**
	 * 根据抓取的数据，创建excel(用于演示)
	 * @param semiconductorList
	 * @param file
	 */
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,String file);
	
	/**
	 * 获取规格
	 * @param basehtml
	 * @return
	 */
	public String analysisGuige(String basehtml,SpiderRegex regex);
	
	/**
	 * 获得图片链接
	 * @param basehtml
	 * @return
	 */
	public String analysisImageUrl(String basehtml,SpiderRegex regex);
	
	/**
	 * 获得价格链接
	 * @param basehtml
	 * @return
	 */
	public String analysisPriceUrl(String basehtml,SpiderRegex regex);
	
	/**
	 * 获取分页数
	 * @param basehtml
	 * @param regex
	 * @return
	 */
	public int getPageCount(String basehtml,SpiderRegex regex);
	
	/**
	 * 分析网站链接
	 * @param url
	 */
	public void analysisService();

}
