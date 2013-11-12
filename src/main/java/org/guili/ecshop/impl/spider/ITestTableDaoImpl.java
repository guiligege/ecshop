package org.guili.ecshop.impl.spider;

import org.guili.ecshop.bean.spider.Shop;
import org.guili.ecshop.dao.spider.ITestTableDao;
import org.guili.ecshop.util.BasicSqlSupport;

public class ITestTableDaoImpl extends BasicSqlSupport implements ITestTableDao{

	@Override
	public boolean add(Shop shop) throws Exception {
		boolean flag=false;
		int count=this.session.insert("org.guili.ecshop.dao.ITestTableDao.add", shop);
		if(count>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Shop selectone() {
		Shop shop=(Shop)this.session.selectOne("org.guili.ecshop.dao.ITestTableDao.selectone");
		return shop;
	}


}
