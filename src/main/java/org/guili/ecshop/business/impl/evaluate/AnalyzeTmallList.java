package org.guili.ecshop.business.impl.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.SpiderRegex;

/**
 * 分析淘宝品牌及品牌下的商品
 * @ClassName:   AnalyzeTmallList 
 * @Description: 分析淘宝品牌及品牌下的商品(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-25 下午3:24:42 
 *
 */
public class AnalyzeTmallList {
	
	private static Logger logger=Logger.getLogger(AnalyzeTmallList.class);
	
	/**
	 * 根据商品列表链接获得商品详细页链接列表
	 * @param url	商品全部查询链接
	 * @return
	 */
	public static List<String> getBrandItemsList(String url){
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(AnalyzeTmallList.convertUrl(url), "gbk");
		if(htmltext.equals("")){
			return null;
		}
		//获得总页数
		String pageRegex = "<b class=\"ui-page-s-len\">(.*?)</b>";
		String[] pages = regex.htmlregex(htmltext,pageRegex,true);
		if(pages==null || pages.length==0){
			//<span class="page-info">1/38</span>
			 pageRegex = "<span class=\"page-info\">(.*?)</span>";
			 pages = regex.htmlregex(htmltext,pageRegex,true);
		}
		if(pages==null || pages.length==0){
			return null;
		}
		int totalpage=AnalyzeTmallList.getTotalPage(pages);
		//获得每页的数据
		List<String> itemsList=new ArrayList<String>();
		if(totalpage>0){
			for(int page=1;page<=totalpage;page++){
				String onePageUrl=AnalyzeTmallList.convertUrl(url).substring(0, AnalyzeTmallList.convertUrl(url).length()-1)+page;
				logger.debug("onePageUrl--->"+onePageUrl);
				AnalyzeTmallList.OnePageBrandItemsList(onePageUrl);
				
			}
		}
		return itemsList;
	}
	
	/**
	 * 获得一页的商品信息
	 * @param url
	 * @return
	 */
	public static List<String> OnePageBrandItemsList(String url){
		
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(AnalyzeTmallList.convertUrl(url), "gbk");
		if(htmltext.equals("")){
			return null;
		}
		String brandItemRegex = "href=\"(.*?)\"";
		String[] brandItem = regex.htmlregex(htmltext,brandItemRegex,true);
		//单个列表商品详细
		if(brandItem==null || brandItem.length==0){
			return null;
		}
		List<String> onePageBrandItems=new ArrayList<String>();
		for(String myBrandItem:brandItem){
			if(myBrandItem.startsWith("http://detail.tmall.com/item.htm")){
				if(myBrandItem.contains("&")){
					myBrandItem=myBrandItem.substring(0, myBrandItem.indexOf("&"));
				}
				if(!myBrandItem.contains("id=")){
					continue;
				}
				if(!onePageBrandItems.contains(myBrandItem)){
					onePageBrandItems.add(myBrandItem);
					logger.debug("onePageBrandItems has :"+myBrandItem);
				}
			}
		}
		
		return onePageBrandItems;
	}
	
	/**
	 * 传入淘宝商家url，传出列表页url
	 * @param url
	 * @return
	 */
	public static String convertUrl(String url){
		String returnUrl="";
		returnUrl=url.substring(0,url.substring("http://".length()).indexOf("/")+"http://".length())+"/search.htm?search=y&pageNo=1";
		return returnUrl;
	}
	
	/**
	 * 从列表页找到总页数
	 * @param pages
	 * @return
	 */
	public static int getTotalPage(String[] pages){
		int totalpage=0;
		if(pages!=null && pages.length==1){
			totalpage=Integer.parseInt(pages[0].split("/")[1]);
		}
		return totalpage;
	}
	
	/**
	 * 分析天猫品牌列表
	 * @param url 
	 * @return  返回list like：http://uniqlo.tmall.com/
	 */
	public static List<String> AnalyzeTmallBrand(String url){
		List<String> tmallbrandList=new ArrayList<String>();
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(url, "gbk");
		if(htmltext.equals("")){
			return null;
		}
		//String brandItemRegex = "target=\"_blank\" href=\"(.*?)\" title=\"";
		String brandItemRegex = "<a target=\"_blank\" class=\"bFlis-con-mask\" href=\"(.*?)\">";
		String[] bigbrandItem = regex.htmlregex(htmltext,brandItemRegex,true);
		if(bigbrandItem!=null && bigbrandItem.length>0){
			for(String brandItem:bigbrandItem){
				brandItem=AnalyzeTmallList.convertBrandItem(brandItem);
				if(brandItem.equals("")){
					continue;
				}
				if(tmallbrandList.contains(brandItem)){
					continue;
				}
				tmallbrandList.add(brandItem);
			}
		}
		//查看小的商标
		brandItemRegex = "<a class=\"bFlil-link\" target=\"_blank\" href=\"(.*?)\" title=";
		String[] brandItems = regex.htmlregex(htmltext,brandItemRegex,true);
		if(brandItems!=null && brandItems.length>0){
			for(String brandItem:brandItems){
				brandItem=AnalyzeTmallList.convertBrandItem(brandItem);
				if(brandItem.equals("")){
					continue;
				}
				if(tmallbrandList.contains(brandItem)){
					continue;
				}
				tmallbrandList.add(brandItem);
			}
		}
		//查看特殊排列商标
		brandItemRegex = "<a class=\"bFlil-link\" target=\"_blank\" title=\"(.*?)\">";
		String[] brandItem1 = regex.htmlregex(htmltext,brandItemRegex,true);
		if(brandItem1!=null && brandItem1.length>0){
			for(String brandItem:brandItem1){
				//判断当前是否有品牌链接
				if(brandItem.indexOf("href=\"")>0){
					brandItem=brandItem.substring(brandItem.indexOf("href=\"")+"href=\"".length());
				}
				brandItem=AnalyzeTmallList.convertBrandItem(brandItem);
				if(brandItem.equals("")){
					continue;
				}
				if(tmallbrandList.contains(brandItem)){
					continue;
				}
				tmallbrandList.add(brandItem);
			}
		}
		return tmallbrandList;
	}
	
	/**
	 * 转换brandItem
	 */
	public static String convertBrandItem(String brandItem){
		if(brandItem.contains("list.tmall.com")){
			return "";
		}
		if(brandItem.indexOf("?")>0){
			brandItem=brandItem.substring(0, brandItem.indexOf("?"));
		}
		brandItem=brandItem.substring(0,brandItem.indexOf("com/")+"com/".length());
		return brandItem;
	}
	//test
	public static void main(String[] args) {
		logger.debug(AnalyzeTmallList.convertUrl("http://hanlidu.tmall.com/shop/view_shop.htm?spm=a220m.1000862.1000730.2.fhTbDq&user_number_id=728412204&rn=f2b6ed1084b76c27501189515f9279f2"));
		//AnalyzeTmallList.getBrandItemsList("http://timberland.tmall.com/?spm=a3200.2787281.a2223nt.7.F0V2tu");
		AnalyzeTmallList.AnalyzeTmallBrand("http://brand.tmall.com/brandMap.htm?spm=a3200.2192433.0.0.RIdKpk");
	}
	
}
