package org.guili.ecshop.business;

import java.util.Date;

import org.guili.ecshop.bean.spider.Semiconductor;
import org.guili.ecshop.bean.spider.Shop;
import org.guili.ecshop.dao.spider.ITestTableDao;
import org.guili.ecshop.dao.spider.SemiconductorDao;
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
		Date now=new Date();
		System.out.println("start time:"+now.getTime()+"ms");
		//for(long i=1;i<1000000;i++){
			Shop shop=new Shop();
			shop.setId(5L);
			shop.setName("test");
			try {
				testDao.add(shop);
			} catch (Exception e) {
				throw e;
			}
		//}
		System.out.println("end time:"+(new Date().getTime()-now.getTime())+"ms");
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
