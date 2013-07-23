package org.guili.ecshop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.Semiconductor;
import org.guili.ecshop.bean.Shop;
import org.guili.ecshop.business.MutiThreadTest;
import org.guili.ecshop.business.TestBussiness;
import org.guili.ecshop.business.TestMuti;
import org.guili.ecshop.business.impl.AvnetSpiderServiceImpl;
import org.guili.ecshop.business.impl.DigikeySpiderServiceImpl;
import org.guili.ecshop.business.impl.MouserSpiderServiceImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
//用于对应controller。区别不同的类
@RequestMapping("/result")
public class ResultController {
	private Logger log=Logger.getLogger(ResultController.class);
	@Resource(name="testBusiness")
	private TestBussiness testBusiness=null;
	//多线程测试
	@Resource(name="mutiThreadTest")
	private MutiThreadTest mutiThreadTest=null;
	@Resource(name="executor")
	private ThreadPoolTaskExecutor executor=null;
	@Resource(name="digikeySpiderServiceImpl")
	private DigikeySpiderServiceImpl digikeySpiderServiceImpl=null;
	@Resource(name="mouserSpiderServiceImpl")
	private MouserSpiderServiceImpl mouserSpiderServiceImpl=null;
	@Resource(name="avnetSpiderServiceImpl")
	private AvnetSpiderServiceImpl avnetSpiderServiceImpl=null;
	
	public void setAvnetSpiderServiceImpl(
			AvnetSpiderServiceImpl avnetSpiderServiceImpl) {
		this.avnetSpiderServiceImpl = avnetSpiderServiceImpl;
	}
	public void setMouserSpiderServiceImpl(
			MouserSpiderServiceImpl mouserSpiderServiceImpl) {
		this.mouserSpiderServiceImpl = mouserSpiderServiceImpl;
	}

	public void setDigikeySpiderServiceImpl(
			DigikeySpiderServiceImpl digikeySpiderServiceImpl) {
		this.digikeySpiderServiceImpl = digikeySpiderServiceImpl;
	}

	public void setTestBusiness(TestBussiness testBusiness) {
		this.testBusiness = testBusiness;
	}
	
	public void setMutiThreadTest(MutiThreadTest mutiThreadTest) {
		this.mutiThreadTest = mutiThreadTest;
	}

	@RequestMapping(value="/add.htm")
	public String addUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
		testBusiness.add();
		return "result";
	}
	//下面两种方式都ok
	@RequestMapping(value="/result.htm")
	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Shop shop=testBusiness.getone();
		avnetSpiderServiceImpl.analysisService();
		mouserSpiderServiceImpl.analysisService();
		digikeySpiderServiceImpl.analysisService();
//		log.info("logger--->"+shop.getName());
		return "result1";
	}
	@RequestMapping(value="/testdatabase.htm")
	public String addUser1(HttpServletRequest request,ModelMap modelMap) throws Exception{
		testBusiness.findone();
		return "result";
	}
	//下面两种方式都ok
	@RequestMapping(value="/result1.htm")
	public String viewUser1(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Semiconductor semiconductor=testBusiness.findone();
//		log.info("logger--->"+semiconductor.getCreateTime());
		Semiconductor semiconductor1=testBusiness.findonebycodeAndurl();
		log.info("logger--->"+semiconductor1.getCode());
		testBusiness.updateSemiconductor();
		return "result1";
	}
	@RequestMapping("/resultview.htm")
	public ModelAndView viewUser1(HttpServletRequest request) throws Exception{
		Shop shop=testBusiness.getone();
		log.debug("logger1--->"+shop.getName());
		//多线程测试
		mutiThreadTest.setTestBusiness(testBusiness);
		for(int i=0;i<100;i++){
			executor.execute(mutiThreadTest);
		}
		return new ModelAndView("result");
	}
	//取json数据例子,直接返回调用页面
	//类似于struts2的void返回方法的调用
	@RequestMapping(value="/test.htm")  
    @ResponseBody  
    public Object test(HttpSession session){
        session.setAttribute("permit", "中文");
        System.out.println("test....................");
        return session.getAttribute("permit");
    }  
}
