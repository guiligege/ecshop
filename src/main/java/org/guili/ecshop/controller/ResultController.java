package org.guili.ecshop.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.guili.ecshop.business.TestBussiness;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
//用于对应controller。区别不同的类
@RequestMapping("/result")
public class ResultController {
	@Resource(name="testBusiness")
	private TestBussiness testBusiness=null;

	public void setTestBusiness(TestBussiness testBusiness) {
		this.testBusiness = testBusiness;
	}
//	@RequestMapping
//	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		testBusiness.add();
//		return "result";
//	}
	//下面两种方式都ok
	@RequestMapping(value="/result.do")
	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
		testBusiness.getone();
		return "result1";
	}
	@RequestMapping("/resultview.do")
	public ModelAndView viewUser1(HttpServletRequest request) throws Exception{
		testBusiness.getone();
		return new ModelAndView("result");
	}
	//取json数据例子,直接返回调用页面
	//类似于struts2的void返回方法的调用
	@RequestMapping(value="/test.do")  
    @ResponseBody  
    public Object test(HttpSession session){
		
        session.setAttribute("permit", "aa");
        System.out.println("test....................");
        return session.getAttribute("permit");
    }  
	
	
}
