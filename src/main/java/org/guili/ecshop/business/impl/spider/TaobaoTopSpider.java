package org.guili.ecshop.business.impl.spider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.spider.TopStore;
import org.guili.ecshop.util.SpiderRegex;

public class TaobaoTopSpider {
	
	private static Logger log=Logger.getLogger(TaobaoTopSpider.class);
	public  List<TopStore> classesContent() {
		SpiderRegex regex = new SpiderRegex();
		List<TopStore> topStoreList = new ArrayList<TopStore>();

		StringBuffer csb = new StringBuffer();
		//通过网址获取网页内容
		String htmltext = regex.gethtmlContent("http://www.taobao.com/go/act/taoke/ai_crown.php?pid=mm_10011550_0_0&unid=&source_id=&spm=0.0.0.0.FNIigK","gbk");
		//匹配需要的那部分网页
		String reg = "<div class=\"module-box\">(.*?)</dl>          </div>";
		String[] clcontent = regex.htmlregex(htmltext,reg,true);
		//具体内容部分的拆分
		for(int i =0;i<clcontent.length;i++){
			reg = "<span class=\"title\">(.*?)</span>";
			//主标题
			String[] class1 = regex.htmlregex(clcontent[i],reg,false);
			if(class1 == null || class1.length ==0){
				return null;
			}
			reg = "</em>(.*?)<span";
			String[] cl2content =regex.htmlregex(clcontent[i],reg,true);
			reg = "class=\"rank(.*?)</span>";
			String[] cl3content =regex.htmlregex(clcontent[i],reg,true);
			if(cl2content!=null && cl2content.length>0 && cl3content!=null && cl3content.length>0){
				for(int j = 0;j<cl2content.length;j++){
					TopStore topStore=new TopStore();
					if(class1!=null){
						topStore.setStoretag(class1[0]);
					}
					topStore.setStoreinfo(cl2content[j]);
					topStore.setXingyong(cl3content[j+1].split("\">")[1]);
					topStore.setCreatetime(new Date());
					topStoreList.add(topStore);
				}
			}
		}
		for(int i =0;i<topStoreList.size();i++){
			log.debug(topStoreList.get(i));
		}
		return topStoreList;
	}
	
	public static void main(String[] args) {
		TaobaoTopSpider taobaoTopSpider=new TaobaoTopSpider();
		taobaoTopSpider.classesContent();
	}
}
