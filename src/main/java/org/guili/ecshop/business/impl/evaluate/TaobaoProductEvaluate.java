package org.guili.ecshop.business.impl.evaluate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.taobao.TaobaoTotalAllData;
import org.guili.ecshop.business.credit.IProductEvaluate;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.SpiderRegex;

/**
 * 淘宝产品评价
 * @ClassName:   TaobaoProductEvaluate 
 * @Description: 专注于处理淘宝产品的评价(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-13 下午12:04:03 
 *
 */
public class TaobaoProductEvaluate implements IProductEvaluate {

	private static Logger logger=Logger.getLogger(TaobaoProductEvaluate.class);
	
	//商家总体评价的权重
	private static double TOTAL_WWEIGHTS=0.25;
	private static double SINGEL_WWEIGHTS=0.75;
	//分数分布
	private static double TOTAL_INFO_SCORE=5;
	private static double TOTAL_INFO_BAD_PROPORTION_SCORE=20;
	private static double SINGEL_INFO_BAD_PROPORTION_SCORE=35;
	private static double SINGEL_INFO_EVALUATE_Repeat_PROPORTION_SCORE=40;
	//中差评比重
	//500以上区间
	private static double SINGEL_BAD_PROPORTION_MAX_500=8.0d;		//有分最低值
	private static double SINGEL_BAD_PROPORTION_MIN=1.0d;   	//最高分线
	//100-500
	private static double SINGEL_BAD_PROPORTION_MAX_100=9.0d;		//有分最低值
	//-100
	private static double SINGEL_BAD_PROPORTION_MAX_20=10.0d;		//有分最低值
	
	private static double TOTAL_BAD_PROPORTION_MAX=8d;			//总评及格线
	private static double TOTAL_BAD_PROPORTION_MIDDEl=5.0d;		//总评及格线
	private static double TOTAL_BAD_PROPORTION_MIN=3d;			//最高分数线
	
	//以前描述的底线和顶线
	private static double TOTAL_INFO_DESC_SCORE_MIN=4.4;
	private static double TOTAL_INFO_DESC_SCORE_MAX=4.8;
	//限制多少才进行统计
	private static int SINGEL_TOTAL_LIMIT=20;
	
	/**
	 * 卖家总体印象
	 */
	@Override
	public double sellerTotalEvaluate(Object obj) {
		if(obj==null){
			return 0;
		}
		TaobaoTotalAllData taobaoTotalAllData=(TaobaoTotalAllData)obj;
		//分析淘宝商品总体信誉
		//看卖家的以前整体信息
		double prevDescScore=this.calculateTotalDescScore(taobaoTotalAllData);
//		//卖家整体的差评比重分数
		double weightScore=this.calculateWeightScore(taobaoTotalAllData);
		logger.debug("prevDescScore-->"+prevDescScore);
		logger.debug("weightScore-->"+weightScore);
		return CommonTools.doubleFormat(prevDescScore+weightScore);
	}
	
	/**
	 * 计算商品总体貌似分值
	 * @param taobaoTotalAllData
	 * @return
	 */
	private double calculateTotalDescScore(TaobaoTotalAllData taobaoTotalAllData){
		if(taobaoTotalAllData==null){
			return 0;
		}
		double correspond=Double.parseDouble(taobaoTotalAllData.getData().getCorrespond());
		double prevDescScore=0;
		if(correspond<TaobaoProductEvaluate.TOTAL_INFO_DESC_SCORE_MIN){
			prevDescScore=0;
		}else if(correspond>=TaobaoProductEvaluate.TOTAL_INFO_DESC_SCORE_MAX){
			prevDescScore=5;
		}else{
			prevDescScore=((correspond-TaobaoProductEvaluate.TOTAL_INFO_DESC_SCORE_MIN)
							/(TOTAL_INFO_DESC_SCORE_MAX-TOTAL_INFO_DESC_SCORE_MIN))*TOTAL_INFO_SCORE;
		}
		return prevDescScore;
	}
	
