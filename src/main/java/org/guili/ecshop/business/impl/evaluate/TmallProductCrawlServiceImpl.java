package org.guili.ecshop.business.impl.evaluate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.taobao.EvaluateTime;
import org.guili.ecshop.bean.credit.taobao.TaobaoImpress;
import org.guili.ecshop.bean.credit.taobao.TaobaoTotalAllData;
import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;
import org.guili.ecshop.bean.credit.tmall.TmallEvaluate;
import org.guili.ecshop.bean.credit.tmall.TmallSingleEvaluate;
import org.guili.ecshop.business.credit.IProductEvaluateService;
import org.guili.ecshop.business.credit.ITmallProductService;
import org.guili.ecshop.dao.credit.TmallAnalyzeDao;
import org.guili.ecshop.util.CommonTools;
import org.guili.ecshop.util.FileTools;
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
public class TmallProductCrawlServiceImpl implements IProductEvaluateService {
	
	private static Logger logger=Logger.getLogger(TmallProductCrawlServiceImpl.class);
	
	private ITmallProductService tmallProductService;
	private static int OUTERPAGESIZE=50;
	//限制多少才进行统计
	private static int SINGEL_TOTAL_LIMIT=20;
	private static int PAGE_SIZE=100;
	private static int Available_Page=10;
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
		private static double SINGEL_BAD_PROPORTION_MAX_500=6.0d;		//有分最低值
		private static double SINGEL_BAD_PROPORTION_MIDDEl=5.0d;		//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MIN=1.0d;   		//最高分线
		//100-800
		private static double SINGEL_BAD_PROPORTION_MIDDEl_100=5.0d;	//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MAX_100=5.0d;		//有分最低值
		//-100
		private static double SINGEL_BAD_PROPORTION_MIDDEl_20=5.0d;		//单个商品及格线
		private static double SINGEL_BAD_PROPORTION_MAX_20=4.0d;		//有分最低值
		
		private static double TOTAL_BAD_PROPORTION_MAX=6.5d;			//总评及格线
		private static double TOTAL_BAD_PROPORTION_MIDDEl=4.0d;			//总评及格线
		private static double TOTAL_BAD_PROPORTION_MIN=2.5d;			//最高分数线
		
		//以前描述的底线和顶线
		private static double TOTAL_INFO_DESC_SCORE_MIN=4.5;
		private static double TOTAL_INFO_DESC_SCORE_MAX=4.8;
		
