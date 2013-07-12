package org.guili.ecshop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.guili.ecshop.bean.Shop;
import org.guili.ecshop.business.MutiThreadTest;
import org.guili.ecshop.business.TestBussiness;
import org.guili.ecshop.business.TestMuti;
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

	public void setTestBusiness(TestBussiness testBusiness) {
		this.testBusiness = testBusiness;
	}
	@RequestMapping(value="/add.htm")
	public String addUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
		testBusiness.add();
		return "result";
	}
	//下面两种方式都ok
	@RequestMapping(value="/result.htm")
	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
		Shop shop=testBusiness.getone();
		log.info("logger--->"+shop.getName());
		return "result1";
	}
	@RequestMapping("/resultview.htm")
	public ModelAndView viewUser1(HttpServletRequest request) throws Exception{
		Shop shop=testBusiness.getone();
		log.debug("logger1--->"+shop.getName());
		//多线程测试
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
