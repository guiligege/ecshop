package org.guili.ecshop.business.impl.evaluate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.FileTools;
import org.guili.ecshop.util.SpiderRegex;

/**
 * 分析淘宝品牌及品牌下的商品
 * @ClassName:   AnalyzeTmallList 
 * @Description: 分析淘宝品牌及品牌下的商品(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-25 下午3:24:42 
 *
 *public class Singleton {
    private static Singleton uniqueInstance = null;
 
    private Singleton() {
       // Exists only to defeat instantiation.
    }
 
    public static Singleton getInstance() {
       if (uniqueInstance == null) {
           uniqueInstance = new Singleton();
       }
       return uniqueInstance;
    }
    // Other methods...
}
 */
public class AnalyzeTmallList {
	
	private static Logger logger=Logger.getLogger(AnalyzeTmallList.class);
	
	private List<String> tmallBrandList=new ArrayList<String>();
	private static String TMALLBRANDURL="http://brand.tmall.com/brandMap.htm?spm=a3200.2192433.0.0.RIdKpk";
	
	/**
	 * 单例实现tmall分析
	 */
	private static AnalyzeTmallList analyzeTmallList = null;
	private AnalyzeTmallList() {
		 tmallBrandList=this.AnalyzeTmallBrand(AnalyzeTmallList.TMALLBRANDURL);
	}
	public static AnalyzeTmallList getInstance() {
	      if (analyzeTmallList == null) {
	         analyzeTmallList = new AnalyzeTmallList();
	      }
	      return analyzeTmallList;
	}
	
	/**
	 * 根据商品列表链接获得商品详细页链接列表
	 * @param url	商品全部查询链接
	 * @return
	 */
	public List<String> getBrandItemsList(String url){
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(this.convertUrl(url), "gbk");
		if(htmltext.equals("")){
			return null;
		}
		//获得总页数
		//<b class="ui-page-s-len">
		String pageRegex = "<b class=\"ui-page-s-len\">(.*?)</b>";
		logger.info(this.convertUrl(url));
		String[] pages = this.analyzeTotalPage(htmltext,pageRegex,regex);
		int totalpage=this.getTotalPage(pages);
		//获得每页的数据
		List<String> itemsList=new ArrayList<String>();
		if(totalpage>0){
			for(int page=1;page<=totalpage;page++){
				String onePageUrl=this.convertUrl(url).substring(0, this.convertUrl(url).length()-1)+page;
				logger.debug("onePageUrl--->"+onePageUrl);
				itemsList.addAll(this.OnePageBrandItemsList(onePageUrl));
				
			}
		}
		return itemsList;
	}
	
	/**
	 * 分析商家商品分析
	 * @param html
	 * @param pageRegex
	 * @param regex
	 * @return
	 */
	public String[] analyzeTotalPage(String html,String pageRegex,SpiderRegex regex){
		//其他品牌处理
		//FileTools.appendToFile(EvaluateConfig.tmallbackurlline, html);
		String[] pages = regex.htmlregex(html,pageRegex,true);
		//优衣库特殊处理
		if(pages==null || pages.length==0){
			//<span class="page-info">1/38</span>
			 pageRegex = "<span class=\"page-info\">(.*?)</span>";
			 pages = regex.htmlregex(html,pageRegex,true);
		}
		if(pages==null || pages.length==0){
			 //<b class="ui-page-s-len">1/2</b>
			 pageRegex = "<span class=\"page-info\">(.*?)</span>";
			 pages = regex.htmlregex(html,pageRegex,true);
		}
		if(pages==null || pages.length==0){
			logger.info("该商家的商品为空！！！");
			return null;
		}
		if(pages[0].equals("0/0")){
			//<b class="ui-page-s-len">1/39</b>
			 pageRegex = "<b class=\"ui-page-s-len\">(.*?)</b>";
			 pages = regex.htmlregex(html,pageRegex,true);
		}
		return pages;
	}
	
	/**
	 * 获得一页的商品信息
	 * @param url
	 * @return
	 */
	public  List<String> OnePageBrandItemsList(String url){
		
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(url, "gbk");
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
		brandItem=null;
		return onePageBrandItems;
	}
	