		//一个商品在同一时间段内，被同一个人评论的2次，3次的人数。可能性值(未来统计)
		//评论总数20-100个
		private static double Twice_People_min_One_stage=0;
		private static double Twice_People_max_One_stage=0.07;
		//Twice_People，Three_People或关系
		private static double Three_People_min_One_stage=0;
		private static double Three_People_max_One_stage=0.035;
		private static int FourMore_People_One_stage=0;
		//评论总数100-800个
		private static double Twice_People_min_Two_stage=0;
		private static double Twice_People_max_Two_stage=0.07;
		private static double Three_People_min_Two_stage=0;
		private static double Three_People_max_Two_stage=0.035;
		private static int FourMore_People_Two_stage=1;
		//评论总数800+个 
		private static double Twice_People_min_Three_stage=0;
		private static double Twice_People_max_Three_stage=0.07;
		private static double Three_People_min_Three_stage=0;
		private static double Three_People_max_Three_stage=0.35;
		private static int FourMore_People_Three_stage=1;
		private static int Per_Addition=3;
		
		
		private static int One_stage=20;
		private static int Two_stage=100;
		private static int Three_stage=800;
		//20
		private static double per_two_score_One_stage=2;
		private static double per_three_score_One_stage=3.5;
		private static double per_four_score_One_stage=3;
		//100
		private static double per_two_score_Two_stage=1;
		private static double per_three_score_Two_stage=2.5;
		private static double per_four_score_Two_stage=3;
		//评论总数800+个
		private static double per_two_score_Three_stage=0.5;
		private static double per_three_score_Three_stage=1.5;
		private static double per_four_score_Three_stage=3;
		/**
		 * 计算tmall商品评价分数
		 */
	@Override
	public double evaluateCalculate(String url, ModelMap modelMap,List<TmallAnalyzeBean> tmallAnalyzeBeanList) throws Exception{
		//分析url对应的商家用户id和商品id
		Map<String, String> parammap=this.analyzeUrl(url);
		String userid=parammap.get("userid")==null?"":parammap.get("userid");
		String productid=parammap.get("productid")==null?"":parammap.get("productid");
		if(parammap==null || parammap.size()==0){
			return 0;
		}
		logger.debug("---------------------------"+parammap.get("shopname")==null?"":parammap.get("shopname").trim());
		//记录天猫商品评价对象
		TmallAnalyzeBean tmallAnalyzeBean=new TmallAnalyzeBean();
		tmallAnalyzeBean.setBrandname(parammap.get("shopname")==null?"":parammap.get("shopname").trim());
		tmallAnalyzeBean.setProducturl(url);
		//获取淘宝总体评论对象。
		TaobaoTotalAllData taobaoTotalAllData=this.analyzeTaobaoTotalAllData(userid, productid);
		
		if(taobaoTotalAllData==null || taobaoTotalAllData.getData()==null || taobaoTotalAllData.getData().getCorrespond()==null){
			logger.info("taobaoTotalAllData.getData(),taobaoTotalAllData.getData().getCorrespond() is null");
			return 0;
		}
		//获得当前商品的评论
		//抓取提升速度
//		double prevScore=this.sellerTotalEvaluate(taobaoTotalAllData);
//		double productScore=this.singleProductEvaluate(taobaoTotalAllData);
		//获得淘宝单个商品的评价信息
		//一期做前100页，即800条评论的重复率,记录下有用评论
		Map<String, Map<String, Object>> productEvaluate=analyzeProductUrlAll(userid, productid,taobaoTotalAllData.getData().getCount().getTotal());
		//计算评论的重复的评分
		//double repeatScore=0;
		//收集商品评价
		if(taobaoTotalAllData!=null){
			//店铺整体描述
			double correspond=Double.parseDouble(taobaoTotalAllData.getData().getCorrespond());
			tmallAnalyzeBean.setCorrespond(correspond);
			//整体中差评权重
			List<String> correspondList=taobaoTotalAllData.getData().getCorrespondList();
			if(correspondList==null || correspondList.size()!=5){
				return 0;
			}
			logger.debug("correspondList--->"+correspondList.toString());
			double badWeight=Double.parseDouble(correspondList.get(2))+Double.parseDouble(correspondList.get(3))+Double.parseDouble(correspondList.get(4));
			logger.debug("中差评权重："+badWeight);
			tmallAnalyzeBean.setBadWeightALL(badWeight);
			//单个商品的评价总数
			int total=taobaoTotalAllData.getData().getCount().getTotal();
			tmallAnalyzeBean.setTotal(total);
			//单个商品的中差评权重
			int badAndNormal=taobaoTotalAllData.getData().getCount().getBad()
					+taobaoTotalAllData.getData().getCount().getNormal();
			int reEval=taobaoTotalAllData.getData().getCount().getAdditional();
			tmallAnalyzeBean.setBadAndNormalWeightSingle(badAndNormal+reEval/TmallProductCrawlServiceImpl.Per_Addition);
			tmallAnalyzeBean.setBadWeightSingle(badAndNormal);
		}
		if(productEvaluate!=null){
			//抓取提升速度
			//repeatScore=evaluateSingleRepeat(productEvaluate,taobaoTotalAllData);
			//查询2,3,3次以上的次数
			EvaluateTime evaluateTime=this.getEvaluatePeople(productEvaluate.get("usermap"));
			tmallAnalyzeBean.setTwicePerson(evaluateTime.getTwicePeople());
			tmallAnalyzeBean.setThreestimesPerson(evaluateTime.getThreestimesPeople());
			tmallAnalyzeBean.setMoreThreestimesPerson(evaluateTime.getMoreThreestimesPeople());
		}
		//获得总的分数评价
		if(taobaoTotalAllData.getData().getCount().getTotal()<=SINGEL_TOTAL_LIMIT){
			modelMap.put("isless", true);
		}else{
			modelMap.put("isless", false);
		}
//		modelMap.put("prevScore", prevScore);
//		modelMap.put("productScore", productScore);
//		modelMap.put("repeatScore", repeatScore);
//		logger.info("卖家总评评分："+prevScore);
//		logger.info("产品总评评分："+productScore);
//		logger.info("重复评论评分："+repeatScore);
		double result=0;
//		double result=CommonTools.doubleFormat(prevScore+productScore+repeatScore);
//		modelMap.put("result", result);
//		logger.info("总分："+result);
		/*try {
			tmallAnalyzeDao.addTmallAnalyzeBean(tmallAnalyzeBean);
		} catch (Exception e) {
			logger.error("tmallAnalyze 数据保存error！！"+e.getMessage());
		}*/
		//批量加入list，优化保存
		tmallAnalyzeBeanList.add(tmallAnalyzeBean);
		return result;
	}
	
