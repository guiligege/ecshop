package org.guili.ecshop.business.impl.evaluate;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.taobao.EvaluateTime;
import org.guili.ecshop.bean.credit.taobao.TaobaoEvaluate;
import org.guili.ecshop.bean.credit.taobao.TaobaoImpress;
import org.guili.ecshop.bean.credit.taobao.TaobaoSingleData;
import org.guili.ecshop.bean.credit.taobao.TaobaoTotalAllData;
import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;
import org.guili.ecshop.business.credit.IProductEvaluateService;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.SpiderRegex;
import org.springframework.ui.ModelMap;

/**
 * 淘宝产品评价
 * @ClassName:   TaobaoProductEvaluate 
 * @Description: 专注于处理淘宝产品的评价(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-13 下午12:04:03 
 *
 */
public class TaobaoProductEvaluateService implements IProductEvaluateService {

	private static Logger logger=Logger.getLogger(TaobaoProductEvaluateService.class);
	
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
	private static double SINGEL_BAD_PROPORTION_MIN=1.0d;   	//最高分线
	//100-800
	private static double SINGEL_BAD_PROPORTION_MIDDEl_100=5.0d;		//单个商品及格线
	private static double SINGEL_BAD_PROPORTION_MAX_100=10.0d;		//有分最低值
	//-100
	private static double SINGEL_BAD_PROPORTION_MIDDEl_20=5.0d;		//单个商品及格线
	private static double SINGEL_BAD_PROPORTION_MAX_20=9.0d;		//有分最低值
	
	private static double TOTAL_BAD_PROPORTION_MAX=8d;			//总评及格线
	private static double TOTAL_BAD_PROPORTION_MIDDEl=5.0d;		//总评及格线
	private static double TOTAL_BAD_PROPORTION_MIN=3d;			//最高分数线
	
