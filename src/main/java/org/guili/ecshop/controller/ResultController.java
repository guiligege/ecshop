package org.guili.ecshop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.spider.Semiconductor;
import org.guili.ecshop.business.TestBussiness;
import org.guili.ecshop.business.impl.spider.AvnetSpiderServiceImpl;
import org.guili.ecshop.business.impl.spider.DigikeySpiderServiceImpl;
import org.guili.ecshop.business.impl.spider.MouserSpiderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//用于对应controller。区别不同的类
@RequestMapping("/")
public class ResultController {
	private Logger log=Logger.getLogger(ResultController.class);
	@Resource(name="testBusiness")
	private TestBussiness testBusiness=null;
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
	
	@RequestMapping(value="result/add.htm")
	public String addUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
		testBusiness.add();
		return "result";
	}
	//下面两种方式都ok
	@RequestMapping(value="result/result.htm")
	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Shop shop=testBusiness.getone();
		avnetSpiderServiceImpl.analysisService();
		mouserSpiderServiceImpl.analysisService();
		digikeySpiderServiceImpl.analysisService();
//		log.info("logger--->"+shop.getName());
		return "result1";
	}
	@RequestMapping(value="result/testdatabase.htm")
	public String addUser1(HttpServletRequest request,ModelMap modelMap) throws Exception{
		//testBusiness.findone();
		testBusiness.getone();
		log.info("logger--->");
		return "result";
	}
	//下面两种方式都ok
	@RequestMapping(value="result/result1.htm")
	public String viewUser1(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Semiconductor semiconductor=testBusiness.findone();
//		log.info("logger--->"+semiconductor.getCreateTime());
		Semiconductor semiconductor1=testBusiness.findonebycodeAndurl();
		log.info("logger--->"+semiconductor1.getCode());
		testBusiness.updateSemiconductor();
		return "result1";
	}
	//取json数据例子,直接返回调用页面
	//类似于struts2的void返回方法的调用
	@RequestMapping(value="result/test.htm")  
    @ResponseBody  
    public Object test(HttpSession session){
        session.setAttribute("permit", "中文");
        System.out.println("test....................");
        return session.getAttribute("permit");
    }
	@RequestMapping(value="index.htm")
	public String index(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Semiconductor semiconductor=testBusiness.findone();
//		log.info("logger--->"+semiconductor.getCreateTime());
		return "index";
	}
}