	/**
	 * 传入淘宝商家url，传出列表页url
	 * @param url
	 * @return
	 */
	public  String convertUrl(String url){
		String returnUrl="";
		returnUrl=url.substring(0,url.substring("http://".length()).indexOf("/")+"http://".length())+"/search.htm?search=y&orderType=hotsell_desc&pageNum=1";
		return returnUrl;
	}
	
	/**
	 * 从列表页找到总页数
	 * @param pages
	 * @return
	 */
	public  int getTotalPage(String[] pages){
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
	public  List<String> AnalyzeTmallBrand(String url){
		List<String> tmallbrandList=new ArrayList<String>();
		//正则解析器
		SpiderRegex regex = new SpiderRegex();
		String htmltext=CommonTools.requestUrl(url, "gbk");
		if(htmltext.equals("")){
			return null;
		}
		//分析3种不同形式的品牌1
		//String brandItemRegex = "target=\"_blank\" href=\"(.*?)\" title=\"";
		String brandItemRegex = "<a target=\"_blank\" class=\"bFlis-con-mask\" href=\"(.*?)\">";
		String[] bigbrandItem = regex.htmlregex(htmltext,brandItemRegex,true);
		if(bigbrandItem!=null && bigbrandItem.length>0){
			for(String brandItem:bigbrandItem){
				brandItem=this.convertBrandItem(brandItem);
				if(brandItem.equals("")){
					continue;
				}
				if(tmallbrandList.contains(brandItem)){
					continue;
				}
				tmallbrandList.add(brandItem);
			}
		}
		//分析3种不同形式的品牌2
		brandItemRegex = "<a class=\"bFlil-link\" target=\"_blank\" href=\"(.*?)\" title=";
		String[] brandItems = regex.htmlregex(htmltext,brandItemRegex,true);
		if(brandItems!=null && brandItems.length>0){
			for(String brandItem:brandItems){
				brandItem=this.convertBrandItem(brandItem);
				if(brandItem.equals("")){
					continue;
				}
				if(tmallbrandList.contains(brandItem)){
					continue;
				}
				tmallbrandList.add(brandItem);
			}
		}
		//分析3种不同形式的品牌3
		brandItemRegex = "<a class=\"bFlil-link\" target=\"_blank\" title=\"(.*?)\">";
		String[] brandItem1 = regex.htmlregex(htmltext,brandItemRegex,true);
		if(brandItem1!=null && brandItem1.length>0){
			for(String brandItem:brandItem1){
				//判断当前是否有品牌链接
				if(brandItem.indexOf("href=\"")>0){
					brandItem=brandItem.substring(brandItem.indexOf("href=\"")+"href=\"".length());
				}
				brandItem=this.convertBrandItem(brandItem);
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
	public  String convertBrandItem(String brandItem){
		if(brandItem.contains("list.tmall.com")){
			return "";
		}
		if(brandItem.indexOf("?")>0){
			brandItem=brandItem.substring(0, brandItem.indexOf("?"));
		}
		brandItem=brandItem.substring(0,brandItem.indexOf("com/")+"com/".length());
		return brandItem;
	}
	
	public List<String> getTmallBrandList() {
		return tmallBrandList;
	}
	public void setTmallBrandList(List<String> tmallBrandList) {
		this.tmallBrandList = tmallBrandList;
	}
	//test
	public static void main(String[] args) {
		logger.debug(AnalyzeTmallList.getInstance().convertUrl("http://hanlidu.tmall.com/shop/view_shop.htm?spm=a220m.1000862.1000730.2.fhTbDq&user_number_id=728412204&rn=f2b6ed1084b76c27501189515f9279f2"));
		//AnalyzeTmallList.getBrandItemsList("http://timberland.tmall.com/?spm=a3200.2787281.a2223nt.7.F0V2tu");
		AnalyzeTmallList.getInstance();//.AnalyzeTmallBrand("http://brand.tmall.com/brandMap.htm?spm=a3200.2192433.0.0.RIdKpk");
	}
	
}
