package org.guili.ecshop.controller.payment;

import java.util.ResourceBundle;

import org.guili.ecshop.util.ResourceUtil;

public class AliPayConfig{

	public static ResourceBundle	alipay_config					= ResourceBundle.getBundle(ResourceUtil.ALIPAYFILEPATH);

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串 2088101568355903
	public static String			partner							= String.valueOf(alipay_config.getString("login_partner"));							// "2088101568351631";//"2088201564862550";//=
																																							// "2088401678374334";

	// 交易安全检验码，由数字和字母组成的32位字符串 ldpdr169a3e5n86i9f92sn3cznbyx72s
	public static String			key								= String.valueOf(alipay_config.getString("login_key"));								// "r7oltisl5570yp07xtsa0q6y4gni8ltz";//"2ttncpw4rjezx7iseue0m7r5bm9okrye";//"o04sv8yg7wq9m97nkmedkr3olh5469aa";

	/**
	 * HTTP形式消息验证地址
	 */
	public static final String		HTTP_VERIFY_URL					= "http://notify.alipay.com/trade/notify_query.do?";

	// 当前页面跳转后的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
	// 域名不能写成http://localhost/alipay.auth.authorize_jsp_utf8/return_url.jsp ，否则会导致return_url执行无效
	// 124.74.76.70:8002
	// http://www.nikestore.com.cn/member/register.htm
	// http://www.nikestore.com.cn/enterpayment.htm
	public static String			return_url						= String.valueOf(alipay_config.getString("login_return_url"));							// "http://180.168.119.194:18005/unitLogin.htm";

	public static String			query_return_url				= String.valueOf(alipay_config.getString("login_query_return_url"));					// "http://180.168.119.194:18005/enterpayment.htm";

	public static String			service							= String.valueOf(alipay_config.getString("service"));

	public static String			regsource						= String.valueOf(alipay_config.getString("regsource"));

	public static String			promote_url						= String.valueOf(alipay_config.getString("promote_url"));

	// 查询服务
	public static String			query_service					= String.valueOf(alipay_config.getString("query_service"));

	public static String			target_service					= String.valueOf(alipay_config.getString("target_service"));

	public static String			alipay_path						= String.valueOf(alipay_config.getString("alipay_path"));

	public static String			alipayName						= String.valueOf(alipay_config.getString("alipayName"));

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	// 调试用，创建TXT日志路径
	public static String			log_path						= "D:\\alipay_log_" + System.currentTimeMillis() + ".txt";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String			input_charset					= String.valueOf(alipay_config.getString("input_charset"));								;

	// 签名方式 不需修改
	public static String			sign_type						= String.valueOf(alipay_config.getString("sign_type"));

	// 访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
	public static String			transport						= String.valueOf(alipay_config.getString("transport"));

	// alipay用户等级
	public static String			vip_userGrade					= String.valueOf(alipay_config.getString("vip_userGrade"));

	// alipay用户等级状态
	public static String			user_grade_type					= String.valueOf(alipay_config.getString("user_grade_type"));							;

	/**
	 * 支付宝快捷登录,自动设值用户 默认的密码,<br>
	 * create by jinxin (2011-8-12 下午01:31:30)
	 */
	public static final String		alipayLoginAutoPassword			= String.valueOf(alipay_config.getString("alipayLoginAutoPassword"));

	/** 支付宝渠道code */
	public static final String		ALIPAY_CHANNEL_CODE				= String.valueOf(alipay_config.getString("ALIPAY_CHANNEL_CODE"));

	public static final String		DEFAULT_CHANNEL_CODE			= String.valueOf(alipay_config.getString("DEFAULT_CHANNEL_CODE"));

	public static final String		APLIPAY_QUERYTIME_SERVICENAME	= String.valueOf(alipay_config.getString("APLIPAY_QUERYTIME_SERVICENAME"));

	// ----------------支付帐号------------------------------
	public static String			payment_partner					= String.valueOf(alipay_config.getString("payment_partner"));

	public static String			payment_key						= String.valueOf(alipay_config.getString("payment_key"));

	public static String			payment_seller_email			= String.valueOf(alipay_config.getString("payment_seller_email"));

	// 订单页面登陆
	public static int				ORDER_LOGIN_TYPE				= 1;

	// 快捷页面登陆
	public static int				LOGIN_TYPE						= 2;

	public static String			return_url2						= ResourceUtil.getValue(alipay_config, "login_return_url_2");
}
