package org.guili.ecshop.business.impl.evaluate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.taobao.TaobaoImpress;
import org.guili.ecshop.bean.credit.taobao.TaobaoTotalAllData;
import org.guili.ecshop.business.credit.IProductEvaluate;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.SpiderRegex;
import org.springframework.ui.ModelMap;

/**
 * 天猫产品评价
 * @ClassName:   TmallProductEvaluate 
 * @Description: 专注于处理天猫(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-13 下午12:05:31 
 *
 */
public class TmallProductEvaluate implements IProductEvaluate {

	private static Logger logger=Logger.getLogger(TmallProductEvaluate.class);
	//限制多少才进行统计
	private static int SINGEL_TOTAL_LIMIT=20;
	//商家总体评价的权重
		private static double TOTAL_WWEIGHTS=0.25;
		private static double SINGEL_WWEIGHTS=0.75;
		//分数分布
		private static double TOTAL_INFO_SCORE=5;
		private static double TOTAL_INFO_BAD_PROPORTION_SCORE=20;
		private static double SINGEL_INFO_BAD_PROPORTION_SCORE=35;
		private static double SINGEL_INFO_EVALUATE_Repeat_PROPORTION_SCORE=40;
		//中差评比重
		//800以上区间
		private static double SINGEL_BAD_PROPORTION_MAX_500=11.0d;		//有分最低值
		private static double SINGEL_BAD_PROPORTION_MIDDEl=5.0d;		//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MIN=1.0d;   		//最高分线
		//100-800
		private static double SINGEL_BAD_PROPORTION_MIDDEl_100=5.0d;	//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MAX_100=10.0d;		//有分最低值
		//-100
		private static double SINGEL_BAD_PROPORTION_MIDDEl_20=5.0d;		//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MAX_20=9.0d;		//有分最低值
		
		private static double TOTAL_BAD_PROPORTION_MAX=7d;			//总评及格线
		private static double TOTAL_BAD_PROPORTION_MIDDEl=4.0d;			//总评及格线
		private static double TOTAL_BAD_PROPORTION_MIN=3d;			//最高分数线
		
		//以前描述的底线和顶线
		private static double TOTAL_INFO_DESC_SCORE_MIN=4.5;
		private static double TOTAL_INFO_DESC_SCORE_MAX=4.8;
	@Override
	public double evaluateCalculate(String url, ModelMap modelMap) {
		//分析url对应的商家用户id和商品id
		Map<String, String> parammap=this.analyzeUrl(url);
		String userid=parammap.get("userid")==null?"":parammap.get("userid");
		String productid=parammap.get("productid")==null?"":parammap.get("productid");
		if(parammap==null || parammap.size()==0){
			return 0;
		}
		//获取淘宝总体评论对象。
		TaobaoTotalAllData taobaoTotalAllData=this.analyzeTaobaoTotalAllData(userid, productid);
		//获得当前商品的评论
		double prevScore=this.sellerTotalEvaluate(taobaoTotalAllData);
		double productScore=this.singleProductEvaluate(taobaoTotalAllData);
		//获得淘宝单个商品的评价信息
		//一期做前100页，即800条评论的重复率,记录下有用评论
		/*Map<String, Map<String, Object>> productEvaluate=analyzeProductUrlAll(userid, productid,taobaoTotalAllData.getData().getCount().getTotal());
		//计算评论的重复的评分
		double repeatScore=evaluateSingleRepeat(productEvaluate,taobaoTotalAllData);
		//获得总的分数评价
		if(taobaoTotalAllData.getData().getCount().getTotal()<=SINGEL_TOTAL_LIMIT){
			modelMap.put("isless", true);
		}else{
			modelMap.put("isless", false);
		}
		
		modelMap.put("productScore", productScore);
		modelMap.put("repeatScore", repeatScore);
		
		logger.info("产品总评评分："+productScore);
		logger.info("评论重复率评分："+repeatScore);
		double result=CommonTools.doubleFormat(prevScore+productScore+repeatScore);
		modelMap.put("result", result);
		logger.info("总分："+result);*/
		modelMap.put("prevScore", prevScore);
		modelMap.put("productScore", productScore);
		logger.info("卖家总评评分："+prevScore);
		logger.info("产品总评评分："+productScore);
		double result=0;
		return result;
	}
	/**
	 * 分析yurl
	 */
	@Override
	public Map<String, String> analyzeUrl(String producturl) {
		Map<String, String> parammap=new HashMap<String, String>();
		parammap=this.tmallAnalyze(producturl);
		return parammap;
	}
	
