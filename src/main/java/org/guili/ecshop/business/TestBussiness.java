package org.guili.ecshop.business;

import org.guili.ecshop.bean.Shop;
import org.guili.ecshop.dao.ITestTableDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TestBussiness {
	
	private ITestTableDao testDao=null;

	public ITestTableDao getTestDao() {
		return testDao;
	}

	public void setTestDao(ITestTableDao testDao) {
		this.testDao = testDao;
	}
	
	public void add()throws Exception{
		Shop shop=new Shop();
		shop.setId(5L);
		shop.setName("test");
		try {
			testDao.add(shop);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Shop getone()throws Exception{
		return testDao.selectone();
	}

}
