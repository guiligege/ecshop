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
import org.guili.ecshop.business.ISpiderService;
import org.guili.ecshop.util.ExcelWriter;
import org.guili.ecshop.util.ImageUtils;
import org.guili.ecshop.util.ResourceUtil;
import org.guili.ecshop.util.SpiderRegex;

/**
 * avnet.com spider
 * @author guili
 */
public class AvnetSpiderServiceImpl implements ISpiderService {
	private static final String PRICEINNERSPLIT=ResourceUtil.getValue(ResourceUtil.FILEPATH,"PRICESPLIT");
	private static Logger log=Logger.getLogger(AvnetSpiderServiceImpl.class);
	private static final String BASEURL=ResourceUtil.getValue(ResourceUtil.FILEPATH,"Avnet");
	private static String PRICESPLIT="$$";
	
	@Override
	public List<Semiconductor> analysisContent(String url) {
		//网站地址
				String baseurl=BASEURL;
				SpiderRegex regex = new SpiderRegex();
				List<Semiconductor> classlist = new ArrayList<Semiconductor>();
				//通过网址获取网页内容
				String htmltext = regex.gethtmlContent(url,"UTF-8");
				//分析当前页数
				int pagecount=getPageCount(htmltext,regex);
				log.debug("pagecount-->"+pagecount);
				//循环分页
				for(int page=1;page<=pagecount;page++){
					int aventpage=(page-1)*25;
					String localurl=url.substring(0, url.length()-1);
					localurl=localurl+aventpage;
					log.debug("localurl--->"+localurl);
					htmltext=regex.gethtmlContent(localurl,"UTF-8");
					//匹配需要的那部分网页
					String reghead = "<tr style=\"background:#FFFFFF;\">(.*?)<\\/tr>";
					String[] headcontent = regex.htmlregex(htmltext,reghead,true);
					//头部
					List<String> headlist=new ArrayList<String>();
					for(int i =0;i<headcontent.length;i++){
						reghead = "<td.*?>(.*?)<\\/td>";
						String[] cl2contenthead =regex.htmlregex(headcontent[i],reghead,false);
						if(cl2contenthead!=null && cl2contenthead.length>0){
							for(String head:cl2contenthead){
								head=head.replaceAll("&nbsp;", " ");
								headlist.add(head);
							}
						}
					}
					String reg = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" class=\"dataTableParent resultsTbl\" id=\"resultsTbl1\" >(.*?)<\\/table>";
					String[] clcontent = regex.htmlregex(htmltext,reg,true);
					//具体内容部分的拆分
//							for(int i =0;i<clcontent.length;i++){
						reg = "<tr style=\"background-color:#.*?>(.*?)<\\/tr>";
						String[] cl2content =regex.htmlregex(clcontent[0],reg,true);
						if(cl2content!=null&& cl2content.length>0){
							Semiconductor semiconductor=new Semiconductor();
							for(int j = 0;j<cl2content.length;j++){
								try {
									
									reg = "<td.*?>(.*?)<\\/td>";
									String[] class2 = regex.htmlregex(cl2content[j],reg,false);
//									//特殊处理数据start
									reg="<td.*?>(.*?)<\\/td>";
									String[] class3 = regex.htmlregex(cl2content[j],reg,true);
									//规格
									String guige=this.analysisGuige(class3[4],regex);
//									//图片
//									String imageurl=this.analysisImageUrl(class3[1],regex);
//									String imagepath=imageurl.substring(imageurl.lastIndexOf("/")+1);
//									log.debug("imageurl--->"+imageurl+"::"+"imagepath-->"+imagepath);
//									//下载图片
//									ImageUtils.writeImage(imageurl);
//									log.debug("aaa");
//									//单位价格
//									String priceurl=this.analysisPriceUrl(class3[7],regex);
//									//获取商品的多价格
									String prices="";
									prices=analysisPricesToString(class3[6],regex);
									//获取最小的购买数
									String minicount="";
									minicount=analysisMiniCount(class3[8],regex);
									//end
									if(class2!=null&& class2.length>0){
										for(int i=0;i<class2.length;i++){
											class2[i]=class2[i].replaceAll("\t", "");
										}
										//转换为对象
										semiconductor.setGuige(guige);
										semiconductor.setImagepath("");
										semiconductor.setProducterkey(class2[1]);
										semiconductor.setCode(class2[2]);
										semiconductor.setProducter(class2[3]);
										semiconductor.setDesc(class2[5]);
										semiconductor.setDiscount(class2[7]);
			//									semiconductor.setPrice(class2[7]);
										semiconductor.setPrice(prices);
										semiconductor.setLowestcount(minicount);
										if(headlist.size()>9){
											semiconductor.setFunction(buildDiscription(headlist,class2));
										}
										classlist.add(semiconductor);
										semiconductor=new Semiconductor();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
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

	@Override
	public String analysisPricesToString(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String analysisPricesToString(String basehtml, SpiderRegex regex) {
		String reg = "<div class=\"small\" style=\"font-family:Arial,Helvetica,sans-serif;text-align:right;\">(.*?)<\\/div>";
		String[] guiges=regex.htmlregex(basehtml,reg,true);
		String guige="";
		if(guiges!=null && guiges.length>0){
			guige=guiges[0].replaceAll("<br>","\\$\\$");
		}
		if(guige.equals("<div style=\"text-align:center\">Requires<br/>Quote")){
			guige="";
		}
		log.debug("analysisPricesToString--->"+guige);
		return guige;
	}

	public String analysisMiniCount(String basehtml, SpiderRegex regex) {
		String reg = "Min:&nbsp;(.*?)&nbsp;";
		String[] guiges=regex.htmlregex(basehtml,reg,true);
		String count="";
		if(guiges!=null && guiges.length>0){
			count=guiges[0];
			log.debug("count--->"+count);
		}
		return count;
	}
	@Override
	public String buildDiscription(List<String> headlist, String[] class2) {
		String function="";
		for(int k=9;k<headlist.size();k++){
			function+=headlist.get(k)+":"+class2[k]+"$$";
		}
		function=function.substring(0, function.length()-2);
		log.debug("function---->"+function);
		return function;
	}

	@Override
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,
			String file) {
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

	@Override
	public String analysisGuige(String basehtml, SpiderRegex regex) {
		String reg = "<a href=(.*?) onclick=";
		String[] guiges=regex.htmlregex(basehtml,reg,false);
		String guige="";
		if(guiges!=null && guiges.length>0){
			guige=guiges[0].replaceAll("\"", "");
			log.debug("guige--->"+guige);
		}
		return guige;
	}

	@Override
	public String analysisImageUrl(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String analysisPriceUrl(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageCount(String basehtml, SpiderRegex regex) {
		String regpag="of <strong>(.*?)</strong>";
		String[] pagecounts = regex.htmlregex(basehtml,regpag,false);
		int pagecount=1;
		if(pagecounts!=null && pagecounts.length>0){
			pagecount=Integer.parseInt(pagecounts[0]);
		}
		return pagecount;
	}
	
	//test
	public static void main(String[] args) throws Exception {
		Date start=new Date();
		AvnetSpiderServiceImpl scs = new AvnetSpiderServiceImpl();
		List<Semiconductor> semiconductorList=scs.analysisContent("https://avnetexpress.avnet.com/store/em/EMController/Communication/Analog-Front-End/_/N-100031?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-1&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
		scs.createSemiconductorExcel(semiconductorList, "");
		log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
	}
}