	//以前描述的底线和顶线
	private static double TOTAL_INFO_DESC_SCORE_MIN=4.3;
	private static double TOTAL_INFO_DESC_SCORE_MAX=4.8;
	//限制多少才进行统计
	private static int SINGEL_TOTAL_LIMIT=20;
	private static int PAGE_SIZE=20;
	private static int Available_Page=40;
	//一个商品在同一时间段内，被同一个人评论的2次，3次的人数。可能性值(未来统计)
	//评论总数20-100个
	private static double Twice_People_min_One_stage=0;
	private static double Twice_People_max_One_stage=0.05;
	//Twice_People，Three_People或关系
	private static double Three_People_min_One_stage=0;
	private static double Three_People_max_One_stage=0.025;
	private static int FourMore_People_One_stage=0;
	//评论总数100-800个
	private static double Twice_People_min_Two_stage=0;
	private static double Twice_People_max_Two_stage=0.05;
	private static double Three_People_min_Two_stage=0;
	private static double Three_People_max_Two_stage=0.025;
	private static int FourMore_People_Two_stage=1;
	//评论总数800+个
	private static double Twice_People_min_Three_stage=0;
	private static double Twice_People_max_Three_stage=0.05;
	private static double Three_People_min_Three_stage=0;
	private static double Three_People_max_Three_stage=0.25;
	private static int FourMore_People_Three_stage=1;
	private static int Per_Addition=3;
	
	
	private static int One_stage=20;
	private static int Two_stage=100;
	private static int Three_stage=800;
	//20
	private static double per_two_score_One_stage=3;
	private static double per_three_score_One_stage=4.5;
	private static double per_four_score_One_stage=6;
	//100
	private static double per_two_score_Two_stage=2;
	private static double per_three_score_Two_stage=3.5;
	private static double per_four_score_Two_stage=5;
	//评论总数800+个
	private static double per_two_score_Three_stage=1;
	private static double per_three_score_Three_stage=2.5;
	private static double per_four_score_Three_stage=3;
	
	
	/**
	 * 计算该商品总分
	 */
	@Override
	public  double evaluateCalculate(String url,ModelMap modelMap){
		
		//分析url对应的商家用户id和商品id
		Map<String, String> parammap=this.analyzeUrl(url);
		String userid=parammap.get("userid")==null?"":parammap.get("userid");
		String productid=parammap.get("productid")==null?"":parammap.get("productid");
		if(parammap==null || parammap.size()==0){
			return 0;
		}
		//获取淘宝总体评论对象。
		TaobaoTotalAllData taobaoTotalAllData=this.analyzeTaobaoTotalAllData(userid, productid);
		
		/*TaobaoSingleData taobaoSingleData=null;
		if(taobaoTotalAllData.getData().getCount().getTotal()>SINGEL_TOTAL_LIMIT){
			taobaoSingleData=this.analyzeProductUrl(userid, productid, 1);
		}*/
		//获得当前商品的评论
		double prevScore=this.sellerTotalEvaluate(taobaoTotalAllData);
		double productScore=this.singleProductEvaluate(taobaoTotalAllData);
		//获得淘宝单个商品的评价信息
		//一期做前100页，即800条评论的重复率,记录下有用评论
		Map<String, Map<String, Object>> productEvaluate=analyzeProductUrlAll(userid, productid,taobaoTotalAllData.getData().getCount().getTotal());
		//计算评论的重复的评分
		double repeatScore=0;
		if(productEvaluate!=null){
			repeatScore=evaluateSingleRepeat(productEvaluate,taobaoTotalAllData);
		}
		//获得总的分数评价
		if(taobaoTotalAllData.getData().getCount().getTotal()<=SINGEL_TOTAL_LIMIT){
			modelMap.put("isless", true);
		}else{
			modelMap.put("isless", false);
		}
		modelMap.put("prevScore", prevScore);
		modelMap.put("productScore", productScore);
		modelMap.put("repeatScore", repeatScore);
		logger.info("卖家总评评分："+prevScore);
		logger.info("产品总评评分："+productScore);
		logger.info("评论重复率评分："+repeatScore);
		double result=CommonTools.doubleFormat(prevScore+productScore+repeatScore);
		modelMap.put("result", result);
		logger.info("总分："+result);
		return result;
	}
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
		if(correspond<TaobaoProductEvaluateService.TOTAL_INFO_DESC_SCORE_MIN){
			prevDescScore=0;
		}else if(correspond>=TaobaoProductEvaluateService.TOTAL_INFO_DESC_SCORE_MAX){
			prevDescScore=5;
		}else{
			prevDescScore=((correspond-TaobaoProductEvaluateService.TOTAL_INFO_DESC_SCORE_MIN)
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
		if(badWeight>=TaobaoProductEvaluateService.TOTAL_BAD_PROPORTION_MAX){
			weightScore=0;
		}else if(badWeight<=TaobaoProductEvaluateService.TOTAL_BAD_PROPORTION_MIN){
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE;
		}else{
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE*((TaobaoProductEvaluateService.TOTAL_BAD_PROPORTION_MAX-badWeight)/5);
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
		if(total<=TaobaoProductEvaluateService.SINGEL_TOTAL_LIMIT){
			return 0;
		}
		//计算单个商品中差评权重
		double singleWeightScore=evaluateSingleWeight(taobaoTotalAllData);
		
		return singleWeightScore;
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
		int reEval=taobaoTotalAllData.getData().getCount().getAdditional();
		double singleWeight=((new Double(badAndNormal)+reEval/TaobaoProductEvaluateService.Per_Addition)/total)*100;
		//不同评价总量，不同的计算
		if(total>TaobaoProductEvaluateService.Three_stage){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_500, singleWeight);
			
		}else if(total>TaobaoProductEvaluateService.Two_stage && total<=TaobaoProductEvaluateService.Three_stage){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_100, singleWeight);
		}else if(total<=TaobaoProductEvaluateService.Two_stage && total >TaobaoProductEvaluateService.One_stage){
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
	
	/**
	 * 计算单个商品的重复购买率得分
	 * @param taobaoSingleData
	 * @return
	 */
	public double evaluateSingleRepeat(Map<String, Map<String, Object>> productEvaluate,TaobaoTotalAllData taobaoTotalAllData){
		if(taobaoTotalAllData==null || productEvaluate==null){
			return 0;
		}
		Map<String, Object> usermap=productEvaluate.get("usermap");
		int count=taobaoTotalAllData.getData().getCount().getTotal();
		//800的特殊处理
		if(count>800){
			count=800;
		}
		int min=0;
		int twoMax=0;
		int threeMax=0;
		double score=0;
		//第一阶段
		if(count>TaobaoProductEvaluateService.One_stage && count<=TaobaoProductEvaluateService.Two_stage){
			twoMax=new Double(count*TaobaoProductEvaluateService.Twice_People_max_One_stage).intValue();
			threeMax=new Double(count*TaobaoProductEvaluateService.Three_People_max_One_stage).intValue();
			score=getUserScore(usermap
					,TaobaoProductEvaluateService.per_two_score_One_stage
					,TaobaoProductEvaluateService.per_three_score_One_stage
					,TaobaoProductEvaluateService.per_four_score_One_stage
					,min,twoMax,threeMax,TaobaoProductEvaluateService.FourMore_People_One_stage);
		}else if(count>TaobaoProductEvaluateService.Two_stage && count<=TaobaoProductEvaluateService.Three_stage){
			//第二阶段
			twoMax=new Double(count*TaobaoProductEvaluateService.Twice_People_max_Two_stage).intValue();
			threeMax=new Double(count*TaobaoProductEvaluateService.Three_People_max_Two_stage).intValue();
			score=getUserScore(usermap
					,TaobaoProductEvaluateService.per_two_score_Two_stage
					,TaobaoProductEvaluateService.per_three_score_Two_stage
					,TaobaoProductEvaluateService.per_four_score_Two_stage
					,min,twoMax,threeMax,TaobaoProductEvaluateService.FourMore_People_Two_stage);
		}else if(count>TaobaoProductEvaluateService.Three_stage){
			//第 三阶段
			twoMax=new Double(count*TaobaoProductEvaluateService.Twice_People_max_Three_stage).intValue();
			threeMax=new Double(count*TaobaoProductEvaluateService.Three_People_max_Three_stage).intValue();
			score=getUserScore(usermap
					,TaobaoProductEvaluateService.per_two_score_Three_stage
					,TaobaoProductEvaluateService.per_three_score_Three_stage
					,TaobaoProductEvaluateService.per_four_score_Three_stage
					,min,twoMax,threeMax,TaobaoProductEvaluateService.FourMore_People_Three_stage);
		}
		return score;
	}
	
	//获得用户评论分数
	private double getUserScore(Map<String, Object> usermap
				,double per_two_score
				,double per_three_score
				,double per_four_score
				,int min,int twoMax,int threeMax,int four){
		double score=TaobaoProductEvaluateService.SINGEL_INFO_EVALUATE_Repeat_PROPORTION_SCORE;
		EvaluateTime evaluateTime=getEvaluatePeople(usermap);
		if(evaluateTime.getTwicePeople()>twoMax){
			score=score-(evaluateTime.getTwicePeople()-twoMax)*per_two_score;
		}
		if(evaluateTime.getThreestimesPeople()>threeMax){
			score=score-(evaluateTime.getThreestimesPeople()-threeMax)*per_three_score;
		}
		if(evaluateTime.getMoreThreestimesPeople()>four){
			score=score-(evaluateTime.getMoreThreestimesPeople()-four)*per_four_score;
		}
		if(score<=0){
			score=0;
		}
		return score;
	}
	
	private  TaobaoSingleData analyzeProductUrl(String userNumid,String auctionNumId,int page){
		if(userNumid.equals("") || auctionNumId.equals("")){
			return null;
		}
		TaobaoSingleData taobaoSingleData=new TaobaoSingleData();
		//以前的请求方式
		//SpiderRegex regex = new SpiderRegex();
		//String htmltext = regex.gethtmlContent(EvaluateConfig.taobao_evaluate_url+"?userNumId="+userNumid+"&auctionNumId="+auctionNumId+"&showContent=1&currentPage="+page,"gbk");
		String htmltext=CommonTools.requestUrl(EvaluateConfig.taobao_evaluate_url, "gbk", "userNumId="+userNumid+"&auctionNumId="+auctionNumId+"&showContent=1&currentPage="+page);
		try {
			taobaoSingleData=json2TaobaoSingleData(htmltext);
		} catch (Exception e) {
			logger.error("分析淘宝商品评论对象出错！error is "+e.getMessage());
		}
		return taobaoSingleData;
	}
	
	/**
	 * 根据用户id和商品解析评论，计算重复率，记录有用信息
	 * @param userNumid		卖家id
	 * @param auctionNumId	商品id
	 * @param totalcount	评论总数
	 * @return
	 */
	private Map<String, Map<String, Object>> analyzeProductUrlAll(String userNumid,String auctionNumId,int totalcount){
		if(0==totalcount || totalcount<=SINGEL_TOTAL_LIMIT){
			return null;
		}
		Date start=new Date(); 
		//需要返回的map
		Map<String, Map<String, Object>> result=new HashMap<String, Map<String,Object>>();
		//评论用户map
		Map<String, Object> userMap=new HashMap<String, Object>();
		Map<String, Object> useFulMap=new HashMap<String, Object>();
		int totalpage=0;
		//是否是整页数据，如果不是就加一页
		if(totalcount%PAGE_SIZE==0){
			totalpage=totalcount/PAGE_SIZE;
		}else{
			totalpage=totalcount/PAGE_SIZE+1;
		}
		if(totalpage>Available_Page){
			totalpage=Available_Page;
		}
		for(int page=1;page<=totalpage;page++){
			logger.info("当前page："+page);
			TaobaoSingleData taobaoSingleData=this.analyzeProductUrl(userNumid, auctionNumId, page);
			userMap=createUserMap(taobaoSingleData, userMap, useFulMap);
		}
		logger.info("use time:"+(new Date().getTime()-start.getTime())+"ms");
		//获得用户评论大于2次的信息。
		
		result.put("usermap", userMap);
		return result;
	}
	
	/**
	 * 统计不同次数评论的人
	 * @param userMap
	 * @return
	 */
	private EvaluateTime getEvaluatePeople(Map<String, Object> userMap){
		if(userMap==null){
			return null;
		}
		EvaluateTime evaluateTime=new EvaluateTime();
		Iterator<String> it=userMap.keySet().iterator();
		while(it.hasNext()){
			Integer temp=(Integer)userMap.get(it.next());
			if(temp==2){
				evaluateTime.setTwicePeople(evaluateTime.getTwicePeople()+1);
			}else if(temp==3){
				evaluateTime.setThreestimesPeople(evaluateTime.getThreestimesPeople()+1);
			}else if(temp>3){
				evaluateTime.setMoreThreestimesPeople(evaluateTime.getMoreThreestimesPeople()+1);
			}
		}
		return evaluateTime;
	}
	
	/**
	 * 根据用户信息构建用户map
	 * @param taobaoSingleData
	 * @param userMap
	 * @return
	 */
	private Map<String, Object> createUserMap(TaobaoSingleData taobaoSingleData,Map<String, Object> userMap,Map<String, Object> useFulMap){
		if(taobaoSingleData==null || taobaoSingleData.getRateListInfo()==null || taobaoSingleData.getRateListInfo().getRateList()==null){
			return userMap;
		}
		//构造usermap,即用户评论的用户信息
		for(TaobaoEvaluate  taobaoEvaluate:taobaoSingleData.getRateListInfo().getRateList()){
			//判断是否已经存在该用户,如果存在则+1
			if(userMap.containsKey(taobaoEvaluate.getDisplayUserNick())){
				userMap.put(taobaoEvaluate.getDisplayUserNick(), (Integer)userMap.get(taobaoEvaluate.getDisplayUserNick())+1);
			}else{
				userMap.put(taobaoEvaluate.getDisplayUserNick(), new Integer(1));
			}
			//有用评论暂存
		}
		return userMap;
	}
	
	public TaobaoSingleData json2TaobaoSingleData(String jsonStr) {
		//ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		 JSONObject jsonObject = null;  
		 //setDataFormat2JAVA();   
        // jsonObject = JSONObject.fromObject(jsonStr.substring(1).substring(0, jsonStr.length()-2));
        // jsonObject = JSONObject.fromObject(jsonStr.substring("TB.detailRate =".length()));
		 //logger.info("-----------"+jsonStr+"---");
		 jsonObject = JSONObject.fromObject(jsonStr.substring("TB.detailRate =".length()+4));
		//TaobaoTotalAllData taobaoTotalAllData = gson.fromJson(jsonStr, TaobaoTotalAllData.class);
         Map<String, Class> alllist = new HashMap<String, Class>();
         alllist.put("rateList", TaobaoEvaluate.class);
         alllist.put("rateList", TaobaoEvaluate.class);
         TaobaoSingleData taobaoSingleData = (TaobaoSingleData)JSONObject.toBean(jsonObject, TaobaoSingleData.class,alllist);
		return taobaoSingleData;
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
//		SpiderRegex regex = new SpiderRegex();
//		String htmltext = regex.gethtmlContent(EvaluateConfig.taobao_evaluate_total_url+"?userNumId="+userNumid+"&auctionNumId="+auctionNumId,"gbk");
		//现在的请求方式
		String htmltext=CommonTools.requestUrl(EvaluateConfig.taobao_evaluate_total_url, "gbk", "userNumId="+userNumid+"&auctionNumId="+auctionNumId);
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
		if(url==null || !(url.startsWith(EvaluateConstConfig.TAOBAOHEAD) || url.startsWith(EvaluateConstConfig.TAOBAOHEAD.replaceAll("http://", "")))){
			return null;
		}
		//取得商品id//http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757
		String productid=url.substring(url.indexOf("id=")+3);
		if(productid!=null){
			parammap.put("productid", productid);
		}
		//解析url内容
		SpiderRegex regex = new SpiderRegex();
		//String htmltext = regex.gethtmlContent(url,"gbk");
		String htmltext=CommonTools.requestUrl(url, "gbk");
		String userRegex = "; userid=(.*?);";
		String shopRegex = "; shopId=(.*?);";
//		String siteCategoryRegex = "siteCategory=(.*?);siteInstanceId=";
		//shopId=106471556; userid=1819877675;siteCategory=2
		String[] userid = regex.htmlregex(htmltext,userRegex,true);
		String[] shopid = regex.htmlregex(htmltext,shopRegex,true);
//		String[] siteCategoryid = regex.htmlregex(htmltext,siteCategoryRegex,true);
		if(userid!=null && userid.length>0){
			parammap.put("userid", userid[0]);
		}
		if(shopid!=null && shopid.length>0){
			parammap.put("shopid", shopid[0]);
		}
//		if(siteCategoryid!=null && siteCategoryid.length>0){
//			parammap.put("siteCategoryid", siteCategoryid[0]);
//		}
		logger.info("userid is :---"+userid[0]+"--");
		logger.info("shopid is :---"+shopid[0]+"--");
//		logger.info("siteCategoryid is :---"+siteCategoryid[0]+"--");
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
        //jsonObject = JSONObject.fromObject(jsonStr.substring(1).substring(0, jsonStr.length()-2));
		//jsonObject = JSONObject.fromObject(jsonStr.substring(2).substring(0, jsonStr.length()-2));
		//新版请求淘宝
		//System.out.println("---------------"+jsonStr.substring(5).substring(0, jsonStr.length()-6)+"---------");
		 jsonObject = JSONObject.fromObject(jsonStr.substring(5).substring(0, jsonStr.length()-6));
         Map<String, Class> mycollection = new HashMap<String, Class>();
         mycollection.put("correspondList", String.class);
         mycollection.put("impress", TaobaoImpress.class);
         mycollection.put("spuRatting", String.class);
		//TaobaoTotalAllData taobaoTotalAllData = gson.fromJson(jsonStr, TaobaoTotalAllData.class);
         TaobaoTotalAllData taobaoTotalAllData = (TaobaoTotalAllData)JSONObject.toBean(jsonObject, TaobaoTotalAllData.class,mycollection);
		return taobaoTotalAllData;
	}
	//test
	public static void main(String[] args) {
		TaobaoProductEvaluateService taobaoProductEvaluate=new TaobaoProductEvaluateService();
		//taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757");
		//taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a230r.1.14.25.llmAw7&id=35047474226");
		taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a1z10.3.w4002-1374277071.31.yiyibf&id=7994294308",new ModelMap() );
		//taobaoProductEvaluate.evaluateCalculate("http://item.taobao.com/item.htm?spm=a230r.1.14.4.XuVzpt&id=36125840220",new ModelMap() );
//		Map<String, String> parammap=taobaoProductEvaluate.analyzeUrl("http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757");
//		taobaoProductEvaluate.sellerTotalEvaluate(parammap);
		//System.out.println(new Long(Math.round(new Double(1.8))).intValue());
	}
	@Override
	public double evaluateCalculate(String url, ModelMap modelMap,
			List<TmallAnalyzeBean> tmallAnalyzeBeanList) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void AnalyzeTmallBrand() {
		// TODO Auto-generated method stub
		
	}
}
