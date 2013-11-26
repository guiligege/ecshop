package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

public class TaobaoTotalAllData implements Serializable{
	private static final long serialVersionUID = 112342211117L;
	private String watershed;
	private TaobaoGift gift;
	private String isBeta;
	private String isShowDefaultSort;
	private TaobaoTotalEvaluate data;
	private boolean showPicRadio;
	private TbaobaoEvaluateBanner banner;
	public String getWatershed() {
		return watershed;
	}
	public void setWatershed(String watershed) {
		this.watershed = watershed;
	}
	public String getIsBeta() {
		return isBeta;
	}
	public void setIsBeta(String isBeta) {
		this.isBeta = isBeta;
	}
	public String getIsShowDefaultSort() {
		return isShowDefaultSort;
	}
	public void setIsShowDefaultSort(String isShowDefaultSort) {
		this.isShowDefaultSort = isShowDefaultSort;
	}
	public TaobaoTotalEvaluate getData() {
		return data;
	}
	public void setData(TaobaoTotalEvaluate data) {
		this.data = data;
	}
	public boolean isShowPicRadio() {
		return showPicRadio;
	}
	public void setShowPicRadio(boolean showPicRadio) {
		this.showPicRadio = showPicRadio;
	}
	public TbaobaoEvaluateBanner getBanner() {
		return banner;
	}
	public void setBanner(TbaobaoEvaluateBanner banner) {
		this.banner = banner;
	}
	public TaobaoGift getGift() {
		return gift;
	}
	public void setGift(TaobaoGift gift) {
		this.gift = gift;
	}
	
}
