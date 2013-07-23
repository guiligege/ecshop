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
import org.guili.ecshop.util.ImageUtils;
import org.guili.ecshop.util.ResourceProperty;
import org.guili.ecshop.util.ResourceUtil;
import org.guili.ecshop.util.SpiderRegex;

/**
 * avnet.com spider
 * @author guili
 */
public class AvnetSpiderServiceImpl implements ISpiderService {
	private static final String PRICEINNERSPLIT=ResourceUtil.getValue(ResourceUtil.FILEPATH,"PRICESPLIT");
	private static Logger log=Logger.getLogger(AvnetSpiderServiceImpl.class);
	private static final String BASEURL=ResourceProperty.AVNET;
	private static String PRICESPLIT="$$";
	private ISemiconductorService semiconductorService=null;
	
	public void setSemiconductorService(ISemiconductorService semiconductorService) {
		this.semiconductorService = semiconductorService;
	}

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
				log.info("avent pagecount:"+pagecount);
				//循环分页
				for(int page=1;page<=pagecount;page++){
					int aventpage=(page-1)*25;
					String localurl=url.substring(0, url.length()-1);
					localurl=localurl+aventpage;
					log.debug("localurl--->"+localurl);
					log.info("avent run page:"+page);
					htmltext=regex.gethtmlContent(localurl,"UTF-8");
					//匹配需要的那部分网页
					String reghead = "<tr style=\"background:#FFFFFF;\">(.*?)<\\/tr>";
					String[] headcontent = regex.htmlregex(htmltext,reghead,true);
					//头部
					List<String> headlist=new ArrayList<String>();
					//防止分页没有
					if(headcontent==null || headcontent.length==0){
						continue;
					}
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
					if(headlist==null || headlist.size()==0){
						continue;
					}
					//分类
					String baseclass="";
					reghead = "<div class=\"breadcrumbs\">(.*?)<\\/div>";
					String[] baseclasss=regex.htmlregex(htmltext,reghead,false);
					if(baseclasss!=null && baseclasss.length>0){
						baseclass=baseclasss[0].replaceAll("\t", "").replaceAll(" ", "").replaceAll("X&nbsp;", "").replaceAll("&#187;", "\\$\\$");
					}
					String reg = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" class=\"dataTableParent resultsTbl\" id=\"resultsTbl1\" >(.*?)<\\/table>";
					String[] clcontent = regex.htmlregex(htmltext,reg,true);
					//具体内容部分的拆分
//							for(int i =0;i<clcontent.length;i++){
					//防止页面访问错误
					if(clcontent==null || clcontent.length==0){
						continue;
					}
					reg = "<tr style=\"background-color:#.*?>(.*?)<\\/tr>";
					String[] cl2content =regex.htmlregex(clcontent[0],reg,true);
					if(cl2content!=null&& cl2content.length>0){
						Semiconductor semiconductor=new Semiconductor();
						for(int j = 0;j<cl2content.length;j++){
							try {
								
								reg = "<td.*?>(.*?)<\\/td>";
								String[] class2 = regex.htmlregex(cl2content[j],reg,false);
//								//特殊处理数据start
								reg="<td.*?>(.*?)<\\/td>";
								String[] class3 = regex.htmlregex(cl2content[j],reg,true);
								//规格
								String guige=this.analysisGuige(class3[4],regex);
//								//图片
//								//获取商品的多价格
								String prices="";
								prices=analysisPricesToString(class3[6],regex);
								//获取最小的购买数
								String minicount="";
								minicount=analysisMiniCount(class3[8],regex);
								//现有数量
								String nowcount=this.getcount(class3[7], regex);
								//end
								if(class2!=null&& class2.length>0){
									for(int i=0;i<class2.length;i++){
										class2[i]=class2[i].replaceAll("\t", "");
									}
									//转换为对象
									semiconductor.setGuige(guige);
									semiconductor.setImagepath("");
									semiconductor.setImagename("");
									semiconductor.setProducterkey(class2[1]);
									semiconductor.setCode(class2[2]);
									semiconductor.setProducter(class2[3]);
									semiconductor.setDesc(class2[5]);
									semiconductor.setDiscount(nowcount);
									semiconductor.setPrice(prices);
									semiconductor.setLowestcount(minicount);
									if(headlist.size()>9){
										semiconductor.setFunction(buildDiscription(headlist,class2));
									}
									semiconductor.setBasesiteclass(baseclass);
									semiconductor.setSourcesite(ResourceProperty.AVNET);
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
		String reg = "最小起订量：&nbsp;(.*?)&nbsp;";
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
		  File f = new File("F:\\sources\\qtavent.xls");
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
		String regpag="/ <strong>(.*?)<\\/strong>";
		String[] pagecounts = regex.htmlregex(basehtml,regpag,false);
		int pagecount=1;
		try {
			if(pagecounts!=null && pagecounts.length>0){
				if(pagecounts[0].indexOf(",")>=0){
					pagecounts[0]=pagecounts[0].replaceAll(",", "");
				}
				pagecount=Integer.parseInt(pagecounts[0]);
			}
		} catch (Exception e) {
		}
		return pagecount;
	}
	private String getcount(String basehtml, SpiderRegex regex){
		String regpag="</script>(.*?)<br/>";
		String[] counts = regex.htmlregex(basehtml,regpag,false);
		regpag="</a>(.*?)<!-- Symphony Changes Ends - View Parts on Order -->";
		String[] counts1 = regex.htmlregex(basehtml,regpag,false);
		StringBuffer count=new StringBuffer("");
		if(counts!=null && counts.length>0){
			count.append(counts[0].trim().replaceAll("&nbsp;", " ")+"$$");
		}
		if(counts1!=null && counts1.length>0){
			count.append(counts1[0].replaceAll("\t", "").replaceAll("&nbsp;", " ")+"$$");
		}
		return count.toString();
	}
	
	//test
	public static void main(String[] args) throws Exception {
		Date start=new Date();
		AvnetSpiderServiceImpl scs = new AvnetSpiderServiceImpl();
//		List<Semiconductor> semiconductorList=scs.analysisContent("https://avnetexpress.avnet.com/store/em/EMController/Amplifiers/Amplifiers-Misc/_/N-100002?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-7&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
//		scs.createSemiconductorExcel(semiconductorList, "");
		scs.analysisService();
		log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
	}

	/**
	 * 分享网站地址抓取数据
	 */
	@Override
	public void analysisService() {
		//通过网址获取网页内容
		SpiderRegex regex = new SpiderRegex();
		List<String> urls=new ArrayList<String>();
		String htmltext = regex.gethtmlContent("https://avnetexpress.avnet.com/store/em/EMController/Communication/_/N-100030?action=products&cat=1&catalogId=500201&langId=-7&regionalStock=&storeId=500201","UTF-8");
		//匹配需要的那部分网页
		String regbig = "<table style=\"width:90%;\".*?>(.*?)<\\/table>";
		String[] bigcontent = regex.htmlregex(htmltext,regbig,true);
		int counturl=0;
		if(bigcontent!=null && bigcontent.length>0){
			for(int i=0;i<bigcontent.length;i++){
				regbig = "href=\"(.*?)\"";
				String[] smallContent=regex.htmlregex(bigcontent[i],regbig,true);
				log.debug(smallContent.length);
				if(smallContent!=null){
					if(smallContent.length>1){
						String smallurl=BASEURL+smallContent[1].substring(1);
						String innerhtmltext = regex.gethtmlContent(smallurl,"UTF-8");
						regbig = "<td style=\"padding:8px;\">(.*?)<\\/td>";
						String[] innersmallContent=regex.htmlregex(innerhtmltext,regbig,true);
						regbig = "href=\"(.*?)\"";
						//解析内部链接
						for(String innerurl:innersmallContent){
							String[] innerurls=regex.htmlregex(innerurl,regbig,false);
							if(innerurls!=null){
								counturl+=1;
								log.info("avnet run!");
								log.debug("listurl:--->"+BASEURL+innerurls[0].substring(0, innerurls[0].indexOf("?")).substring(1)+"?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-7&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
								List<Semiconductor> semiconductorList=analysisContent(BASEURL+innerurls[0].substring(0, innerurls[0].indexOf("?")).substring(1)+"?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-7&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
								//保存或更新到数据库
								if(semiconductorList!=null && semiconductorList.size()>0){
									semiconductorService.pageservice(semiconductorList);
								}
							}
						}
					}else{
						//解析顶部链接
						for(String smallurl:smallContent){
							log.info("avnet run!");
							log.debug("smallurl--->"+BASEURL+smallurl.substring(0, smallurl.indexOf("?")).substring(1)+"?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-7&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
							counturl+=1;
							List<Semiconductor> semiconductorList=analysisContent(BASEURL+smallurl.substring(0, smallurl.indexOf("?")).substring(1)+"?action=products&cat=1&catalogId=500201&categoryLink=true&cutTape=&inStock=&langId=-7&myCatalog=&npi=&proto=&regionalStock=&rohs=&storeId=500201&term=&topSellers=&No=0");
							//保存或更新到数据库
							if(semiconductorList!=null && semiconductorList.size()>0){
								semiconductorService.pageservice(semiconductorList);
							}
						}
					}
				}
			}
			log.debug("counturl-->"+counturl);
		}
	}
}
