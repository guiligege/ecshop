package org.guili.ecshop.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.business.ISpiderService;
import org.guili.ecshop.util.SpiderRegex;

/**
 * mouser spider
 * @author guili
 */
public class MouserSpiderServiceImpl implements ISpiderService {

	private Logger logger=Logger.getLogger(MouserSpiderServiceImpl.class);
	@Override
	public List<Semiconductor> analysisContent(String url) {
		//网站地址
				String baseurl="http://www.digikey.cn";
				SpiderRegex regex = new SpiderRegex();
				List<Semiconductor> classlist = new ArrayList<Semiconductor>();
				//通过网址获取网页内容
				String htmltext = regex.gethtmlContent(url,"UTF-8");
				//分析当前页数
				int pagecount=getPageCount(htmltext,regex);
				logger.debug("pagecount-->"+pagecount);
				
				//循环分页
				for(int page=1;page<=pagecount;page++){
					String localurl=url.substring(0, url.length()-1);
					localurl=localurl+page;
					logger.debug("localurl--->"+localurl);
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
//					for(int i =0;i<clcontent.length;i++){
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
//								String guige=this.getguige(class3[0],regex);
								//图片
//								String imageurl=this.getImageUrl(class3[1],regex);
//								String imagepath=imageurl.substring(imageurl.lastIndexOf("/")+1);
//								log.debug("imageurl--->"+imageurl+"::"+"imagepath-->"+imagepath);
								//下载图片
//								ImageUtils.writeImage(imageurl);
								//单位价格
//								String priceurl=this.getPriceUrl(class3[7],regex);
								//获取商品的多价格
//								String prices="";
//								if(priceurl!=null && !priceurl.equals("")){
//									prices=digikeypricesBuffer(baseurl+priceurl);
//								}
								//end
								if(class2!=null&& class2.length>0){
									//转换为对象
									semiconductor.setGuige(class2[0]);
									semiconductor.setImagepath(class2[1]);
									semiconductor.setProducterkey(class2[2]);
									semiconductor.setCode(class2[3]);
									semiconductor.setProducter(class2[4]);
									semiconductor.setDesc(class2[5]);
									semiconductor.setDiscount(class2[6]);
									semiconductor.setPrice(class2[7]);
//									semiconductor.setPrice(prices);
									semiconductor.setLowestcount(class2[8]);
									if(headlist.size()>9){
										semiconductor.setFunction(buildDiscription(headlist,class2));
									}
									classlist.add(semiconductor);
									semiconductor=new Semiconductor();
								}
							}
//						}
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

	@Override
	public String buildDiscription(List<String> headlist, String[] class2) {
		String function="";
		for(int k=9;k<headlist.size();k++){
			function+=headlist.get(k)+":"+class2[k]+"$$";
		}
		function=function.substring(0, function.length()-2);
		logger.debug("function---->"+function);
		return function;
	}

	@Override
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,
			String file) {
		// TODO Auto-generated method stub

	}

	@Override
	public String analysisGuige(String basehtml, SpiderRegex regex) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return 0;
	}

}
