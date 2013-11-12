/*
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package org.guili.ecshop.controller.payment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PaymentAutoController{
	private static final Log log = LogFactory.getLog(PaymentAutoController.class);
	
	@RequestMapping(value = "/payment/{paymentType}/feedback.htm")
	public void autoReceive(@PathVariable
	String paymentType,HttpServletRequest request,HttpServletResponse response){
		
		try{
			receiveLog(2, request);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		PaymentAdaptor adaptor = null;
		try{
			/*request.setAttribute("userMember", getUserDetails().getMember());
			if (paymentManager.soPyamentByNotify(paymentType, request, response)){
				log.error("payment autoReceive Exception");
				response.getWriter().write("success");
			}else{
				log.error("payment autoReceive Exception");
				response.getWriter().write("fail");
			}*/
		}catch (Exception e){
			log.error("payment autoReceive Exception:"+e.getMessage());
			log.error("payment autoReceive Exception");
			/*try{
				if (adaptor != null)
					response.getWriter().write("fail");
					salesManager.savePaymentLog(adaptor.getPaymentSlipNo(request), false,e.getMessage());
			}catch (IOException e1){
				e1.printStackTrace();
			}*/
		}
	}

	@RequestMapping(value = "/paymentconfirm/{code}.htm")
	public String paymentReceive(@PathVariable
	String code,Model model,HttpServletRequest request,HttpServletResponse response){
		
		/*//判断是否从支付页面过来
		if(Validator.isNotNullOrEmpty(request.getSession().getAttribute("comeFromSuccess"))
				&&"yes".equalsIgnoreCase(request.getSession().getAttribute("comeFromSuccess").toString())){
			try{
				request.getSession().removeAttribute("comeFromSuccess");
				receiveLog(1, request);
			}catch (IOException e){
				log.error(e.getMessage());
			}
			try{
				if(paymentManager.soPayment(code, model, request, response)){
					SalesOrder so = salesOrderService.findByCodeByHql(code);
					 List<SoLine> soLines= so.getSoLines();
	
						//拼接omniture用到的商品字符串
						StringBuffer sb = new StringBuffer();
						for (SoLine soLine : soLines) {
							if (sb.length() > 0) sb.append(",");
							sb.append(";").append(soLine.getSkuCode());
							sb.append(";").append(soLine.getRequestedQty());
							sb.append(";").append(soLine.getUnitPrice());
						}
						model.addAttribute("productStr", sb);
					model.addAttribute("soLines", soLines);
	                model.addAttribute("so", so);	
	                model.addAttribute("projectName", getServerRootWithContextPath(request));
					return "shop.payment.confirm";
				}else{
					return "shop.paymentError";
				}
			}catch (Exception e) {
				log.debug("--------- exception:"+code);
				model.addAttribute("soCode", code);
				return "shop.paymentError";
			}
		}else{
			SalesOrder so = salesOrderService.findByCodeByHql(code);
			if(Validator.isNotNullOrEmpty(so)){
				return "redirect:/sales/orderlist.htm";
			}
			return "redirect:/index.htm";
		}*/
		return "shop.payment.confirm";
	}

	private void receiveLog(int type,HttpServletRequest request) throws IOException{
		File file = new File("payment.log");
		FileOutputStream out = new FileOutputStream(file, true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer bf = new StringBuffer();
		bf.append("[").append(dateFormat.format(new Date())).append("] [").append(type).append("] ");
		bf.append(request.getRequestURL()).append("?");
		Map<?, ?> params = request.getParameterMap();
		for (Object key : params.keySet()){
			bf.append(key).append("=");
			bf.append(((String[]) params.get(key))[0]).append("&");
		}
		bf.append("\r\n");
		out.write(bf.toString().getBytes());
		out.flush();
		out.close();
	}
}
