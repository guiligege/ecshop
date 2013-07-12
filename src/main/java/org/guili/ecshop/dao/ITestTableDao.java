package org.guili.ecshop.dao;

import org.guili.ecshop.bean.Shop;

/**
 * interface
 * @author guili
 *
 */
public interface ITestTableDao {
	public boolean add(Shop shop) throws Exception;
	public Shop selectone()throws Exception;
}
