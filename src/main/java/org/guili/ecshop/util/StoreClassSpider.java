package org.guili.ecshop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.guili.ecshop.bean.Semiconductor;

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
	
	//抓取网站所有类别www.digikey.cn
	public List<Semiconductor> digikeyContent1() {
		SpiderRegex regex = new SpiderRegex();
		List<Semiconductor> classlist = new ArrayList<Semiconductor>();
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
				Semiconductor semiconductor=new Semiconductor();
				for(int j = 0;j<cl2content.length;j++){
					reg = "<td.*?>(.*?)<\\/td>";
					String[] class2 = regex.htmlregex(cl2content[j],reg,false);
					
					if(class2!=null&& class2.length>0){
						for(int k = 0;k<class2.length;k++){
							csb.append(class2[k]).append("--");
//								classlist.add(csb.toString());
//								csb = new StringBuffer();
						}
						//转换为对象
						semiconductor.setGuige(class2[0]);
						semiconductor.setImagepath(class2[1]);
						semiconductor.setProducterkey(class2[2]);
						semiconductor.setCode(class2[3]);
						semiconductor.setProducter(class2[4]);
						semiconductor.setDesc(class2[5]);
						semiconductor.setDiscount(class2[6]);
						semiconductor.setPrice(class2[7]);
						semiconductor.setLowestcount(class2[8]);
						semiconductor.setBaozhuang(class2[9]);
						semiconductor.setXilie(class2[10]);
						semiconductor.setRate(class2[11]);
						semiconductor.setDianya(class2[12]);
						semiconductor.setDianliu(class2[13]);
						semiconductor.setLianlu(class2[14]);
						semiconductor.setDirection(class2[15]);
						semiconductor.setWendu(class2[16]);
						semiconductor.setSize(class2[17]);
						semiconductor.setStandard(class2[18]);
						semiconductor.setShutdown(class2[19]);
						classlist.add(semiconductor);
						semiconductor=new Semiconductor();
					}
				}
			}
		}
		for(int i =0;i<classlist.size();i++){
			System.out.println(classlist.get(i));
		}
		return classlist;
	}
	
	public void createSemiconductorExcel(List<Semiconductor> semiconductorList,String file){
		 System.out.println(" 开始导出Excel文件 ");
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
		  e.setCell(9, "包装");
		  e.setCell(10, "系列");
		  e.setCell(11, "数据速率");
		  e.setCell(12, "电源电压");
		  e.setCell(13, "25&deg; C 时的待机电流（典型值）");
		  e.setCell(14, "链路范围，低功耗");
		  e.setCell(15, "方向");
		  e.setCell(16, "工作温度");
		  e.setCell(17, "尺寸");
		  e.setCell(18, "标准");
		  e.setCell(19, "关断");
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
				  e.setCell(9, semiconductor.getBaozhuang());
				  e.setCell(10, semiconductor.getXilie());
				  e.setCell(11, semiconductor.getRate());
				  e.setCell(12, semiconductor.getDianya());
				  e.setCell(13, semiconductor.getDianliu());
				  e.setCell(14, semiconductor.getLianlu());
				  e.setCell(15, semiconductor.getDirection());
				  e.setCell(16, semiconductor.getWendu());
				  e.setCell(17, semiconductor.getSize());
				  e.setCell(18, semiconductor.getStandard());
				  e.setCell(19, semiconductor.getShutdown());
			  }
		  }
		  try {
		   e.export();
		   System.out.println(" 导出Excel文件[成功] ");
		  } catch (IOException ex) {
		   System.out.println(" 导出Excel文件[失败] ");
		   ex.printStackTrace();
		  }
	}
		
	public static void main(String[] args) throws Exception {
		StoreClassSpider scs = new StoreClassSpider();
//		List<String> classlist = scs.classesContent();
//		System.out.println(classlist);
		//String content=scs.getBaiDuSoft();
//		System.out.println(content);
//		scs.insertToDB(classlist);
		List<Semiconductor> semiconductorList=scs.digikeyContent1();
		scs.createSemiconductorExcel(semiconductorList, "");
	}
}
