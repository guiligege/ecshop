package org.guili.ecshop.controller.evaluate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.guili.ecshop.business.credit.IProductEvaluateService;
import org.guili.ecshop.business.impl.evaluate.ProdcutEvaluateFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品评论控制类
 * @ClassName:   EvaluateController 
 * @Description: 商品评论控制类(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-19 下午7:14:11 
 *
 */
@Controller
@RequestMapping("/")
public class EvaluateController {
	
	private Logger logger=Logger.getLogger(EvaluateController.class);
	@Resource(name="prodcutEvaluateFactory")
	private ProdcutEvaluateFactory prodcutEvaluateFactory;
	
	/**
	 * 商品评论分析
	 * @param request
	 * @param modelMap
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="evaluate/evaluateProduct.htm",method={RequestMethod. GET})
	public void  evaluateProduct(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,@RequestParam(required=false) String url) throws Exception{
		if(url==null || url.isEmpty()){
			response.getWriter().write(new JSONObject().toString());
		}
		logger.debug(url);
		//获取具体实现类
		IProductEvaluateService productEvaluate=prodcutEvaluateFactory.getProdcutEvaluate(url);
		//计算并获得商品评论信息
		productEvaluate.evaluateCalculate(url,modelMap);
		if(modelMap!=null && modelMap.size()>0){
			JSONObject jsonobj=JSONObject.fromObject(modelMap);
			response.getWriter().write(jsonobj.toString());
		}else{
			response.getWriter().write(new JSONObject().toString());
		}
		//return ;
	}
	
	/**
	 * 商品评论分析
	 * @param request
	 * @param modelMap
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="evaluate/evaluateTmallProduct.htm",method={RequestMethod. GET})
	public void  evaluateTmallProduct(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,@RequestParam(required=false) String url) throws Exception{
		if(url==null || url.isEmpty()){
			response.getWriter().write(new JSONObject().toString());
		}
		logger.debug(url);
		//获取具体实现类
		IProductEvaluateService productEvaluate=prodcutEvaluateFactory.getProdcutEvaluate(url);
		//productEvaluate.evaluateCalculate(url,modelMap);
		productEvaluate.AnalyzeTmallBrand();
		if(modelMap!=null && modelMap.size()>0){
			JSONObject jsonobj=JSONObject.fromObject(modelMap);
			response.getWriter().write(jsonobj.toString());
		}else{
			response.getWriter().write(new JSONObject().toString());
		}
		//return ;
	}
}