	/**
	 * 构建TmallAnalyzeBean对象
	 * @param tmallAnalyzeBean
	 */
	public void createTmallAnalyzeBean(TaobaoTotalAllData taobaoTotalAllData,TmallAnalyzeBean tmallAnalyzeBean){
		if(taobaoTotalAllData==null){
			return;
		}
		double correspond=Double.parseDouble(taobaoTotalAllData.getData().getCorrespond());
		List<String> correspondList=taobaoTotalAllData.getData().getCorrespondList();
		if(correspondList==null || correspondList.size()!=5){
			return ;
		}
		logger.debug("correspondList--->"+correspondList.toString());
		double badWeight=Double.parseDouble(correspondList.get(2))+Double.parseDouble(correspondList.get(3))+Double.parseDouble(correspondList.get(4));
		logger.debug("中差评权重："+badWeight);
		int total=taobaoTotalAllData.getData().getCount().getTotal();
		int badAndNormalWeightSingle=taobaoTotalAllData.getData().getCount().getBad()
				+taobaoTotalAllData.getData().getCount().getNormal();
		int reEval=taobaoTotalAllData.getData().getCount().getAdditional();
		int badWeightSingle=badAndNormalWeightSingle+reEval/reEval/TmallProductCrawlServiceImpl.Per_Addition;
		logger.debug("correspond-->"+correspond+"badWeight-->"+badWeight+"total-->"+total+"badAndNormalWeightSingle-->"+badAndNormalWeightSingle+"badWeightSingle-->"+badWeightSingle);
		tmallAnalyzeBean.setTotal(total);
		tmallAnalyzeBean.setCorrespond(correspond);
		tmallAnalyzeBean.setBadWeightALL(badWeight);
		tmallAnalyzeBean.setBadAndNormalWeightSingle(badAndNormalWeightSingle);
		tmallAnalyzeBean.setBadWeightSingle(badWeightSingle);
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
		if(count>TmallProductCrawlServiceImpl.One_stage && count<=TmallProductCrawlServiceImpl.Two_stage){
			twoMax=new Double(count*TmallProductCrawlServiceImpl.Twice_People_max_One_stage).intValue();
			threeMax=new Double(count*TmallProductCrawlServiceImpl.Three_People_max_One_stage).intValue();
			score=getUserScore(usermap
					,TmallProductCrawlServiceImpl.per_two_score_One_stage
					,TmallProductCrawlServiceImpl.per_three_score_One_stage
					,TmallProductCrawlServiceImpl.per_four_score_One_stage
					,min,twoMax,threeMax,TmallProductCrawlServiceImpl.FourMore_People_One_stage);
		}else if(count>TmallProductCrawlServiceImpl.Two_stage && count<=TmallProductCrawlServiceImpl.Three_stage){
			//第二阶段
			twoMax=new Double(count*TmallProductCrawlServiceImpl.Twice_People_max_Two_stage).intValue();
			threeMax=new Double(count*TmallProductCrawlServiceImpl.Three_People_max_Two_stage).intValue();
			score=getUserScore(usermap
					,TmallProductCrawlServiceImpl.per_two_score_Two_stage
					,TmallProductCrawlServiceImpl.per_three_score_Two_stage
					,TmallProductCrawlServiceImpl.per_four_score_Two_stage
					,min,twoMax,threeMax,TmallProductCrawlServiceImpl.FourMore_People_Two_stage);
		}else if(count>TmallProductCrawlServiceImpl.Three_stage){
			//第 三阶段
			twoMax=new Double(count*TmallProductCrawlServiceImpl.Twice_People_max_Three_stage).intValue();
			threeMax=new Double(count*TmallProductCrawlServiceImpl.Three_People_max_Three_stage).intValue();
			score=getUserScore(usermap
					,TmallProductCrawlServiceImpl.per_two_score_Three_stage
					,TmallProductCrawlServiceImpl.per_three_score_Three_stage
					,TmallProductCrawlServiceImpl.per_four_score_Three_stage
					,min,twoMax,threeMax,TmallProductCrawlServiceImpl.FourMore_People_Three_stage);
		}
		return score;
	}
	
