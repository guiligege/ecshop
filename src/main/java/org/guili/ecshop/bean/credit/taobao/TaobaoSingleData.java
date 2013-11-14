package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;
import java.util.List;

public class TaobaoSingleData implements Serializable{

	private static final long serialVersionUID = 11234221111L;
	private TaobaoSingleScoreInfo scoreInfo;
	private TaobaoSingleCreditInfo rateListInfo;
	private String babyRateJsonList;
	private String detailRate;
	public TaobaoSingleScoreInfo getScoreInfo() {
		return scoreInfo;
	}
	public void setScoreInfo(TaobaoSingleScoreInfo scoreInfo) {
		this.scoreInfo = scoreInfo;
	}
	public TaobaoSingleCreditInfo getRateListInfo() {
		return rateListInfo;
	}
	public void setRateListInfo(TaobaoSingleCreditInfo rateListInfo) {
		this.rateListInfo = rateListInfo;
	}
	public String getBabyRateJsonList() {
		return babyRateJsonList;
	}
	public void setBabyRateJsonList(String babyRateJsonList) {
		this.babyRateJsonList = babyRateJsonList;
	}
	public String getDetailRate() {
		return detailRate;
	}
	public void setDetailRate(String detailRate) {
		this.detailRate = detailRate;
	}
	
}
