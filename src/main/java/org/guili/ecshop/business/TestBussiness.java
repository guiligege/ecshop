package org.guili.ecshop.business;

import java.util.Date;

import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.bean.Shop;
import org.guili.ecshop.dao.ITestTableDao;
import org.guili.ecshop.dao.SemiconductorDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TestBussiness {
	
	private ITestTableDao testDao=null;
	private SemiconductorDao semiconductorDao=null;

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
	
	//testSemiconductor
	public void saveSemiconductor(){
		Semiconductor semiconductor=new Semiconductor();
		semiconductor.setCreateTime(new Date());
		semiconductorDao.insertSemiconductor(semiconductor);
	}
	public void updateSemiconductor(){
		Semiconductor semiconductor=new Semiconductor();
		semiconductor.setCode("123");
		semiconductor.setSourcesite("123");
		semiconductor.setPrice("123");
		semiconductor.setDiscount("123");
		semiconductor.setCreateTime(new Date());
		semiconductorDao.updateSemiconductor(semiconductor);
	}
	public Semiconductor findone()throws Exception{
		return semiconductorDao.selectone(1L);
	}
	public Semiconductor findonebycodeAndurl()throws Exception{
		Semiconductor semiconductor=new Semiconductor();
		semiconductor.setCode("123");
		semiconductor.setSourcesite("123");
		return semiconductorDao.selectonebyCodeAndUrl(semiconductor);
	}
	
	public void test(){
		System.out.println("test");
	}

	public void test1(){
		System.out.println("test1");
	}
	public void test2(){
		System.out.println("test2");
	}

	public SemiconductorDao getSemiconductorDao() {
		return semiconductorDao;
	}

	public void setSemiconductorDao(SemiconductorDao semiconductorDao) {
		this.semiconductorDao = semiconductorDao;
	}
}
