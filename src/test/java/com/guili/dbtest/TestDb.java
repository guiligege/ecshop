//package com.guili.dbtest;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.guili.ecshop.business.TestBussiness;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.support.DefaultListableBeanFactory;
//import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
//import org.springframework.beans.factory.xml.XmlBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.context.support.FileSystemXmlApplicationContext;
//import org.springframework.core.io.ClassPathResource;
//
//import junit.framework.TestCase;
//
//public class TestDb extends TestCase {
//	public TestDb(String name) {
//        super(name);
//    }
//
////	 public void testConMysql() {
////		 shopService.getShop();
////	 }
//    /*
//    */
////    public void testConMysql() {
////        ConMysql con = new ConMysql();
////        Connection c = con.getconn();
////        try {
////            Statement stmt = c.createStatement();
////            String sql = "select id,name from t_shop where id=1";
////            ResultSet rs=stmt.executeQuery(sql);
////            while(rs.next()){
////            	System.out.println(rs.getLong(1)+"---"+rs.getString(2));
////            }
//////            assertNotNull(stmt.executeQuery("select * from customer"));
//////            assertEquals(1, stmt.executeQuery("select count(*) from customer"));
////
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////    }
//	/**
//	 * 测试spring ioc
//	 * @throws Exception
//	 */
//	public void testSelect() throws Exception{
//		//BeanFactory factory=new ClassPathXmlApplicationContext("/org/guili/ecshop/config/ApplicationContext.xml").getBeanFactory();
//		BeanFactory factory=new FileSystemXmlApplicationContext("E:/myecshop/src/main/resources/org/guili/ecshop/config/ApplicationContext.xml")
//								.getBeanFactory();
//		TestBussiness TestBussiness=(TestBussiness)factory.getBean("testBusiness");
//		System.out.println(TestBussiness.getone().getName());
//		
//		SqlSessionTemplate sqlsession=(SqlSessionTemplate)factory.getBean("sqlSession");
//		//加载bean的xml
////		ClassPathResource res = new ClassPathResource("/org/guili/ecshop/config/ApplicationContext.xml");  
////		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();  
////		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);  
////		reader.loadBeanDefinitions(res); 
////		
////		XmlBeanFactory
////		TestBussiness TestBussiness=(TestBussiness)factory.getBean("testBusiness");
////		System.out.println(TestBussiness.getone().getName());
//	}
//
//    
//}
