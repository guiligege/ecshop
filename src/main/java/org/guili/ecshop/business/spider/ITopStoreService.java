package org.guili.ecshop.business.spider;

import java.util.List;

import org.guili.ecshop.bean.spider.TopStore;

/**
 * 高信誉商家
 * @ClassName:   ITopStoreService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-12-11 下午2:10:59 
 *
 */
public interface ITopStoreService {
	public boolean addTopstore(TopStore topStore);
	public TopStore selectOneTopstore();
	
	/**
	 * 保存高信誉店铺
	 */
	public void saveTopstores(List<TopStore> topStoreList);
}