	/**
	 * 根据中差评权重给商家打分
	 * @param taobaoTotalAllData	淘宝商家总评
	 * @return	中差评权重分数
	 */
	private double calculateWeightScore(TaobaoTotalAllData taobaoTotalAllData){
		if(taobaoTotalAllData==null){
			return 0;
		}
		double weightScore=0;
		List<String> correspondList=taobaoTotalAllData.getData().getCorrespondList();
		if(correspondList==null || correspondList.size()!=5){
			return 0;
		}
		logger.debug("correspondList--->"+correspondList.toString());
		double badWeight=Double.parseDouble(correspondList.get(2))+Double.parseDouble(correspondList.get(3))+Double.parseDouble(correspondList.get(4));
		logger.debug("中差评权重："+badWeight);
		//中差评
		if(badWeight>=TaobaoProductEvaluate.TOTAL_BAD_PROPORTION_MAX){
			weightScore=0;
		}else if(badWeight<=TaobaoProductEvaluate.TOTAL_BAD_PROPORTION_MIN){
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE;
		}else{
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE*((TaobaoProductEvaluate.TOTAL_BAD_PROPORTION_MAX-badWeight)/5);
		}
		return weightScore;
	}

	@Override
	public double singleProductEvaluate(Object obj) {
		if(obj==null){
			return 0;
		}
		TaobaoTotalAllData taobaoTotalAllData=(TaobaoTotalAllData)obj;
		int total=taobaoTotalAllData.getData().getCount().getTotal();
		if(total<=TaobaoProductEvaluate.SINGEL_TOTAL_LIMIT){
			return 0;
		}
		double singleWeightScore=evaluateSingleWeight(taobaoTotalAllData);
		return 0;
	}

