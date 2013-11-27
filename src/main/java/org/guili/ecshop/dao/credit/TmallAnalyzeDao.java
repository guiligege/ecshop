package org.guili.ecshop.dao.credit;

import java.util.List;

import org.guili.ecshop.bean.credit.tmall.TmallAnalyzeBean;

/**
 * 天猫分析dao
 * @ClassName:   TmallAnalyzeDao 
 * @Description: 用于存储并分析天猫数据(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-26 下午7:49:14 
 *
 */
public interface TmallAnalyzeDao {
	public boolean addTmallAnalyzeBean(TmallAnalyzeBean tmallAnalyzeBean)  throws Exception;
}
