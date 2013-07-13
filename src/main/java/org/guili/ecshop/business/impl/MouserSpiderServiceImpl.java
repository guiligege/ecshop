package org.guili.ecshop.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.business.ISpiderService;
import org.guili.ecshop.util.ResourceUtil;
import org.guili.ecshop.util.SpiderRegex;

/**
 * mouser spider
 * @author guili
 */
public class MouserSpiderServiceImpl implements ISpiderService {

	private static String PRICESPLIT=ResourceUtil.getValue(ResourceUtil.FILEPATH,"PRICESPLIT");
	private static Logger log=Logger.getLogger(DigikeySpiderServiceImpl.class);
	private static String BASEURL=ResourceUtil.getValue(ResourceUtil.FILEPATH,"MOUSECOM");
	@Override
	public List<Semiconductor> analysisContent(String url) {
		//网站地址
//		String baseurl="http://www.mouser.cn";
		SpiderRegex regex = new SpiderRegex();
		List<Semiconductor> classlist = new ArrayList<Semiconductor>();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//分析当前页数
		int pagecount=getPageCount(htmltext,regex);
		log.debug("pagecount-->"+pagecount);
		
		//循环分页
		for(int page=1;page<=pagecount;page++){
			int mouserpage=(page-1)*25;
			String localurl=url.substring(0, url.indexOf("No=")+"No=".length());
			localurl=localurl+mouserpage;
			log.debug("localurl--->"+localurl);
			htmltext=regex.gethtmlContent(localurl,"UTF-8");
			//匹配需要的那部分网页
			String reg = "<tr class=\"SearchResultColumnHeading\" valign=\"top\" style=\"font-weight:bold;height:100%;\">(.*?)<\\/tr>";
			String[] header = regex.htmlregex(htmltext,reg,true);
			//头部
			List<String> headlist=new ArrayList<String>();
			reg="<th.*?>(.*?)<\\/th>";
			String[] headcontent=regex.htmlregex(header[0],reg,false);
			for(int i=1;i<headcontent.length;i++){
				headlist.add(headcontent[i]);
			}
			//具体内容部分的拆分
			reg = "<tr class=\"SearchResultsRowOdd\">(.*?)</td>				</tr>";
			String[] clcontent = regex.htmlregex(htmltext,reg,true);
			//定义变量存放需要处理的数据
			String imageurl="",guige="",prices="";
			if(clcontent!=null && clcontent.length>0){
				for(int j=0;j<clcontent.length;j++){
//					try {
						
						Semiconductor semiconductor=new Semiconductor();
						reg = "<td>(.*?)<\\/td>";
						String[] class2 = regex.htmlregex(clcontent[j],reg,false);
						reg = "<td>(.*?)<\\/td>";
						String[] tds = regex.htmlregex(clcontent[j],reg,true);
						//特殊处理
						//价格url
//						reg = "<a title=\"单击查看其他价格间断。\" href=\"..\\/..\\/..\\/..\\/(.*?)\">查看";
//						String[] urls = regex.htmlregex(clcontent[j],reg,false);
//						if(urls!=null && urls.length>0){
//							String priceurl=BASEURL+urls[0];
//						}
						String priceurl=this.analysisPriceUrl(clcontent[j], regex);
						//图片
						//<img title='Skyworks Solutions, Inc. SKY12207-306LF' alt='Skyworks Solutions, Inc. SKY12207-306LF' id=826709117 src='/images/skyworks/sm/qfn16.jpg' />
//						reg="<a.*?>(.*?)</a>";
//						String[] imgs = regex.htmlregex(tds[0],reg,true);
//						int start=imgs[0].indexOf("src='")+"src='".length();
////						int end=imgs[0].indexOf("' />");
//						String img=imgs[0].substring(start,imgs[0].length()-3);
						//初始化数据
						imageurl="";guige="";prices="";
						if(tds!=null && tds.length>0){
							imageurl=this.analysisImageUrl(tds[0], regex);
							//说明书
//							String shuoming="";
//							shuoming=tds[5].substring(tds[5].indexOf("href=\"")+"href=\"".length(), tds[5].indexOf("target")-2);
							guige=this.analysisGuige(tds[5], regex);
						}
						if(priceurl!=null && !"".equals(priceurl)){
							prices=this.analysisPricesToString(priceurl);
						}
						//特殊处理结束
						if(class2!=null&& class2.length>0){
							//转换为对象
							//semiconductor.setGuige(class2[6]);
//							semiconductor.setImagepath(class2[1]);
							semiconductor.setImagepath(imageurl);
							semiconductor.setProducterkey(class2[1]);
							semiconductor.setCode(class2[2]);
							semiconductor.setProducter(class2[3]);
							semiconductor.setDesc(class2[4]);
							semiconductor.setGuige(guige);
							semiconductor.setDiscount(class2[6]);
//							semiconductor.setPrice(class2[8]);
							semiconductor.setPrice(prices);
							semiconductor.setLowestcount(class2.length>=9?class2[8]:"受限供货情况");
							if(headlist.size()>9 && class2.length>=9){
								semiconductor.setFunction(buildDiscription(headlist,class2));
							}else{
								semiconductor.setFunction("");
							}
							classlist.add(semiconductor);
							semiconductor=new Semiconductor();
						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			}
			
		}
		return classlist;
	}

	@Override
	public List<String> analysisPrices(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 分析mouser的价格
	 */
	@Override
	public String analysisPricesToString(String url) {
		SpiderRegex regex = new SpiderRegex();
//		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		StringBuffer price=new StringBuffer("");
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//匹配需要的那部分网页
		String reg = "<td style=\"padding:0 5px;\">(.*?)<\\/tr>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		if(clcontent!=null && clcontent.length>0){
			for(int i =0;i<clcontent.length;i++){
				reg = "<a.*?>(.*?)<\\/a>";
				String[] mousecounts =regex.htmlregex(clcontent[i],reg,false);
				reg = "<span.*?>(.*?)<\\/span>";
				String[] mouseprices =regex.htmlregex(clcontent[i],reg,false);
				csb.append(mousecounts[0]).append(PRICESPLIT).append(mouseprices[0]);
				price.append(csb.toString()+"$$");
				csb = new StringBuffer();
			}
		}
		if(price!=null && !price.equals("")){
			price.substring(0, price.length()-2);
		}
		log.debug(" price---->"+price.toString());
		return price.toString();
	}

	/**
	 * 构建
	 */
	@Override
	public String buildDiscription(List<String> headlist, String[] class2) {
		String function="";
		if(headlist!=null && headlist.size()>0){
			for(int k=9;k<headlist.size();k++){
				function+=headlist.get(k)+":"+class2[k-1]+"$$";
			}
			if(!function.equals("")){
				function=function.substring(0, function.length()-2);
			}
			log.debug("function---->"+function);
		}
		return function;
	}

	@Override
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,
			String file) {
		// TODO Auto-generated method stub

	}

	/**
	 * 分析规格
	 */
	@Override
	public String analysisGuige(String basehtml, SpiderRegex regex) {
		String shuoming="";
		if(basehtml!=null && !"".equals(basehtml)){
			try {
				shuoming=basehtml.substring(basehtml.indexOf("href=\"")+"href=\"".length(), basehtml.indexOf("target")-2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return shuoming;
	}

	/**
	 * 分析图片路径
	 */
	@Override
	public String analysisImageUrl(String basehtml, SpiderRegex regex) {
		String reg="<a.*?>(.*?)</a>";
		String[] imgs = regex.htmlregex(basehtml,reg,true);
		String img="";
		if(imgs!=null && imgs.length>0){
			int start=imgs[0].indexOf("src='")+"src='".length();
			img=imgs[0].substring(start,imgs[0].length()-3);
		}
		return img;
	}

	/**
	 * 分析价格链接
	 */
	@Override
	public String analysisPriceUrl(String basehtml, SpiderRegex regex) {
		String reg = "<a title=\"单击查看其他价格间断。\" href=\"..\\/..\\/..\\/..\\/(.*?)\">查看";
		String[] urls = regex.htmlregex(basehtml,reg,false);
		reg = "<a href='..\\/..\\/..\\/..\\/(.*?)' >查看";
		String[]urls2=regex.htmlregex(basehtml,reg,false);
		String priceurl="";
		if(urls!=null && urls.length>0){
			
			priceurl=BASEURL+urls[0];
		}
		if(urls2!=null && urls2.length>0){
			
			priceurl=BASEURL+urls2[0];
		}
		return priceurl;
	}

	/**
	 * 分析页面分页数
	 */
	@Override
	public int getPageCount(String basehtml, SpiderRegex regex) {
		String regpag="<span id=\"ctl00_ContentMain_PagerTop\">(.*?)<\\/a><\\/span>";
		String[] pagenums = regex.htmlregex(basehtml,regpag,true);
		regpag="<a.*?>(.*?)<\\/a>";
		String[] pagecounts=regex.htmlregex(pagenums[0],regpag,false);
		int pagecount=1;
		if(pagecounts!=null && pagecounts.length>0){
			pagecount=Integer.parseInt(pagecounts[pagecounts.length-1]);
		}
//		if(pagecounts!=null && pagecounts.length>0){
//			pagecount=Integer.parseInt(pagecounts[0]);
//		}
		log.debug("pagecount-->"+pagecount);
		return pagecount;
	}

	//test
		public static void main(String[] args) throws Exception {
			Date start=new Date();
			ISpiderService scs = new MouserSpiderServiceImpl();
			List<Semiconductor> semiconductorList=scs.analysisContent("http://www.mouser.cn/Semiconductors/Discrete-Semiconductors/_/N-awhng/?No=0");
			scs.createSemiconductorExcel(semiconductorList, "");
//			String temp=scs.analysisPricesToString("http://www.mouser.cn/ProductDetail/Skyworks-Solutions-Inc/SKY12207-306LF/?qs=sGAEpiMZZMvplms98TlKYxZLCcC6DAiBNMTlNJl6JDk%3d");
//			System.out.println("analysisPricesToString-->"+temp);
			log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
		}
}