	/**
	 * 计算单个商品中差评比重
	 * @param taobaoTotalAllData	商品总评信息
	 * @return
	 */
	public double evaluateSingleWeight(TaobaoTotalAllData taobaoTotalAllData){
		double singleWeightScore=0;
		int total=taobaoTotalAllData.getData().getCount().getTotal();
		int badAndNormal=taobaoTotalAllData.getData().getCount().getBad()
				+taobaoTotalAllData.getData().getCount().getNormal();
		double singleWeight=(badAndNormal/total)*100;
		//不同评价总量，不同的计算
		if(total>=500){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_500, singleWeight);
			
		}else if(total>=100 && total<500){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_100, singleWeight);
		}else if(total<100 && total >SINGEL_TOTAL_LIMIT){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_20, singleWeight);
		}
		return singleWeightScore;
	}
	
	/**
	 * 计算当前权重的分值
	 * @param min	最小可用区间
	 * @param max	最大可用区间
	 * @param weight	当前值
	 * @return
	 */
	public double getSingleStageScore(double min,double max,double weight){
		double singleWeightScore=0;
		if(weight<min){
			singleWeightScore=SINGEL_INFO_BAD_PROPORTION_SCORE;
		}else if(weight>max){
			singleWeightScore=0;
		}else{
			singleWeightScore=((max-weight)
							  /(max-min))
							  * SINGEL_INFO_BAD_PROPORTION_SCORE;
		}
		return singleWeightScore;
	}
	
	@Override
	public  double evaluateCalculate(String url){
		
		//分析url对应的商家用户id和商品id
		Map<String, String> parammap=this.analyzeUrl(url);
		if(parammap==null || parammap.size()==0){
			return 0;
		}
		String userid=parammap.get("userid")==null?"":parammap.get("userid");
		String productid=parammap.get("productid")==null?"":parammap.get("productid");
		//获取淘宝总体评论对象。
		TaobaoTotalAllData taobaoTotalAllData=this.analyzeTaobaoTotalAllData(userid, productid);
		
		double prevScore=this.sellerTotalEvaluate(taobaoTotalAllData);
		double productScore=this.singleProductEvaluate(taobaoTotalAllData);
		
		/*//解析url请求
		SpiderRegex regex = new SpiderRegex();
		parammap.get("");
		String htmltext = regex.gethtmlContent(EvaluateConfig.taobao_evaluate_total_url+"?userNumId=1819877675&auctionNumId=27584368469","gbk");
		
		//List<TaobaoTotalAllData> taobaoTotalAllDataList=json2TaobaoTotalAllDataList(htmltext);
		TaobaoTotalAllData taobaoTotalAllData=json2TaobaoTotalAllData(htmltext);
		
		taobaoTotalAllData.getData().getCorrespond();
		logger.debug(htmltext);*/
		return CommonTools.doubleFormat(prevScore+productScore);
	}
	
	/**
	 * 分析活动淘宝商品总评对象
	 * @param userNumid		淘宝卖家id
	 * @param auctionNumId	淘宝卖家商品id
	 * @return
	 */
	private  TaobaoTotalAllData analyzeTaobaoTotalAllData(String userNumid,String auctionNumId){
		if(userNumid.equals("") || auctionNumId.equals("")){
			return null;
		}
		TaobaoTotalAllData taobaoTotalAllData=new TaobaoTotalAllData();
		//以前的请求方式
		SpiderRegex regex = new SpiderRegex();
		String htmltext = regex.gethtmlContent(EvaluateConfig.taobao_evaluate_total_url+"?userNumId="+userNumid+"&auctionNumId="+auctionNumId,"gbk");
		//现在的请求方式
		//String htmltext=CommonTools.requestUrl(EvaluateConfig.taobao_evaluate_total_url, "gbk", "userNumId=1819877675&auctionNumId=27584368469");
		//List<TaobaoTotalAllData> taobaoTotalAllDataList=json2TaobaoTotalAllDataList(htmltext);
		try {
			taobaoTotalAllData=json2TaobaoTotalAllData(htmltext);
		} catch (Exception e) {
			logger.error("分析淘宝总评对象出错！error is "+e.getMessage());
		}
		return taobaoTotalAllData;
	}
	
	/**
	 * 分析yurl
	 */
	@Override
	public Map<String, String> analyzeUrl(String producturl) {
		Map<String, String> parammap=new HashMap<String, String>();
		parammap=this.taobaoAnalyze(producturl);
		return parammap;
	}
	
	/**
	 * taobao私有解析淘宝url函数
	 * @param url
	 * @return
	 */
	private Map<String, String>  taobaoAnalyze(String url){
		Map<String, String> parammap=new HashMap<String, String>();
		if(url==null || !(url.startsWith("http://item.taobao.com/item.htm") || url.startsWith("item.taobao.com/item.htm"))){
			return null;
		}
		//取得商品id//http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757
		String productid=url.substring(url.indexOf("id=")+3);
		if(productid!=null){
			parammap.put("productid", productid);
		}
		//解析url内容
		SpiderRegex regex = new SpiderRegex();
		String htmltext = regex.gethtmlContent(url,"gbk");
		String userRegex = "userid=(.*?);siteCategory";
		String shopRegex = "shopId=(.*?); userid=";
		String siteCategoryRegex = "siteCategory=(.*?);siteInstanceId=";
		//shopId=106471556; userid=1819877675;siteCategory=2
		String[] userid = regex.htmlregex(htmltext,userRegex,true);
		String[] shopid = regex.htmlregex(htmltext,shopRegex,true);
		String[] siteCategoryid = regex.htmlregex(htmltext,siteCategoryRegex,true);
		if(userid!=null && userid.length>0){
			parammap.put("userid", userid[0]);
		}
		if(shopid!=null && shopid.length>0){
			parammap.put("shopid", shopid[0]);
		}
		if(siteCategoryid!=null && siteCategoryid.length>0){
			parammap.put("siteCategoryid", siteCategoryid[0]);
		}
		logger.info("userid is :---"+userid[0]+"--");
		logger.info("shopid is :---"+shopid[0]+"--");
		logger.info("siteCategoryid is :---"+siteCategoryid[0]+"--");
		return parammap;
	}
	
	/**
	 * 从json数据转换为淘宝数据对象
	 * @param jsonStr
	 * @return
	 */
	public TaobaoTotalAllData json2TaobaoTotalAllData(String jsonStr) {
		//ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		 JSONObject jsonObject = null;  
		 //setDataFormat2JAVA();   
        // jsonObject = JSONObject.fromObject(jsonStr.substring(1).substring(0, jsonStr.length()-2));
         jsonObject = JSONObject.fromObject(jsonStr.substring(1).substring(0, jsonStr.length()-2));
		//TaobaoTotalAllData taobaoTotalAllData = gson.fromJson(jsonStr, TaobaoTotalAllData.class);
         TaobaoTotalAllData taobaoTotalAllData = (TaobaoTotalAllData)JSONObject.toBean(jsonObject, TaobaoTotalAllData.class);
		return taobaoTotalAllData;
	}
	//test
	public static void main(String[] args) {
		TaobaoProductEvaluate taobaoProductEvaluate=new TaobaoProductEvaluate();
		//taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757");
		taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757");
//		Map<String, String> parammap=taobaoProductEvaluate.analyzeUrl("http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757");
//		taobaoProductEvaluate.sellerTotalEvaluate(parammap);
	}
}
