package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;
import java.util.List;

public class TaobaoTotalEvaluate implements Serializable{
	private static final long serialVersionUID = 112342211118L;
	private String correspond;
	private String correspondCount;
	private List<String> correspondList;
	private TaobaoEvaluateCount count;
	private List<TaobaoImpress> impress;
	private String links;
	private String refundTime;
	private List<String> spuRatting;
	public String getCorrespond() {
		return correspond;
	}
	public void setCorrespond(String correspond) {
		this.correspond = correspond;
	}
	public String getCorrespondCount() {
		return correspondCount;
	}
	public void setCorrespondCount(String correspondCount) {
		this.correspondCount = correspondCount;
	}
	public TaobaoEvaluateCount getCount() {
		return count;
	}
	public void setCount(TaobaoEvaluateCount count) {
		this.count = count;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public List<String> getCorrespondList() {
		return correspondList;
	}
	public void setCorrespondList(List<String> correspondList) {
		this.correspondList = correspondList;
	}
	public List<String> getSpuRatting() {
		return spuRatting;
	}
	public void setSpuRatting(List<String> spuRatting) {
		this.spuRatting = spuRatting;
	}
	public List<TaobaoImpress> getImpress() {
		return impress;
	}
	public void setImpress(List<TaobaoImpress> impress) {
		this.impress = impress;
	}
	
	
}
