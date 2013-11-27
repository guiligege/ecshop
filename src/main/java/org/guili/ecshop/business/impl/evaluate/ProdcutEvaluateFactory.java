package org.guili.ecshop.business.impl.evaluate;

import org.guili.ecshop.business.credit.IProductEvaluateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品评论分析器工厂
 * @ClassName:   ProdcutEvaluateFactory 
 * @Description: 产品评论分析器工厂(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-19 下午7:28:48 
 *
 */
public class ProdcutEvaluateFactory {
	
	private static final Logger	logger	= LoggerFactory.getLogger(ProdcutEvaluateFactory.class);
	//各各不同网站的产品评论分析器
	private  IProductEvaluateService taobaoProductEvaluate;
	private  IProductEvaluateService tmallProductEvaluate;
	private  IProductEvaluateService jdProductEvaluate;
	private  IProductEvaluateService yxProductEvaluate;
	private  IProductEvaluateService qqProductEvaluate;
	private  IProductEvaluateService yhdProductEvaluate;
	private  IProductEvaluateService suningProductEvaluate;
	private  IProductEvaluateService gomeProductEvaluate;
	private  IProductEvaluateService tmallProductEvaluateCraw;
	
	/**
	 * 根据url生成商品评价器 
	 * @param url
	 * @return
	 */
	public  IProductEvaluateService getProdcutEvaluate(String url){
		if(url==null || url.equals("")){
			return null;
		}
		/**
		 * 通过url确认具体的商品评论分析器
		 */
		if(url.startsWith(EvaluateConstConfig.TAOBAOHEAD)){
			logger.debug("return taobaoProductEvaluate");
			return taobaoProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.TMALLHEAD)){
			logger.debug("return tmallProductEvaluate");
			return tmallProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.JDHEAD)){
			logger.debug("return jdProductEvaluate");
			return jdProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.YXHEAD)){
			logger.debug("return yxProductEvaluate");
			return yxProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.QQHEAD)){
			logger.debug("return qqProductEvaluate");
			return qqProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.YHDHEAD)){
			logger.debug("return yhdProductEvaluate");
			return yhdProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.SUNINGHEAD)){
			logger.debug("return suningProductEvaluate");
			return suningProductEvaluate;
		}else if(url.contains(EvaluateConstConfig.BRANDTMALL)){
			logger.debug("return suningProductEvaluate");
			return tmallProductEvaluateCraw;
		}else if(url.startsWith(EvaluateConstConfig.GUOMEIHEAD)){
			logger.debug("return gomeProductEvaluate");
			return gomeProductEvaluate;
		}
		return null;
	}

	public IProductEvaluateService getTaobaoProductEvaluate() {
		return taobaoProductEvaluate;
	}

	public void setTaobaoProductEvaluate(IProductEvaluateService taobaoProductEvaluate) {
		this.taobaoProductEvaluate = taobaoProductEvaluate;
	}

	public IProductEvaluateService getTmallProductEvaluate() {
		return tmallProductEvaluate;
	}

	public void setTmallProductEvaluate(IProductEvaluateService tmallProductEvaluate) {
		this.tmallProductEvaluate = tmallProductEvaluate;
	}

	public IProductEvaluateService getJdProductEvaluate() {
		return jdProductEvaluate;
	}

	public void setJdProductEvaluate(IProductEvaluateService jdProductEvaluate) {
		this.jdProductEvaluate = jdProductEvaluate;
	}

	public IProductEvaluateService getYxProductEvaluate() {
		return yxProductEvaluate;
	}

	public void setYxProductEvaluate(IProductEvaluateService yxProductEvaluate) {
		this.yxProductEvaluate = yxProductEvaluate;
	}

	public IProductEvaluateService getQqProductEvaluate() {
		return qqProductEvaluate;
	}

	public void setQqProductEvaluate(IProductEvaluateService qqProductEvaluate) {
		this.qqProductEvaluate = qqProductEvaluate;
	}

	public IProductEvaluateService getYhdProductEvaluate() {
		return yhdProductEvaluate;
	}

	public void setYhdProductEvaluate(IProductEvaluateService yhdProductEvaluate) {
		this.yhdProductEvaluate = yhdProductEvaluate;
	}

	public IProductEvaluateService getSuningProductEvaluate() {
		return suningProductEvaluate;
	}

	public void setSuningProductEvaluate(IProductEvaluateService suningProductEvaluate) {
		this.suningProductEvaluate = suningProductEvaluate;
	}

	public IProductEvaluateService getGomeProductEvaluate() {
		return gomeProductEvaluate;
	}

	public void setGomeProductEvaluate(IProductEvaluateService gomeProductEvaluate) {
		this.gomeProductEvaluate = gomeProductEvaluate;
	}

	public IProductEvaluateService getTmallProductEvaluateCraw() {
		return tmallProductEvaluateCraw;
	}

	public void setTmallProductEvaluateCraw(
			IProductEvaluateService tmallProductEvaluateCraw) {
		this.tmallProductEvaluateCraw = tmallProductEvaluateCraw;
	}
	
}
