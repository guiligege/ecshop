
package org.guili.ecshop.controller.payment;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPaymentAdaptor implements PaymentAdaptor{

	public static final String	BASE_RETURN_URL	= "/paymentconfirm/";

	public String getReturnUrl(String soCode,HttpServletRequest request){
		return getServerRootWithContextPath(request) + BASE_RETURN_URL + soCode + ".htm";
	}

	public String getFeedbackUrlByPaymentType(String paymentType,HttpServletRequest request){
		return getServerRootWithContextPath(request) + "/payment/" + paymentType + "/feedback.htm";
	}

	protected String getServerRootWithContextPath(HttpServletRequest request){
		return "http://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort() + request.getContextPath());
	}
}
