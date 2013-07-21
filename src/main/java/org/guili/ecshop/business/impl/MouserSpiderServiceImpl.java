package org.guili.ecshop.business.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.business.ISemiconductorService;
import org.guili.ecshop.business.ISpiderService;
import org.guili.ecshop.util.ExcelWriter;
import org.guili.ecshop.util.ResourceProperty;
import org.guili.ecshop.util.ResourceUtil;
import org.guili.ecshop.util.SpiderRegex;

/**
 * mouser spider
 * @author guili
 */
public class MouserSpiderServiceImpl implements ISpiderService {

	private static String PRICESPLIT=ResourceUtil.getValue(ResourceUtil.FILEPATH,"PRICESPLIT");
	private static Logger log=Logger.getLogger(DigikeySpiderServiceImpl.class);
	private static String BASEURL=ResourceProperty.MOUSECOM;
	private ISemiconductorService semiconductorService=null;
	
	public void setSemiconductorService(ISemiconductorService semiconductorService) {
		this.semiconductorService = semiconductorService;
	}

	@Override
	public List<Semiconductor> analysisContent(String url) {
		log.info("mouser run!");
		//网站地址
//		String baseurl="http://www.mouser.cn";
		SpiderRegex regex = new SpiderRegex();
		List<Semiconductor> classlist = new ArrayList<Semiconductor>();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//分析当前页数
		int pagecount=getPageCount(htmltext,regex);
		log.debug("pagecount-->"+pagecount);
		log.info("mouser pagecount:"+pagecount);
		//循环分页
		for(int page=1;page<=pagecount;page++){
			int mouserpage=(page-1)*25;
			String localurl=url.substring(0, url.indexOf("No=")+"No=".length());
			localurl=localurl+mouserpage;
			log.debug("localurl--->"+localurl);
			log.info("mouser run page:"+page);
			htmltext=regex.gethtmlContent(localurl,"UTF-8");
			//匹配需要的那部分网页
			String reg = "<tr class=\"SearchResultColumnHeading\" valign=\"top\" style=\"font-weight:bold;height:100%;\">(.*?)<\\/tr>";
			String[] header = regex.htmlregex(htmltext,reg,true);
			//头部
			List<String> headlist=new ArrayList<String>();
			reg="<th.*?>(.*?)<\\/th>";
			String[] headcontent=regex.htmlregex(header[0],reg,false);
			//防止分页没有
			if(headcontent==null || headcontent.length==0){
				continue;
			}
			for(int i=1;i<headcontent.length;i++){
				headlist.add(headcontent[i]);
			}
			//分类
			String baseclass="";
			reg = "<strong><\\/strong>(.*?)<hr \\/>";
			String[] baseclasss=regex.htmlregex(htmltext,reg,false);
			if(baseclasss!=null && baseclasss.length>0){
				baseclass=baseclasss[0].replaceAll(">", "\\$\\$");
			}
			//具体内容部分的拆分
			//奇数部分
			reg = "<tr class=\"SearchResultsRowOdd\">(.*?)</td>				</tr>";
			String[] clcontent = regex.htmlregex(htmltext,reg,true);
			//定义变量存放需要处理的数据
			String imageurl="",guige="",prices="";
			//防止页面访问错误
			if(clcontent==null || clcontent.length==0){
				continue;
			}
			if(clcontent!=null && clcontent.length>0){
				for(int j=0;j<clcontent.length;j++){
					try {
						
						Semiconductor semiconductor=new Semiconductor();
						reg = "<td>(.*?)<\\/td>";
						String[] class2 = regex.htmlregex(clcontent[j],reg,false);
						reg = "<td>(.*?)<\\/td>";
						String[] tds = regex.htmlregex(clcontent[j],reg,true);
						//特殊处理
						//价格url
						String priceurl=this.analysisPriceUrl(clcontent[j], regex);
						//图片
						//初始化数据
						imageurl="";guige="";prices="";
						String imagename="";
						if(tds!=null && tds.length>0){
							imageurl=this.analysisImageUrl(tds[0], regex);
							if(imageurl!=null && !imageurl.equals("")){
								imageurl=BASEURL+imageurl.substring(1, imageurl.length());
								imagename=imageurl.substring(imageurl.lastIndexOf("/")+1);
							}
							guige=this.analysisGuige(tds[5], regex);
							//把相对地址转化为绝对地址
							if(!guige.equals("") && !guige.startsWith("http")){
								guige=BASEURL+guige;
							}
						}
						if(priceurl!=null && !"".equals(priceurl)){
							prices=this.analysisPricesToString(priceurl);
						}
						log.debug("imageurl:"+imageurl+",guige="+guige+",prices="+prices);
						//特殊处理结束
						if(class2!=null&& class2.length>0){
							//转换为对象
							//semiconductor.setGuige(class2[6]);
//							semiconductor.setImagepath(class2[1]);
							semiconductor.setBasesiteclass(baseclass);
							semiconductor.setSourcesite(ResourceProperty.MOUSECOM);
							semiconductor.setImagepath(imageurl);
							semiconductor.setImagename(imagename);
							semiconductor.setProducterkey(class2[1]);
							semiconductor.setCode(class2[2]);
							semiconductor.setProducter(class2[3]);
							semiconductor.setDesc(class2[4]);
							semiconductor.setGuige(guige);
							semiconductor.setDiscount(class2[6]);
//							semiconductor.setPrice(class2[8]);
							semiconductor.setPrice(prices);
							//新加创建时间
							semiconductor.setCreateTime(new Date());
							semiconductor.setLowestcount(class2.length>=9?"1":"受限供货情况");
							if(headlist.size()>9 && class2.length>=9){
								semiconductor.setFunction(buildDiscription(headlist,class2));
							}else{
								semiconductor.setFunction("");
							}
							classlist.add(semiconductor);
							semiconductor=new Semiconductor();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			//偶数部分
			reg = "<tr class=\"SearchResultsRowEven\">(.*?)</td>				</tr>";
			String[] oushuclcontent = regex.htmlregex(htmltext,reg,true);
			//定义变量存放需要处理的数据
			imageurl="";guige="";prices="";
			if(oushuclcontent!=null && oushuclcontent.length>0){
				for(int j=0;j<oushuclcontent.length;j++){
//					try {
						
						Semiconductor semiconductor=new Semiconductor();
						reg = "<td>(.*?)<\\/td>";
						String[] class2 = regex.htmlregex(oushuclcontent[j],reg,false);
						reg = "<td>(.*?)<\\/td>";
						String[] tds = regex.htmlregex(oushuclcontent[j],reg,true);
						//特殊处理
						String priceurl=this.analysisPriceUrl(oushuclcontent[j], regex);
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
							if(imageurl!=null && !imageurl.equals("")){
								imageurl=BASEURL+imageurl.substring(1, imageurl.length());
							}
							//说明书
//							String shuoming="";
//							shuoming=tds[5].substring(tds[5].indexOf("href=\"")+"href=\"".length(), tds[5].indexOf("target")-2);
							guige=this.analysisGuige(tds[5], regex);
							//把相对地址转化为绝对地址
							if(!guige.equals("") && !guige.startsWith("http")){
								guige=BASEURL+guige;
							}
						}
						if(priceurl!=null && !"".equals(priceurl)){
							prices=this.analysisPricesToString(priceurl);
						}
						log.debug("imageurl:"+imageurl+",guige="+guige+",prices="+prices);
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
							//新加创建时间
							semiconductor.setCreateTime(new Date());
							semiconductor.setLowestcount(class2.length>=9?"1":"受限供货情况");
							if(headlist.size()>9 && class2.length>=9){
								semiconductor.setFunction(buildDiscription(headlist,class2));
							}else{
								semiconductor.setFunction("");
							}
							semiconductor.setBasesiteclass(baseclass);
							semiconductor.setSourcesite(ResourceProperty.MOUSECOM);
							classlist.add(semiconductor);
							semiconductor=new Semiconductor();
						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			}
			log.debug("one page success");
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
		log.debug(" 开始导出Excel文件 ");
		  File f = new File("F:\\sources\\qtmouser.xls");
		  ExcelWriter e = new ExcelWriter();
		  try {
		   e = new ExcelWriter(new FileOutputStream(f));
		  } catch (FileNotFoundException e1) {
		   e1.printStackTrace();
		  }
		  //excel头
		  e.createRow(0);
		  e.setCell(0, "规格书 ");
		  e.setCell(1, "图像");
		  e.setCell(2, "Digi-Key");
		  e.setCell(3, "零件编号");
		  e.setCell(4, "制造商");
		  e.setCell(5, "描述");
		  e.setCell(6, "现有数量");
		  e.setCell(7, "单价 (USD)");
		  e.setCell(8, "最低订购数量");
		  e.setCell(9, "功能描述");
		  e.setCell(10, "网站");
		  e.setCell(11, "源分类");
		  if(semiconductorList!=null && semiconductorList.size()>0){
			  for(int i=0;i<semiconductorList.size();i++){
				  Semiconductor semiconductor=semiconductorList.get(i);
				  e.createRow(i+1);
				  e.setCell(0, semiconductor.getGuige());
				  e.setCell(1, semiconductor.getImagepath());
				  e.setCell(2, semiconductor.getProducterkey());
				  e.setCell(3, semiconductor.getCode());
				  e.setCell(4, semiconductor.getProducter());
				  e.setCell(5, semiconductor.getDesc());
				  e.setCell(6, semiconductor.getDiscount());
				  e.setCell(7, semiconductor.getPrice());
				  e.setCell(8, semiconductor.getLowestcount());
				  e.setCell(9, semiconductor.getFunction());
				  e.setCell(10, semiconductor.getSourcesite());
				  e.setCell(11, semiconductor.getBasesiteclass());
			  }
		  }
		  try {
		   e.export();
		   log.debug(" 导出Excel文件[成功] ");
		  } catch (IOException ex) {
		   log.debug(" 导出Excel文件[失败] ");
		   ex.printStackTrace();
		  }

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
		if(shuoming.indexOf("../")>=0){
			shuoming="/"+shuoming.replaceAll("\\.\\.\\/", "");
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
		String url="";
		if(urls!=null && urls.length>0){
			url=urls[0];
			if(urls[0].indexOf("../")>=0){
				url=urls[0].replaceAll("\\.\\.\\/", "");
			}
			priceurl=BASEURL+url;
		}
		if(urls2!=null && urls2.length>0){
			url=urls2[0];
			if(urls2[0].indexOf("../")>=0){
				url=urls2[0].replaceAll("\\.\\.\\/", "");
			}
			priceurl=BASEURL+url;
		}
//		if(priceurl.indexOf("../")>=0){
//			priceurl=priceurl.replaceAll("../", "");
//		}
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
//			List<Semiconductor> semiconductorList=scs.analysisContent("http://www.mouser.cn/Electromechanical/Industrial-Automation/Measurement/_/N-b12pl/?No=0");
//			scs.createSemiconductorExcel(semiconductorList, "");
//			String temp=scs.analysisPricesToString("http://www.mouser.cn/ProductDetail/Skyworks-Solutions-Inc/SKY12207-306LF/?qs=sGAEpiMZZMvplms98TlKYxZLCcC6DAiBNMTlNJl6JDk%3d");
//			System.out.println("analysisPricesToString-->"+temp);
			scs.analysisService();
			log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
		}

		@Override
		public void analysisService() {
			//通过网址获取网页内容
			SpiderRegex regex = new SpiderRegex();
			List<String> urls=new ArrayList<String>();
			String htmltext = regex.gethtmlContent("http://www.mouser.cn/search/default.aspx","UTF-8");
			//匹配需要的那部分网页
			String regbig = "<ul class=\"sub-cats\">(.*?)<\\/ul>";
			String[] bigcontent = regex.htmlregex(htmltext,regbig,true);
			int counturl=0;
			if(bigcontent!=null && bigcontent.length>0){
				for(int i=0;i<bigcontent.length;i++){
					regbig = "href=\"\\.\\.\\/(.*?)\"";
					String[] smallContent=regex.htmlregex(bigcontent[i],regbig,true);
					log.debug(smallContent.length);
					for(String smallurl:smallContent){
						log.info("mouser run!");
						log.debug("smallurl--->"+BASEURL+smallurl+"?No=0");
						counturl+=1;
						List<Semiconductor> semiconductorList=analysisContent(BASEURL+smallurl+"?No=0");
						//保存或更新到数据库
						if(semiconductorList!=null && semiconductorList.size()>0){
							semiconductorService.pageservice(semiconductorList);
						}
					}
				}
				log.debug("counturl-->"+counturl);
			}
		}
}
