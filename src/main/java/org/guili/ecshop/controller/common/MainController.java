package org.guili.ecshop.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页跳转和首页需要信息的跳转
 * @ClassName:   MainController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-19 下午7:09:51 
 *
 */
@Controller
@RequestMapping("/")
public class MainController {
	
	/**
	 * 首页跳转
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="index.htm")
	public String index(HttpServletRequest request,ModelMap modelMap) throws Exception{
//		Semiconductor semiconductor=testBusiness.findone();
//		log.info("logger--->"+semiconductor.getCreateTime());
		return "index";
	}
}
