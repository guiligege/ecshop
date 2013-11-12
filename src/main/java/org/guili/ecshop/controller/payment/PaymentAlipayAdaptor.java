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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.guili.ecshop.bean.payment.SalesOrder;
import org.guili.ecshop.util.Md5Encrypt;
import org.springframework.ui.Model;


public class PaymentAlipayAdaptor extends AbstractPaymentAdaptor{

	public static final String		POST_URL			= "https://mapi.alipay.com/gateway.do";

	public static final String		POST_PARTNER		= "partner";

	public static final String		POST_KEY			= "key";

	public static final String		POST_SERVICE		= "service";

	public static final String		POST_SIGN_TYPE		= "sign_type";

	public static final String		POST_BODY			= "body";

	public static final String		POST_TOTAL_FEE		= "total_fee";

	public static final String		POST_PAYMENT_TYPE	= "payment_type";

	public static final String		POST_PAYMETHOD		= "paymethod";

	public static final String		POST_SELLER_EMAIL	= "seller_email";

	public static final String		POST_SUBJECT		= "subject";

	public static final String		POST_RETURN_URL		= "return_url";

	public static final String		POST_NOTIFY_URL		= "notify_url";

	public static final String		POST_SHOW_URL		= "show_url";

	public static final String		POST_PAYGATEWAY		= "paygateway";

	public static final String		POST_OUT_TRADE_NO	= "out_trade_no";

	public static final String		POST_CHARSET		= "_input_charset";

	public static final String		POST_TOKEN			= "token";

	public static final String		POST_SIGN			= "sign";

	public static final String		SERVER				= "create_direct_pay_by_user";

	public static final String		PAYMENT_TYPE		= "1";

	public static final String		PAYMENT_METHOD		= "directPay";

	public static final String		INPUT_CHARSET		= "UTF-8";

	// 修改 nike 参数
	public static final String		SELLER_EMAIL		= AliPayConfig.payment_seller_email;//"alipay-test14@alipay.com";//alipay4nikestore@jumbomart.cn";

	public static final String		SUBJECT				= "Converse官方商城商品";

	public static final String		BODY				= "Converse官方商城商品";

	public static final String		PARTNER				= AliPayConfig.payment_partner;//"2088201564862550";//2088401678374334";

	protected static final String	key					= AliPayConfig.payment_key;//"2ttncpw4rjezx7iseue0m7r5bm9okrye";//"o04sv8yg7wq9m97nkmedkr3olh5469aa";

	public static final String		ALIPAY_NOTIFY_URL	= "https://mapi.alipay.com/gateway.do?service=notify_verify&";
	
	
	public static final String 		ANTI_PHISHING_KEY 	= "anti_phishing_key";
	
	public static final String 		EXTER_INVOKE_IP		= "exter_invoke_ip";
	
	private static final Log logger = LogFactory.getLog(PaymentAlipayAdaptor.class);

