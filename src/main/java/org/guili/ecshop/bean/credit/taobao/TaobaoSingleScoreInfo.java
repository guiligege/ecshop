package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

public class TaobaoSingleScoreInfo implements Serializable{
	private static final long serialVersionUID = 112342211116L;
	private String merchandisScore;
	private String width;
	private String isB2cSeller;
	private String noMark;
	private String merchandisTotal;
	public String getMerchandisScore() {
		return merchandisScore;
	}
	public void setMerchandisScore(String merchandisScore) {
		this.merchandisScore = merchandisScore;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getIsB2cSeller() {
		return isB2cSeller;
	}
	public void setIsB2cSeller(String isB2cSeller) {
		this.isB2cSeller = isB2cSeller;
	}
	public String getNoMark() {
		return noMark;
	}
	public void setNoMark(String noMark) {
		this.noMark = noMark;
	}
	public String getMerchandisTotal() {
		return merchandisTotal;
	}
	public void setMerchandisTotal(String merchandisTotal) {
		this.merchandisTotal = merchandisTotal;
	}
	
	
}
