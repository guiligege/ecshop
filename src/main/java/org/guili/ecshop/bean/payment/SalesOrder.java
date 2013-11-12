package org.guili.ecshop.bean.payment;

import java.math.BigDecimal;

public class SalesOrder {
	// so payment type
		/** 1 货到付款. */
		public static final String			SO_PAYMENT_TYPE_COD							= "1";

		/** 2 银行电汇. */
		public static final String			SO_PAYMENT_TYPE_TELETRANSFER				= "2";

		/** 3 网银在线. */
		public static final String			SO_PAYMENT_TYPE_NETPAY						= "3";

		/** 6 支付宝. */
		public static final String			SO_PAYMENT_TYPE_ALIPAY						= "6";

		/** 7 快钱. */
		public static final String			SO_PAYMENT_TYPE_99BILL						= "7";

		/** 9 现金收款 */
		public static final String			SO_PAYMENT_TYPE_SASH						= "9";

		/** 10 预付卡. */
		public static final String			SO_PAYMENT_TYPE_PREPAID_CARD				= "10";

		/** 11 财付通. */
		public static final String			SO_PAYMENT_TYPE_TENPAY						= "300";

		/** 12 外部积分兑换. */
		public static final String			SO_PAYMENT_TYPE_EXTERNAL_POINT				= "12";

		/**
		 * 13 招商银行
		 */
		public static final String			SO_PAYMENT_TYPE_CMBC						= "13";

		/**
		 * 14 信用卡-支付宝
		 */
		public static final String			SO_PAYMENT_TYPE_ALIPAY_CREDIT				= "14";

		/** 新华一成卡 */
		public static final String			SO_PAYMENT_TYPE_XINHUA_CARD					= "104";

		/** LEVIS淘宝B2C－支付宝 */
		public static final String			SO_PAYMENT_TYPE_LEVIS_ALIPAY_B2C			= "108";

		// ********************************国际卡 支付部分 目前 nike 已经在实现*****************************************************

		/** 131 国际卡-VISA */
		public static final String			SO_PAYMENT_TYPE_INTERNATIONALCARD_VISA		= "131";

		/** 132 国际卡-MASTER */
		public static final String			SO_PAYMENT_TYPE_INTERNATIONALCARD_MASTER	= "132";

		/** 133 国际卡 -JCB */
		public static final String			SO_PAYMENT_TYPE_INTERNATIONALCARD_JCB		= "133";

		// **********************************************************************************************

		/** 百付宝（汇付天下） */
		public static final String			SO_PAYMENT_TYPE_BAIFUBAO					= "200";

		/**
		 * 18支付宝-快捷支付网关接口
		 */
		public static final String			SO_PAYMENT_TYPE_ALIPAY_EXPRESS				= "18";

		/** 310 中国移动在线支付. */
		public static final String			SO_PAYMENT_TYPE_CMPAY						= "310";

		/** 320 银联在线支付. */
		public static final String			SO_PAYMENT_TYPE_UPOP						= "320";

		/** 601 支付宝手机支付. */
		public static final String			SO_PAYMENT_TYPE_ALIPAY_MOBILE				= "601";

		/** 999 贝宝. */
		public static final String			SO_PAYMENT_TYPE_PAYPAL						= "999";
		
		
		/** 订单编码. */
		private String						code;
		
		private BigDecimal					price;
		/** 付款方式. */
		private String						paymentType;
		
		public BigDecimal getTotalforOrder(){
			return price;
		}
		
		public String getPaymentType(){
			return paymentType;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public void setPaymentType(String paymentType) {
			this.paymentType = paymentType;
		}
}
