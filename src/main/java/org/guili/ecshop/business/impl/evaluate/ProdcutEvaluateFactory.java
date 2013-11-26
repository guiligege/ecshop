package org.guili.ecshop.business.impl.evaluate;

import org.guili.ecshop.business.credit.IProductEvaluate;
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
	private  IProductEvaluate taobaoProductEvaluate;
	private  IProductEvaluate tmallProductEvaluate;
	private  IProductEvaluate jdProductEvaluate;
	private  IProductEvaluate yxProductEvaluate;
	private  IProductEvaluate qqProductEvaluate;
	private  IProductEvaluate yhdProductEvaluate;
	private  IProductEvaluate suningProductEvaluate;
	private  IProductEvaluate gomeProductEvaluate;
	
	/**
	 * 根据url生成商品评价器 
	 * @param url
	 * @return
	 */
	public  IProductEvaluate getProdcutEvaluate(String url){
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
			return tmallProductEvaluate;
		}else if(url.startsWith(EvaluateConstConfig.GUOMEIHEAD)){
			logger.debug("return gomeProductEvaluate");
			return gomeProductEvaluate;
		}
		return null;
	}

	public IProductEvaluate getTaobaoProductEvaluate() {
		return taobaoProductEvaluate;
	}

	public void setTaobaoProductEvaluate(IProductEvaluate taobaoProductEvaluate) {
		this.taobaoProductEvaluate = taobaoProductEvaluate;
	}

	public IProductEvaluate getTmallProductEvaluate() {
		return tmallProductEvaluate;
	}

	public void setTmallProductEvaluate(IProductEvaluate tmallProductEvaluate) {
		this.tmallProductEvaluate = tmallProductEvaluate;
	}

	public IProductEvaluate getJdProductEvaluate() {
		return jdProductEvaluate;
	}

	public void setJdProductEvaluate(IProductEvaluate jdProductEvaluate) {
		this.jdProductEvaluate = jdProductEvaluate;
	}

	public IProductEvaluate getYxProductEvaluate() {
		return yxProductEvaluate;
	}

	public void setYxProductEvaluate(IProductEvaluate yxProductEvaluate) {
		this.yxProductEvaluate = yxProductEvaluate;
	}

	public IProductEvaluate getQqProductEvaluate() {
		return qqProductEvaluate;
	}

	public void setQqProductEvaluate(IProductEvaluate qqProductEvaluate) {
		this.qqProductEvaluate = qqProductEvaluate;
	}

	public IProductEvaluate getYhdProductEvaluate() {
		return yhdProductEvaluate;
	}

	public void setYhdProductEvaluate(IProductEvaluate yhdProductEvaluate) {
		this.yhdProductEvaluate = yhdProductEvaluate;
	}

	public IProductEvaluate getSuningProductEvaluate() {
		return suningProductEvaluate;
	}

	public void setSuningProductEvaluate(IProductEvaluate suningProductEvaluate) {
		this.suningProductEvaluate = suningProductEvaluate;
	}

	public IProductEvaluate getGomeProductEvaluate() {
		return gomeProductEvaluate;
	}

	public void setGomeProductEvaluate(IProductEvaluate gomeProductEvaluate) {
		this.gomeProductEvaluate = gomeProductEvaluate;
	}
}
