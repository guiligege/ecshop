package org.guili.ecshop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.spider.*;

public class StoreClassSpider {
	//抓取的列数
//	private static int COLUMS=25;
	private static String PRICESPLIT="-";
	private static Logger log=Logger.getLogger(StoreClassSpider.class);
	//抓取网站所有类别
	public List<String> classesContent() {
		SpiderRegex regex = new SpiderRegex();
		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent("http://www.360buy.com/allSort.aspx","gbk");
		//匹配需要的那部分网页
		String reg = "<div class=\"mt\".*?>(.*?)<div class=\"m\".*?>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		for(int i =0;i<clcontent.length;i++){
			reg = "<h2>(.*?)<\\/h2>";
			String[] class1 = regex.htmlregex(clcontent[i],reg,false);
			if(class1 == null&& class1.length ==0){
				return null;
			}
			reg = "<dl.*?>(.*?)<\\/dl>";
			String[] cl2content =regex.htmlregex(clcontent[i],reg,true);
			if(cl2content!=null&& cl2content.length>0){
				for(int j = 0;j<cl2content.length;j++){
					reg = "<dt>(.*?)<\\/dt>";
					String[] class2 = regex.htmlregex(cl2content[j],reg,false);
					if(class2!=null&& class2.length>0){
						for(int k = 0;k<class2.length;k++){
							reg = "<em>(.*?)<\\/em>";
							String[] class3 = regex.htmlregex(cl2content[j],reg,false);
							if(class3!=null&& class3.length>0){
								for(int m = 0;m<class3.length;m++){
									csb.append(class1[0]).append("$$");
									csb.append(class2[k]).append("$$");
									csb.append(class3[m]);
									classlist.add(csb.toString());
									csb = new StringBuffer();
									
								}
							}
						}
					}
				}
			}
		}
		for(int i =0;i<classlist.size();i++){
			log.debug(classlist.get(i));
		}
		return classlist;
	}
	
	//插入到数据库
	public void insertToDB(List<String> classlist){
		//todo
	}
	
	/**
	 * 抓取百度手机软件
	 * @return
	 */
	public String getBaiDuSoft(){
		SpiderRegex regex = new SpiderRegex();
		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		String htmltext = regex.gethtmlContent("http://as.baidu.com/a/item?docid=2547668&pre=web_am_rank&pos=rank_3000_4&f=web_alad%40next%40rank_3000_4","utf-8");
		log.debug(htmltext);
		String reg = "<div class=\"com\">(.*?)<div class=\"info-middle\">";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		csb.append(clcontent[0].toString());
		return csb.toString();
	}
	
	//抓取网站所有类别
	public List<String> digikeyContent() {
		SpiderRegex regex = new SpiderRegex();
		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent("http://www.digikey.cn/product-search/zh/sensors-transducers/irda-transceiver-modules/1966896?stock=1","UTF-8");
		//匹配需要的那部分网页
		String reg = "<tbody>(.*?)<\\/table>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		for(int i =0;i<clcontent.length;i++){
			reg = "<tr itemscope(.*?)<\\/tr>";
			String[] cl2content =regex.htmlregex(clcontent[i],reg,true);
			if(cl2content!=null&& cl2content.length>0){
				for(int j = 0;j<cl2content.length;j++){
					reg = "<td.*?>(.*?)<\\/td>";
					String[] class2 = regex.htmlregex(cl2content[j],reg,false);
					if(class2!=null&& class2.length>0){
						for(int k = 0;k<class2.length;k++){
							csb.append(class2[k]).append("--");
						}
						classlist.add(csb.toString());
						csb = new StringBuffer();
					}
				}
			}
		}
		for(int i =0;i<classlist.size();i++){
			log.debug(classlist.get(i));
		}
		return classlist;
	}
	
	//抓取网站所有类别www.digikey.cn
	public List<Semiconductor> digikeyContent1(String url) {
		//网站地址
		String baseurl="http://www.digikey.cn";
		SpiderRegex regex = new SpiderRegex();
		List<Semiconductor> classlist = new ArrayList<Semiconductor>();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//分析当前页数
		int pagecount=getPageCount(htmltext,regex);
		log.debug("pagecount-->"+pagecount);
		
		//循环分页
		for(int page=1;page<=pagecount;page++){
			String localurl=url.substring(0, url.length()-1);
			localurl=localurl+page;
			log.debug("localurl--->"+localurl);
			htmltext=regex.gethtmlContent(localurl,"UTF-8");
			//匹配需要的那部分网页
			String reghead = "<thead>(.*?)<\\/tr>";
			String[] headcontent = regex.htmlregex(htmltext,reghead,true);
			//头部
			List<String> headlist=new ArrayList<String>();
			for(int i =0;i<headcontent.length;i++){
				reghead = "<th.*?>(.*?)<\\/th>";
				String[] cl2contenthead =regex.htmlregex(headcontent[i],reghead,false);
				if(cl2contenthead!=null && cl2contenthead.length>0){
					for(String head:cl2contenthead){
						headlist.add(head);
					}
				}
			}
			String reg = "<tbody>(.*?)<\\/table>";
			String[] clcontent = regex.htmlregex(htmltext,reg,true);
			//具体内容部分的拆分
//			for(int i =0;i<clcontent.length;i++){
				reg = "<tr itemscope(.*?)<\\/tr>";
				String[] cl2content =regex.htmlregex(clcontent[0],reg,true);
				if(cl2content!=null&& cl2content.length>0){
					Semiconductor semiconductor=new Semiconductor();
					for(int j = 0;j<cl2content.length;j++){
						reg = "<td.*?>(.*?)<\\/td>";
						String[] class2 = regex.htmlregex(cl2content[j],reg,false);
						//特殊处理数据start
						reg="<td.*?>(.*?)<\\/td>";
						String[] class3 = regex.htmlregex(cl2content[j],reg,true);
						//规格
						String guige=this.getguige(class3[0],regex);
						//图片
						String imageurl=this.getImageUrl(class3[1],regex);
						String imagepath=imageurl.substring(imageurl.lastIndexOf("/")+1);
						log.debug("imageurl--->"+imageurl+"::"+"imagepath-->"+imagepath);
						//下载图片
//						ImageUtils.writeImage(imageurl);
						log.debug("aaa");
						//单位价格
						String priceurl=this.getPriceUrl(class3[7],regex);
						//获取商品的多价格
						String prices="";
						if(priceurl!=null && !priceurl.equals("")){
							prices=digikeypricesBuffer(baseurl+priceurl);
						}
						//end
						if(class2!=null&& class2.length>0){
							//转换为对象
							semiconductor.setGuige(guige);
							semiconductor.setImagepath(imagepath);
							semiconductor.setProducterkey(class2[2]);
							semiconductor.setCode(class2[3]);
							semiconductor.setProducter(class2[4]);
							semiconductor.setDesc(class2[5]);
							semiconductor.setDiscount(class2[6]);
//							semiconductor.setPrice(class2[7]);
							semiconductor.setPrice(prices);
							semiconductor.setLowestcount(class2[8]);
							if(headlist.size()>9){
								semiconductor.setFunction(getAllFunctions(headlist,class2));
							}
							classlist.add(semiconductor);
							semiconductor=new Semiconductor();
						}
					}
//				}
			}
		}
		return classlist;
	}
	
	/**
	 * 获取单价列表(digikey)
	 * @param url
	 * @return
	 */
	public List<String> digikeyprices(String url){
		SpiderRegex regex = new SpiderRegex();
		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//匹配需要的那部分网页
		String reg = "<table id=pricing(.*?)<\\/table>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		for(int i =0;i<clcontent.length;i++){
			reg = "<tr>(.*?)<\\/tr>";
			String[] cl2content =regex.htmlregex(clcontent[i],reg,true);
			if(cl2content!=null&& cl2content.length>0){
				for(int j = 0;j<cl2content.length;j++){
					reg = "<td.*?>(.*?)<\\/td>";
					String[] class2 = regex.htmlregex(cl2content[j],reg,false);
					if(class2!=null&& class2.length>0){
						csb.append(class2[0]).append(PRICESPLIT).append(class2[1]);
						classlist.add(csb.toString());
						csb = new StringBuffer();
					}
				}
			}
		}
		for(int i =0;i<classlist.size();i++){
			log.debug(classlist.get(i));
		}
		return classlist;
	}
	
	/**
	 * 获取单价列表(digikey)
	 * @param url
	 * @return
	 */
	public String digikeypricesBuffer(String url){
		SpiderRegex regex = new SpiderRegex();
		List<String> classlist = new ArrayList<String>();
		StringBuffer csb = new StringBuffer();
		StringBuffer price=new StringBuffer("");
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent(url,"UTF-8");
		//匹配需要的那部分网页
		String reg = "<table id=pricing(.*?)<\\/table>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		for(int i =0;i<clcontent.length;i++){
			reg = "<tr>(.*?)<\\/tr>";
			String[] cl2content =regex.htmlregex(clcontent[i],reg,true);
			if(cl2content!=null&& cl2content.length>0){
				for(int j = 0;j<cl2content.length;j++){
					reg = "<td.*?>(.*?)<\\/td>";
					String[] class2 = regex.htmlregex(cl2content[j],reg,false);
					if(class2!=null&& class2.length>0){
						csb.append(class2[0]).append(PRICESPLIT).append(class2[1]);
						classlist.add(csb.toString());
						price.append(csb.toString()+"$$");
						csb = new StringBuffer();
					}
				}
			}
		}
		if(price!=null && !price.equals("")){
			price.substring(0, price.length()-2);
		}
		return price.toString();
	}
	/**
	 * 组装功能描述(digikey)
	 * @param headlist
	 * @param class2
	 * @return
	 */
	private String getAllFunctions(List<String> headlist,String[] class2){
		String function="";
		for(int k=9;k<headlist.size();k++){
			function+=headlist.get(k)+":"+class2[k]+"$$";
		}
		function=function.substring(0, function.length()-2);
		log.debug("function---->"+function);
		return function;
	}
	 
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,String file){
		 log.debug(" 开始导出Excel文件 ");
		  File f = new File("F:\\sources\\qt1.xls");
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
	 * 获取规格(digikey)
	 * @param basehtml
	 * @return
	 */
	public String getguige(String basehtml,SpiderRegex regex){
		String reg = "<a href=(.*?) target=";
		String[] guiges=regex.htmlregex(basehtml,reg,false);
		String guige="";
		if(guiges!=null && guiges.length>0){
			guige=guiges[0].replaceAll("\"", "");
			log.debug("guige--->"+guige);
		}
		return guige;
	}
	/**
	 * 获得图片链接(digikey)
	 * @param basehtml
	 * @return
	 */
	public String getImageUrl(String basehtml,SpiderRegex regex){
		String reg = "<img border=0 height=64 src=(.*?) alt=";
		String[] imagepaths=regex.htmlregex(basehtml,reg,false);
		String imageurl="";
		if(imagepaths!=null && imagepaths.length>0){
			imageurl=imagepaths[0].replaceAll("\"", "");
		}
		return imageurl;
	}
	/**
	 * 获得价格链接(digikey)
	 * @param basehtml
	 * @return
	 */
	public String getPriceUrl(String basehtml,SpiderRegex regex){
		String reg="href=\"(.*?)\">";
		String[] priceurls=regex.htmlregex(basehtml,reg,false);
		String priceurl="";
		if(priceurls!=null && priceurls.length>0){
			priceurl=priceurls[0];
			log.debug("priceurl--->"+priceurl);
		}
		return priceurl;
	}
	public int getPageCount(String basehtml,SpiderRegex regex){
		String regpag="页面 1/(.*?) ";
		String[] pagecounts = regex.htmlregex(basehtml,regpag,false);
		int pagecount=1;
		if(pagecounts!=null && pagecounts.length>0){
			pagecount=Integer.parseInt(pagecounts[0]);
		}
		return pagecount;
	}
		
	public static void main(String[] args) throws Exception {
		Date start=new Date();
		StoreClassSpider scs = new StoreClassSpider();
//		List<String> classlist = scs.classesContent();
//		log.debug(classlist);
		//String content=scs.getBaiDuSoft();
//		log.debug(content);
//		scs.insertToDB(classlist);
		List<Semiconductor> semiconductorList=scs.digikeyContent1("http://www.digikey.cn/product-search/zh/optoelectronics/leds-lamp-replacements/524939/page/1");
		scs.createSemiconductorExcel(semiconductorList, "");
		log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
	}
}
