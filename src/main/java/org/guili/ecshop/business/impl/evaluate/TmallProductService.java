package org.guili.ecshop.business.impl.evaluate;

import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;
import org.guili.ecshop.business.credit.ITmallProductService;
import org.guili.ecshop.dao.credit.TmallAnalyzeDao;

/**
 * Tmall数据入db接口实现
 * @ClassName:   TmallProductService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-27 下午1:12:41 
 *
 */
public class TmallProductService implements ITmallProductService{
	
	private static Logger logger=Logger.getLogger(TmallProductService.class);
	/**
	 * 保存天猫分析数据用于分析
	 */
	private TmallAnalyzeDao tmallAnalyzeDao;
	
	/**
	 * 批量插入数据库
	 */
	@Override
	public void addTmallAnalyzeBeanList(List<TmallAnalyzeBean> tmallAnalyzeBeanList){
		try {
			this.addTmallAnalyzeList(tmallAnalyzeBeanList);
		} catch (Exception e) {
			logger.debug("AnalyzeTmallBrand error in page:--url--"+tmallAnalyzeBeanList.get(0).getProducturl());
		}
	}
	
	/**
	 * 保存天猫分析对象List
	 */
	public boolean addTmallAnalyzeList(List<TmallAnalyzeBean> tmallAnalyzeBean) {
		for(TmallAnalyzeBean onetmallAnalyzeBean:tmallAnalyzeBean){
			try {
				tmallAnalyzeDao.addTmallAnalyzeBean(onetmallAnalyzeBean);
			} catch (Exception e) {
				logger.error("tmallAnalyze 数据保存error！！-->"+onetmallAnalyzeBean.getProducturl());
			    e.printStackTrace();
			}
		}
		return true;
	}
	
	public TmallAnalyzeDao getTmallAnalyzeDao() {
		return tmallAnalyzeDao;
	}

	public void setTmallAnalyzeDao(TmallAnalyzeDao tmallAnalyzeDao) {
		this.tmallAnalyzeDao = tmallAnalyzeDao;
	}
}
