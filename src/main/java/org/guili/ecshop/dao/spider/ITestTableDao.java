package org.guili.ecshop.dao.spider;

import org.guili.ecshop.bean.spider.Shop;


/**
 * interface
 * @author guili
 *
 */
public interface ITestTableDao {
	public boolean add(Shop shop) throws Exception;
	public Shop selectone()throws Exception;
}