	public Map<String, String> beginPayment(SalesOrder so,Model model,HttpServletRequest request,HttpServletResponse response){
		Map<String, String> paymentData = new LinkedHashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		params.put(POST_SERVICE, SERVER);
		params.put(POST_PARTNER, PARTNER);
		params.put(POST_SUBJECT, SUBJECT);
		params.put(POST_BODY, BODY);
		params.put(POST_OUT_TRADE_NO, so.getCode());
		params.put(POST_TOTAL_FEE,so.getTotalforOrder().setScale(2, BigDecimal.ROUND_HALF_UP).toString()); 
		params.put(POST_PAYMENT_TYPE, PAYMENT_TYPE);
		params.put(POST_PAYMETHOD, PAYMENT_METHOD);
		params.put(POST_SELLER_EMAIL, SELLER_EMAIL);
		params.put(POST_RETURN_URL, getReturnUrl(so.getCode(), request));
		params.put(POST_NOTIFY_URL, getFeedbackUrlByPaymentType(so.getPaymentType(), request));
		params.put(POST_CHARSET, INPUT_CHARSET);
//		params.put("antiphishing", "0");
		// 支付宝联合登录
		String strToken = (String) request.getSession().getAttribute("aliPayToken");
		if (strToken != null && strToken.length() > 0){
			params.put(POST_TOKEN, strToken);
			paymentData.put(POST_TOKEN, strToken);
		}
		//防钓鱼参数
		String encrypt_key = this.getAlipayTimestamp();
		String exter_invoke_ip = this.getIpAddr(request);
		params.put(ANTI_PHISHING_KEY, encrypt_key);
		params.put(EXTER_INVOKE_IP, exter_invoke_ip);
		
		String signStr = getContent(params, key);
		logger.info("signStr:"+signStr);
		String sign = Md5Encrypt.md5(getContent(params, key), INPUT_CHARSET);
		logger.info("sign:"+sign);
		paymentData.put(POST_SIGN, sign);
		paymentData.put(POST_SIGN_TYPE, "MD5");
		paymentData.put(POST_SERVICE, SERVER);
		paymentData.put(POST_PARTNER, PARTNER);
		paymentData.put(POST_SUBJECT, SUBJECT);
		paymentData.put(POST_BODY, BODY);
		paymentData.put(POST_OUT_TRADE_NO, so.getCode());
		paymentData.put(POST_TOTAL_FEE, so.getTotalforOrder().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		paymentData.put(POST_PAYMENT_TYPE, PAYMENT_TYPE);
		paymentData.put(POST_PAYMETHOD, PAYMENT_METHOD);
		paymentData.put(POST_SELLER_EMAIL, SELLER_EMAIL);
		paymentData.put(POST_RETURN_URL, getReturnUrl(so.getCode(), request));
		paymentData.put(POST_NOTIFY_URL, getFeedbackUrlByPaymentType(so.getPaymentType(), request));
		paymentData.put(POST_CHARSET, INPUT_CHARSET);
		paymentData.put("postUrl", POST_URL);
		
		paymentData.put(ANTI_PHISHING_KEY, encrypt_key);
		paymentData.put(EXTER_INVOKE_IP, exter_invoke_ip);
		
		model.addAttribute("postItem", paymentData);
		model.addAttribute("postUrl", POST_URL);
		model.addAttribute("postMethod", "get");
		return paymentData;
	}

	public String paymentAutoResult(HttpServletRequest request,HttpServletResponse response){
		if (paymentResult(null, request, response)){
			return request.getParameter(PaymentAlipayAdaptor.POST_OUT_TRADE_NO);
		}else{
			return null;
		}
	}

	/***
	 * 这个是整合帐号之前的
	 * @param soCode
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean old_paymentResult(String soCode,HttpServletRequest request,HttpServletResponse response){
		Map<String, String[]> paramData = request.getParameterMap();
		String alipayNotifyURL = ALIPAY_NOTIFY_URL + "partner=" + PARTNER + "&notify_id=" + paramData.get("notify_id")[0];
		String responseTxt = check(alipayNotifyURL);
		// 获得POST 过来参数设置到新的params中
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> iter = paramData.keySet().iterator(); iter.hasNext();){
			String name = (String) iter.next();
			String[] values = paramData.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++){
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			if (name.equals("body") || name.equals("subject")){
				valueStr = BODY;
			}
			params.put(name, valueStr);
		}
		String soCodeInParam = params.get(POST_OUT_TRADE_NO);
		// String totalFee=params.get(POST_TOTAL_FEE);
		String mysign = sign(params, key);
		if (soCode == null || soCode.equals(soCodeInParam)){
			if (mysign.equals(paramData.get("sign")[0]) && responseTxt.equals("true")){
				// 付款成功
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	/***
	 * 这个是整合帐号之后的   2013-03-29
	 * @param soCode
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean paymentResult(String soCode,HttpServletRequest request,HttpServletResponse response){
		Map<String, String[]> paramData = request.getParameterMap();
		// 获得POST 过来参数设置到新的params中
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> iter = paramData.keySet().iterator(); iter.hasNext();){
			String name = iter.next();
			String[] values = paramData.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++){
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			if (name.equals("body") || name.equals("subject")){
				valueStr = BODY;
			}
			params.put(name, valueStr);
		}
		// 参数里面的订单号
		String soCodeInParam = params.get(POST_OUT_TRADE_NO);

		if (soCode == null || soCode.equals(soCodeInParam)){
			String mysign = sign(params, key);
			boolean isNotifySignOk = mysign.equals(paramData.get("sign")[0]);

			if (isNotifySignOk){
				String notify_id = paramData.get("notify_id")[0];

				// 先用 统一账号 验证
				boolean isNotifyVerify_common = doNotifyVerify(notify_id, PARTNER);
				if (isNotifyVerify_common){
					// 通过 直接返回true
					return true;
				}/*else{
					// 不通过 再用老的 验证
					String partner_old = com.jumbo.shop.util.AliPayConfig.OLDPARTNER;// 这里写老的 partner notify 不需要key
					boolean isNotifyVerify_old = doNotifyVerify(notify_id, partner_old);
					// 可以来点 log
					// log.debug("doNotifyVerify_old {}:{}", soCode,isNotifyVerify_old);

					return isNotifyVerify_old;// 老的验证成不成功 直接返回了
				}*/
			}
		}
		return false;
	}

	/**
	 * 发送请求到 Alipay验证
	 * 
	 * @param notify_id
	 *            notify_id
	 * @param partner
	 *            partner
	 * @return
	 */
	private boolean doNotifyVerify(String notify_id,String partner){
		String alipayNotifyURL = ALIPAY_NOTIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
		String responseTxt = check(alipayNotifyURL);

		boolean isSuccess = responseTxt.equals("true");
		// 付款成功
		return isSuccess;
	}
	

	private static String getContent(Map<String, String> params,String privateKey){
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++){
			String key = (String) keys.get(i);
			String value = params.get(key).toString();
			if (i == keys.size() - 1){
				prestr = prestr + key + "=" + value;
			}else{
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr + privateKey;
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param
	 * @param
	 * @return 获取url内容
	 */
	private String check(String urlvalue){
		String inputLine = "";
		try{
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		}catch (Exception e){
			e.printStackTrace();
		}
		return inputLine;
	}

	private String sign(Map<String, String> params,String privateKey){
		Properties properties = new Properties();
		for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();){
			String name = (String) iter.next();
			Object value = params.get(name);
			if (name == null || name.equalsIgnoreCase("sign") || name.equalsIgnoreCase("sign_type")){
				continue;
			}
			properties.setProperty(name, value.toString());
		}
		String content = getSignatureContent(properties);
		return sign(content, privateKey);
	}

	@SuppressWarnings("unchecked")
	public static String getSignatureContent(Properties properties){
		StringBuffer content = new StringBuffer();
		@SuppressWarnings("rawtypes")
		List keys = new ArrayList(properties.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++){
			String key = (String) keys.get(i);
			String value = properties.getProperty(key);
			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}
		return content.toString();
	}

	public static String sign(String content,String privateKey){
		if (privateKey == null){
			return null;
		}
		String signBefore = content + privateKey;
		return Md5Encrypt.md5(signBefore, INPUT_CHARSET);
	}

	public String getPaymentSlipNo(HttpServletRequest request){
		return request.getParameter(POST_OUT_TRADE_NO);
	}
	
	public String getAlipayTimestamp(){
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", AliPayConfig.APLIPAY_QUERYTIME_SERVICENAME);
		param.put("_input_charset",INPUT_CHARSET );
		param.put("partner",PARTNER);
		param.put("sign_type", AliPayConfig.sign_type);
		String sign = sign(param, key);
		
		String urlvalue = AliPayConfig.alipay_path+"?partner="+PARTNER+"&service="
			+AliPayConfig.APLIPAY_QUERYTIME_SERVICENAME+"&_input_charset="+INPUT_CHARSET+"&sign_type="+AliPayConfig.sign_type
			+"&sign="+sign;
		try{
			 URL url = new URL(urlvalue);
	         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	         InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
	         SAXReader saxReader = new SAXReader();
	         Document document = saxReader.read(in);  
	         Element rootElement = document.getRootElement();
	         Element t = rootElement.element("is_success");
	         if("T".equals(t.getText().toUpperCase())){
	         	Element keyElement = rootElement.element("response").element("timestamp").element("encrypt_key");
	         	return URLDecoder.decode(keyElement.getText(),"utf-8");
	         }else{
	        	 throw new Exception("获取支付宝防钓鱼时间戳失败");
	         }
		}catch (Exception e) {
			logger.error("getAlipayTimestamp is wrong!");
			return "";
		}
	}
	
	public String getIpAddr(HttpServletRequest request) { 
		String ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		ip = request.getRemoteAddr(); 
		}
		if(ip == null){
			ip = "127.0.0.1";
		}
		return ip; 
		} 

}
