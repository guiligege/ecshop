package org.guili.ecshop.impl.credit;

import java.util.List;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;
import org.guili.ecshop.dao.credit.TmallAnalyzeDao;
import org.guili.ecshop.util.BasicSqlSupport;

public class TmallAnalyzeDaoImpl extends BasicSqlSupport implements TmallAnalyzeDao {
	private static Logger logger=Logger.getLogger(TmallAnalyzeDaoImpl.class);
	/**
	 * 保存天猫分析对象
	 */
	@Override
	public boolean addTmallAnalyzeBean(TmallAnalyzeBean tmallAnalyzeBean)
			throws Exception {
		boolean flag=false;
		int count=this.session.insert("org.guili.ecshop.dao.credit.TmallAnalyzeDao.addTmallAnalyzeBean", tmallAnalyzeBean);
		if(count>0){
			flag=true;
		}
		return flag;
	}
	
}
