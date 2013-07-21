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
 * Digikey.cn爬虫
 * @author guili
 */
public class DigikeySpiderServiceImpl implements ISpiderService {

	private static final String PRICEINNERSPLIT=ResourceUtil.getValue(ResourceUtil.FILEPATH,"PRICESPLIT");
	private static Logger log=Logger.getLogger(DigikeySpiderServiceImpl.class);
	private static final String BASEURL=ResourceProperty.DIGIKEY;
	private static final String BASEURLSITE=ResourceProperty.DIGIKEYSITE;
	private static String PRICESPLIT="$$";
	private ISemiconductorService semiconductorService=null;
	
	public void setSemiconductorService(ISemiconductorService semiconductorService) {
		this.semiconductorService = semiconductorService;
	}

	/**
	 * 分析网页内容
	 */
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
		log.info("digikey pagecount:"+pagecount);
		//循环分页
		for(int page=1;page<=pagecount;page++){
			String localurl=url.substring(0, url.length()-1);
			localurl=localurl+page;
			log.debug("localurl--->"+localurl);
			log.info("digikey run page:"+page);
			htmltext=regex.gethtmlContent(localurl,"UTF-8");
			//匹配需要的那部分网页
			String reghead = "<thead>(.*?)<\\/tr>";
			String[] headcontent = regex.htmlregex(htmltext,reghead,true);
			//头部
			List<String> headlist=new ArrayList<String>();
			//防止分页没有
			if(headcontent==null || headcontent.length==0){
				continue;
			}
			for(int i =0;i<headcontent.length;i++){
				reghead = "<th.*?>(.*?)<\\/th>";
				String[] cl2contenthead =regex.htmlregex(headcontent[i],reghead,false);
				if(cl2contenthead!=null && cl2contenthead.length>0){
					for(String head:cl2contenthead){
						headlist.add(head);
					}
				}
			}
			//分类
			String baseclass="";
			reghead = "<h1 class=seohtagbold itemprop=\"breadcrumb\">(.*?)<\\/h1>";
			String[] baseclasss=regex.htmlregex(htmltext,reghead,false);
			if(baseclasss!=null && baseclasss.length>0){
				baseclass=baseclasss[0].replaceAll("&nbsp;", "").replaceAll("&gt;", "\\$\\$");
			}
			//
			String reg = "<tbody>(.*?)<\\/table>";
			String[] clcontent = regex.htmlregex(htmltext,reg,true);
			//防止页面访问错误
			if(clcontent==null || clcontent.length==0){
				continue;
			}
			//具体内容部分的拆分
//					for(int i =0;i<clcontent.length;i++){
				reg = "<tr itemscope(.*?)<\\/tr>";
				String[] cl2content =regex.htmlregex(clcontent[0],reg,true);
				if(cl2content!=null&& cl2content.length>0){
					Semiconductor semiconductor=new Semiconductor();
					for(int j = 0;j<cl2content.length;j++){
						try {
							
							reg = "<td.*?>(.*?)<\\/td>";
							String[] class2 = regex.htmlregex(cl2content[j],reg,false);
							//特殊处理数据start
							reg="<td.*?>(.*?)<\\/td>";
							String[] class3 = regex.htmlregex(cl2content[j],reg,true);
							//规格
							String guige=this.analysisGuige(class3[0],regex);
							//图片
							String imageurl=this.analysisImageUrl(class3[1],regex);
							String imagename=imageurl.substring(imageurl.lastIndexOf("/")+1);
							log.debug("imageurl--->"+imageurl+"::"+"imagepath-->"+imagename);
							//下载图片
//							ImageUtils.writeImage(imageurl);
							log.debug("aaa");
							//单位价格
							String priceurl=this.analysisPriceUrl(class3[7],regex);
							//获取商品的多价格
							String prices="";
							if(priceurl!=null && !priceurl.equals("")){
								prices=analysisPricesToString(baseurl+priceurl);
							}
							//end
							if(class2!=null&& class2.length>0){
								//转换为对象
								semiconductor.setGuige(guige);
								semiconductor.setImagepath(imageurl);
								semiconductor.setImagename(imagename);
								semiconductor.setProducterkey(class2[2]);
								semiconductor.setCode(class2[3]);
								semiconductor.setProducter(class2[4]);
								semiconductor.setDesc(class2[5]);
								semiconductor.setDiscount(class2[6]);
	//									semiconductor.setPrice(class2[7]);
								semiconductor.setPrice(prices);
								semiconductor.setLowestcount(class2[8]);
								//新加创建时间
								semiconductor.setCreateTime(new Date());
								if(headlist.size()>9){
									semiconductor.setFunction(buildDiscription(headlist,class2));
								}
								semiconductor.setBasesiteclass(baseclass);
								semiconductor.setSourcesite(ResourceProperty.DIGIKEYSITE);
								classlist.add(semiconductor);
								semiconductor=new Semiconductor();
							}
						} catch (Exception e) {
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

	/**
	 * 分析价格并转为string
	 */
	@Override
	public String analysisPricesToString(String url) {
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
						csb.append(class2[0]).append(PRICEINNERSPLIT).append(class2[1]);
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
	 * 组装描述
	 */
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

	/**
	 * 建excel
	 */
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,
			String file) {
		log.debug(" 开始导出Excel文件 ");
		  File f = new File("F:\\sources\\qtdigikey.xls");
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
	 * 分析图片路径
	 */
	@Override
	public String analysisImageUrl(String basehtml, SpiderRegex regex) {
		String reg = "<img border=0 height=64 src=(.*?) alt=";
		String[] imagepaths=regex.htmlregex(basehtml,reg,false);
		String imageurl="";
		if(imagepaths!=null && imagepaths.length>0){
			imageurl=imagepaths[0].replaceAll("\"", "");
		}
		return imageurl;
	}

	/**
	 * 分析价格列表
	 */
	@Override
	public String analysisPriceUrl(String basehtml, SpiderRegex regex) {
		String reg="href=\"(.*?)\">";
		String[] priceurls=regex.htmlregex(basehtml,reg,false);
		String priceurl="";
		if(priceurls!=null && priceurls.length>0){
			priceurl=priceurls[0];
			log.debug("priceurl--->"+priceurl);
		}
		return priceurl;
	}

	/**
	 * 获得分页数
	 */
	@Override
	public int getPageCount(String basehtml, SpiderRegex regex) {
		String regpag="页面 1/(.*?) ";
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
		DigikeySpiderServiceImpl scs = new DigikeySpiderServiceImpl();
//		List<Semiconductor> semiconductorList=scs.analysisContent("http://www.digikey.cn/product-search/zh/optoelectronics/leds-lamp-replacements/524939/page/1");
		scs.analysisService();
//		scs.createSemiconductorExcel(semiconductorList, "");
		log.debug("总耗时:"+(new Date().getTime()-start.getTime())/1000);
	}

	//解析digikey的数据
	@Override
	public void analysisService() {
		//通过网址获取网页内容
		SpiderRegex regex = new SpiderRegex();
		List<String> urls=new ArrayList<String>();
		String htmltext = regex.gethtmlContent("http://www.digikey.cn/scripts/DkSearch/dksus.dll?WT.z_header=search_go&lang=zhs&keywords=&x=20&y=15","UTF-8");
		//匹配需要的那部分网页
		String regbig = "<ul class=catfiltersub>(.*?)<\\/ul>";
		String[] bigcontent = regex.htmlregex(htmltext,regbig,true);
		int counturl=0;
		if(bigcontent!=null && bigcontent.length>0){
			for(int i=0;i<bigcontent.length;i++){
				regbig = "<a href=\"\\/(.*?)\"";
				String[] smallContent=regex.htmlregex(bigcontent[i],regbig,true);
				log.debug(smallContent.length);
				for(String smallurl:smallContent){
					log.info("digikey run!");
					log.debug("smallurl--->"+BASEURLSITE+smallurl+"/page/1");
					counturl+=1;
					List<Semiconductor> semiconductorList=analysisContent(BASEURLSITE+smallurl+"/page/1");
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
