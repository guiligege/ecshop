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

import org.guili.ecshop.bean.payment.SalesOrder;
import org.guili.ecshop.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PaymentAdaptorFactory{

	private static final Logger	log	= LoggerFactory.getLogger(PaymentAdaptorFactory.class);

	private static String		defaultbank;

	public static PaymentAdaptor getAdaptor(String paymentType){
		log.debug("the param paymentType:{}", paymentType);
		log.debug("the param Constants.pay_isCanGoToPay:{}", Constants.pay_isCanGoToPay);
		/**
		 * 全部 配置,测试环境不可以去付款
		 */
		if (Constants.pay_isCanGoToPay){
			if (SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(paymentType)){
				log.debug("return new PaymentAlipayAdaptor()");
				return new PaymentAlipayAdaptor();
			}/*else if (SalesOrder.SO_PAYMENT_TYPE_NETPAY.equals(paymentType)){
				log.debug("new PaymentNetPayAdept(defaultbank)");
				return new PaymentNetPayAdept(defaultbank);
			}else if (SalesOrder.SO_PAYMENT_TYPE_TENPAY.equals(paymentType)){
				log.debug("new PaymentTenpayAdaptor()");
				return new PaymentTenpayAdaptor();
			}else if (SalesOrder.SO_PAYMENT_TYPE_ALIPAY_CREDIT.equals(paymentType)){
				log.debug("return new PaymentGreditCardAdaptor()");
				return new PaymentGreditCardAdaptor();
			}else if (SalesOrder.SO_PAYMENT_TYPE_ALIPAY_EXPRESS.equals(paymentType)){
				log.debug("return new PaymentExpressPayAdaptor()");
				return new PaymentExpressPayAdaptor(defaultbank);
			}*/
		}
		return null;
	}

	public String getDefaultbank(){
		return defaultbank;
	}

	public static void setDefaultbank(String defaultbank){
		PaymentAdaptorFactory.defaultbank = defaultbank;
	}
}