	//获得用户评论分数
	private double getUserScore(Map<String, Object> usermap
				,double per_two_score
				,double per_three_score
				,double per_four_score
				,int min,int twoMax,int threeMax,int four){
		double score=TmallProductCrawlServiceImpl.SINGEL_INFO_EVALUATE_Repeat_PROPORTION_SCORE;
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
		if(totalcount/PAGE_SIZE>Available_Page){
			totalpage=Available_Page;
		}
		
		for(int page=1;page<=totalpage;page++){
			logger.info("当前page："+page);
			TmallEvaluate tmallEvaluate=this.analyzeProductUrl(userNumid, auctionNumId, page);
			userMap=createUserMap(tmallEvaluate, userMap, useFulMap);
		}
		logger.info("use time:"+(new Date().getTime()-start.getTime())+"ms");
		//获得用户评论大于2次的信息。
		
		result.put("usermap", userMap);
		return result;
	}
	
	/**
	 * 根据用户信息构建用户map
	 * @param tmallEvaluate
	 * @param userMap
	 * @return
	 */
	private Map<String, Object> createUserMap(TmallEvaluate tmallEvaluate,Map<String, Object> userMap,Map<String, Object> useFulMap){
		if(tmallEvaluate==null ){
			return userMap;
		}
		//构造usermap,即用户评论的用户信息
		for(TmallSingleEvaluate  taobaoEvaluate:tmallEvaluate.getItems()){
			//判断是否已经存在该用户,如果存在则+1
			if(userMap.containsKey(taobaoEvaluate.getBuyer())){
				userMap.put(taobaoEvaluate.getBuyer(), (Integer)userMap.get(taobaoEvaluate.getBuyer())+1);
			}else{
				userMap.put(taobaoEvaluate.getBuyer(), new Integer(1));
			}
			//有用评论暂存
		}
		return userMap;
	}
	
	private  TmallEvaluate analyzeProductUrl(String userNumid,String auctionNumId,int page){
		if( auctionNumId.equals("")){
			return null;
		}
		TmallEvaluate tmallEvaluate=new TmallEvaluate();
		//以前的请求方式
		//http://amtmall.com/ajax/rate_list.do?item_id=xxxx&p=2&ps=15			
		String htmltext=CommonTools.requestUrl(EvaluateConfig.tmall_evaluate_url, "gbk", "item_id="+auctionNumId+"&ps="+TmallProductCrawlServiceImpl.PAGE_SIZE+"&p="+page);
		try {
			tmallEvaluate=json2TmallEvaluate(htmltext);
		} catch (Exception e) {
			logger.error("分析淘宝商品评论对象出错！error is "+e.getMessage());
		}
		return tmallEvaluate;
	}
	
