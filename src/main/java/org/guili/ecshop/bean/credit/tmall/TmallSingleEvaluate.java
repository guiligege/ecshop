package org.guili.ecshop.bean.credit.tmall;

import java.io.Serializable;

/**
 * 天猫单个评论对象
 * @author guili
 */
public class TmallSingleEvaluate  implements Serializable{
	private static final long serialVersionUID = 112342211122L;
	private String annoy;
	private String buyer;
	private String credit;
	private String date;
	private String deal;
	private String rateId;
	private String reply;
	private String text;
	private String type;
	
	public String getAnnoy() {
		return annoy;
	}
	public void setAnnoy(String annoy) {
		this.annoy = annoy;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDeal() {
		return deal;
	}
	public void setDeal(String deal) {
		this.deal = deal;
	}
	public String getRateId() {
		return rateId;
	}
	public void setRateId(String rateId) {
		this.rateId = rateId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}

}