	/**
	 * taobao私有解析淘宝url函数
	 * @param url
	 * @return
	 */
	private Map<String, String>  tmallAnalyze(String url){
		Map<String, String> parammap=new HashMap<String, String>();
		if(url==null || !(url.startsWith(EvaluateConstConfig.TMALLHEAD) || url.startsWith(EvaluateConstConfig.TMALLHEAD.replaceAll("http://", "")))){
			return null;
		}
		//取得商品id//http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757
		//获取商品id
		String productid=url.substring(url.indexOf("id=")+3,url.indexOf("id=")+3+url.substring(url.indexOf("id=")+3).indexOf("&"));
		if(productid!=null){
			parammap.put("productid", productid);
		}
		//判断url中是否存在商家id信息
		if(url.indexOf("user_id=")!=-1){
			//截取url中商家id
			if(!url.substring(url.indexOf("user_id=")+"user_id=".length(),url.indexOf("user_id=")+"user_id=".length()+url.substring(url.indexOf("user_id=")+"user_id=".length()).indexOf("&")).equals("")){
				parammap.put("userid", url.substring(url.indexOf("user_id=")+"user_id=".length(),url.indexOf("user_id=")+"user_id=".length()+url.substring(url.indexOf("user_id=")+"user_id=".length()).indexOf("&")));
				logger.debug("search product userid is :---"+parammap.get("userid")+"--");
				logger.debug("search product is :---"+parammap.get("productid")+"--");
				return parammap;
			}
		}
		//解析url内容
		SpiderRegex regex = new SpiderRegex();
		String htmltext = regex.gethtmlContent(url,"gbk");
		//正则解析商家id和商家shopid
		String userRegex = "; userid=(.*?);";
		String shopRegex = "; shopId=(.*?);";
		String[] userid = regex.htmlregex(htmltext,userRegex,true);
		String[] shopid = regex.htmlregex(htmltext,shopRegex,true);
		if(userid!=null && userid.length>0){
			parammap.put("userid", userid[0]);
		}
		if(shopid!=null && shopid.length>0){
			parammap.put("shopid", shopid[0]);
		}
		logger.info("userid is :---"+userid[0]+"--");
		logger.info("shopid is :---"+shopid[0]+"--");
		return parammap;
	}
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
		if(badWeight>=TmallProductEvaluate.TOTAL_BAD_PROPORTION_MAX){
			weightScore=0;
		}else if(badWeight<=TmallProductEvaluate.TOTAL_BAD_PROPORTION_MIN){
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE;
		}else{
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE*((TmallProductEvaluate.TOTAL_BAD_PROPORTION_MAX-badWeight)/5);
		}
		return weightScore;
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
		if(correspond<TmallProductEvaluate.TOTAL_INFO_DESC_SCORE_MIN){
			prevDescScore=0;
		}else if(correspond>=TmallProductEvaluate.TOTAL_INFO_DESC_SCORE_MAX){
			prevDescScore=5;
		}else{
			prevDescScore=((correspond-TmallProductEvaluate.TOTAL_INFO_DESC_SCORE_MIN)
							/(TOTAL_INFO_DESC_SCORE_MAX-TOTAL_INFO_DESC_SCORE_MIN))*TOTAL_INFO_SCORE;
		}
		return prevDescScore;
	}
	
	
	@Override
	public double singleProductEvaluate(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	private  TaobaoTotalAllData analyzeTaobaoTotalAllData(String userNumid,String auctionNumId){
		if(userNumid.equals("") || auctionNumId.equals("")){
			return null;
		}
		TaobaoTotalAllData taobaoTotalAllData=new TaobaoTotalAllData();
		//现在的请求方式
		String htmltext=CommonTools.requestUrl(EvaluateConfig.taobao_evaluate_total_url, "gbk", "userNumId="+userNumid+"&auctionNumId="+auctionNumId);
		try {
			taobaoTotalAllData=json2TaobaoTotalAllData(htmltext);
		} catch (Exception e) {
			logger.error("分析淘宝总评对象出错！error is "+e.getMessage());
		}
		return taobaoTotalAllData;
	}
	
	/**
	 * 从json数据转换为淘宝数据对象
	 * @param jsonStr
	 * @return
	 */
	private TaobaoTotalAllData json2TaobaoTotalAllData(String jsonStr) {
		 JSONObject jsonObject = null;  
		//新版请求淘宝
		 jsonObject = JSONObject.fromObject(jsonStr.substring(5).substring(0, jsonStr.length()-6));
         Map<String, Class> mycollection = new HashMap<String, Class>();
         mycollection.put("correspondList", String.class);
         mycollection.put("impress", TaobaoImpress.class);
         mycollection.put("spuRatting", String.class);
         TaobaoTotalAllData taobaoTotalAllData = (TaobaoTotalAllData)JSONObject.toBean(jsonObject, TaobaoTotalAllData.class,mycollection);
		return taobaoTotalAllData;
	}

	public static void main(String[] args) {
		TmallProductEvaluate tmallProductEvaluate=new TmallProductEvaluate();
		tmallProductEvaluate.evaluateCalculate("http://detail.tmall.com/item.htm?spm=a1z10.5.w4011-3239604436.99.m5vkUY&id=35283331630&rn=3a22ecacc7699a79462d5ef044839ccc",new ModelMap() );
	}

}