	/**
	 * 转换json to 对象
	 * @param jsonStr
	 * @return
	 */
	public TmallEvaluate json2TmallEvaluate(String jsonStr) {
		 JSONObject jsonObject = null;  
		 jsonObject = JSONObject.fromObject(jsonStr);
         Map<String, Class> alllist = new HashMap<String, Class>();
         alllist.put("items", TmallSingleEvaluate.class);
         TmallEvaluate tmallEvaluate = (TmallEvaluate)JSONObject.toBean(jsonObject, TmallEvaluate.class,alllist);
		return tmallEvaluate;
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
		//获取商品id ex:http://item.taobao.com/item.htm?spm=a230r.1.14.71.akJQrl&id=20048694757
		String productid="";
		if(url.contains("&")){
			productid=url.substring(url.indexOf("id=")+3,url.indexOf("id=")+3+url.substring(url.indexOf("id=")+3).indexOf("&"));
		}else{
			productid=url.substring(url.indexOf("id=")+3);
		}
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
		//String htmltext = regex.gethtmlContent(url,"gbk");
		String htmltext=CommonTools.requestUrl(url, "gbk");
		//正则解析商家id和商家shopid
		String userRegex = "; userid=(.*?);";
		String shopRegex = "; shopId=(.*?);";
		String shopnameRegex = "slogo\">(.*?)</a>";
		String[] userid = regex.htmlregex(htmltext,userRegex,true);
		String[] shopid = regex.htmlregex(htmltext,shopRegex,true);
		String[] shopnames = regex.htmlregex(htmltext,shopnameRegex,false);
		
		if(userid!=null && userid.length>0){
			parammap.put("userid", userid[0]);
		}
		if(shopid!=null && shopid.length>0){
			parammap.put("shopid", shopid[0]);
		}
		if(shopnames!=null && shopnames.length>0){
			parammap.put("shopname", shopnames[0]);
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
		if(taobaoTotalAllData==null || taobaoTotalAllData.getData()==null || taobaoTotalAllData.getData().getCorrespond()==null){
			return 0;
		}
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
		if(taobaoTotalAllData==null || taobaoTotalAllData.getData()==null || taobaoTotalAllData.getData().getCorrespondList()==null){
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
		if(badWeight>=TmallProductCrawlServiceImpl.TOTAL_BAD_PROPORTION_MAX){
			weightScore=0;
		}else if(badWeight<=TmallProductCrawlServiceImpl.TOTAL_BAD_PROPORTION_MIN){
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE;
		}else{
			weightScore=TOTAL_INFO_BAD_PROPORTION_SCORE*((TmallProductCrawlServiceImpl.TOTAL_BAD_PROPORTION_MAX-badWeight)/5);
		}
		return weightScore;
	}
	/**
	 * 计算商品总体貌似分值
	 * @param taobaoTotalAllData
	 * @return
	 */
	private double calculateTotalDescScore(TaobaoTotalAllData taobaoTotalAllData){
		if(taobaoTotalAllData==null || taobaoTotalAllData.getData()==null || taobaoTotalAllData.getData().getCorrespond()==null){
			return 0;
		}
		double correspond=Double.parseDouble(taobaoTotalAllData.getData().getCorrespond());
		double prevDescScore=0;
		if(correspond<TmallProductCrawlServiceImpl.TOTAL_INFO_DESC_SCORE_MIN){
			prevDescScore=0;
		}else if(correspond>=TmallProductCrawlServiceImpl.TOTAL_INFO_DESC_SCORE_MAX){
			prevDescScore=5;
		}else{
			prevDescScore=((correspond-TmallProductCrawlServiceImpl.TOTAL_INFO_DESC_SCORE_MIN)
							/(TOTAL_INFO_DESC_SCORE_MAX-TOTAL_INFO_DESC_SCORE_MIN))*TOTAL_INFO_SCORE;
		}
		return prevDescScore;
	}
	
	/**
	 * 计算单个商品的评价权重分数
	 */
	@Override
	public double singleProductEvaluate(Object obj) {
		if(obj==null){
			return 0;
		}
		TaobaoTotalAllData taobaoTotalAllData=(TaobaoTotalAllData)obj;
		int total=taobaoTotalAllData.getData().getCount().getTotal();
		if(total<=TmallProductCrawlServiceImpl.SINGEL_TOTAL_LIMIT){
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
		double singleWeight=((new Double(badAndNormal)+reEval/TmallProductCrawlServiceImpl.Per_Addition)/total)*100;
		//不同评价总量，不同的计算
		if(total>TmallProductCrawlServiceImpl.Three_stage){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_500, singleWeight);
			
		}else if(total>TmallProductCrawlServiceImpl.Two_stage && total<=TmallProductCrawlServiceImpl.Three_stage){
			singleWeightScore=this.getSingleStageScore(SINGEL_BAD_PROPORTION_MIN, SINGEL_BAD_PROPORTION_MAX_100, singleWeight);
		}else if(total<=TmallProductCrawlServiceImpl.Two_stage && total >TmallProductCrawlServiceImpl.One_stage){
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
		TmallProductCrawlServiceImpl tmallProductEvaluate=new TmallProductCrawlServiceImpl();
		int i=0;
		while (i<100000){
			//Map<String, String> params=tmallProductEvaluate.analyzeUrl("http://detail.tmall.com/item.htm?spm=a1z10.3.w17-36871585.13.XZLzs0&id=19571165288&");
			TmallEvaluate tmallEvaluate=tmallProductEvaluate.analyzeProductUrl("", "19571165288", 1);
			logger.info(i+"params---->"+tmallEvaluate.getItems().size());
			i++;
		}
//		TmallProductCrawlServiceImpl tmallProductEvaluate=new TmallProductCrawlServiceImpl();
//		tmallProductEvaluate.evaluateCalculate("http://detail.tmall.com/item.htm?spm=a1z10.3.w17-36871585.13.XZLzs0&id=19571165288&",new ModelMap(),new ArrayList<TmallAnalyzeBean>() );
	}

	@Override
	public double evaluateCalculate(String url, ModelMap modelMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 分析tmallbrand
	 */
	@Override
	public void AnalyzeTmallBrand(){
		List<String> tmallBrandurls=AnalyzeTmallList.getInstance().getTmallBrandList();
		boolean flag=false;
		String backurl="";
		for(String brandurl:tmallBrandurls){
			List<String> allbrandurls=null;
			if(brandurl.equals("http://jieyouxb.tmall.com/")){
				continue;
			}
			//防止重复分析
			try {
				backurl=FileTools.read(EvaluateConfig.tmallbackurl);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(backurl==null || backurl.equals("") || backurl.equals(brandurl)){
				flag=true;
			}
			if(!flag){
				continue;
			}
			//具体分析
			if(brandurl!=null && !brandurl.isEmpty()){
				allbrandurls=AnalyzeTmallList.getInstance().getBrandItemsList(brandurl);
				if(allbrandurls==null || allbrandurls.isEmpty()){
					continue;
				}
				int outerpage=0;
				if(allbrandurls.size()%TmallProductCrawlServiceImpl.OUTERPAGESIZE==0){
					outerpage=allbrandurls.size()/TmallProductCrawlServiceImpl.OUTERPAGESIZE;
				}else{
					outerpage=allbrandurls.size()/TmallProductCrawlServiceImpl.OUTERPAGESIZE+1;
				}
				for(int i=0;i<outerpage;i++){
					List<TmallAnalyzeBean> tmallAnalyzeBeanList=new ArrayList<TmallAnalyzeBean>();
					for(int j=0;j<50;j++){
						if(i*50+j>=allbrandurls.size()){
							break;
						}
						String myurl=allbrandurls.get(i*50+j);
						try {
							this.evaluateCalculate(myurl,new ModelMap(), tmallAnalyzeBeanList);
						} catch (Exception e) {
							logger.error("AnalyzeTmallBrand evaluateCalculate error url:"+myurl);
							//备份信息，防止服务器宕机
							FileTools.appendToFile(EvaluateConfig.tmalllogurl, brandurl);
							e.printStackTrace();
						}
					}
					logger.debug("AnalyzeTmallBrand page save to db :----->"+i);
					//this.addTmallAnalyzeBeanList(tmallAnalyzeBeanList);
					//保存到数据库
					tmallProductService.addTmallAnalyzeBeanList(tmallAnalyzeBeanList);
				}
			}
			
			//备份信息，防止服务器宕机
			try {
				FileTools.write(EvaluateConfig.tmallbackurl, brandurl);
				//已经抓取过的商家
				FileTools.appendToFile(EvaluateConfig.tmallbackurlline, brandurl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
	}

	
	public ITmallProductService getTmallProductService() {
		return tmallProductService;
	}

	public void setTmallProductService(ITmallProductService tmallProductService) {
		this.tmallProductService = tmallProductService;
	}
	

}
