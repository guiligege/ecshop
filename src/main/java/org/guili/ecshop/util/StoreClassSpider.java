package org.guili.ecshop.util;

import java.util.ArrayList;
import java.util.List;

public class StoreClassSpider {
	
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
			System.out.println(classlist.get(i));
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
		System.out.println(htmltext);
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
			System.out.println(classlist.get(i));
		}
		return classlist;
	}
	
	public static void main(String[] args) throws Exception {
		StoreClassSpider scs = new StoreClassSpider();
//		List<String> classlist = scs.classesContent();
//		System.out.println(classlist);
		//String content=scs.getBaiDuSoft();
//		System.out.println(content);
//		scs.insertToDB(classlist);
		List<String> classlist=scs.digikeyContent();
	}
}
