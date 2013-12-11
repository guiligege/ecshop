package org.guili.ecshop.controller.evaluate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.guili.ecshop.business.impl.spider.TaobaoTopSpider;
import org.guili.ecshop.business.spider.ITopStoreService;

/**
 * 淘宝高信誉卖家信息
 * @ClassName:   TopStoreController 
 * @Description: 商品评论控制类(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-19 下午7:14:11 
 *
 */
@Controller
@RequestMapping("/")
public class TopStoreController  {
	
	private Logger logger=Logger.getLogger(TopStoreController.class);
	
	@Resource(name="topStoreService")
	private ITopStoreService topStoreService;
	@Resource(name="taobaoTopSpider")
	private TaobaoTopSpider taobaoTopSpider;
	
	
	//下面两种方式都ok
	@RequestMapping(value="store/result.htm")
	public String viewUser(HttpServletRequest request,ModelMap modelMap) throws Exception{
//			Shop shop=testBusiness.getone();
		topStoreService.saveTopstores(taobaoTopSpider.classesContent());
//		log.info("logger--->"+shop.getName());
		return "result1";
	}
}
