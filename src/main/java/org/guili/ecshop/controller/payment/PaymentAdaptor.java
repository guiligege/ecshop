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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guili.ecshop.bean.payment.SalesOrder;
import org.springframework.ui.Model;

public interface PaymentAdaptor{

	/**
	 * 支付表单参数准备 model need set attr postItem LinkedHashMap<String,String> attr postUrl String
	 * 
	 * @param so
	 * @param model
	 *            must to set key:postItem,postUrl
	 * @param request
	 * @param response
	 *            return Map<form input name, form input value >
	 */
Map<String, String> beginPayment(SalesOrder so,Model model,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 支付网关返回验证
	 * 
	 * @param soCode
	 *            need check is equals in request soCode
	 * @param request
	 * @param response
	 * @return
	 */
	boolean paymentResult(String soCode,HttpServletRequest request,HttpServletResponse response);

	/**
	 * 支付网关后台验证
	 * 
	 * @param request
	 * @param response
	 * @return soCode
	 */
	String paymentAutoResult(HttpServletRequest request,HttpServletResponse response);

	/**
	 * 得到订单号
	 * 
	 * @param request
	 * @return
	 */
	String getPaymentSlipNo(HttpServletRequest request);
}
