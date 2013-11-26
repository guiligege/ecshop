package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;
import java.util.List;

public class TaobaoSingleCreditInfo implements Serializable{
	private static final long serialVersionUID = 112342211115L;
	private TaobaoPageInfo paginator;
	private String  watershed;
	private List<TaobaoEvaluate> rateList;
	private String showMore;
	public TaobaoPageInfo getPaginator() {
		return paginator;
	}
	public void setPaginator(TaobaoPageInfo paginator) {
		this.paginator = paginator;
	}
	public String getWatershed() {
		return watershed;
	}
	public void setWatershed(String watershed) {
		this.watershed = watershed;
	}
	public List<TaobaoEvaluate> getRateList() {
		return rateList;
	}
	public void setRateList(List<TaobaoEvaluate> rateList) {
		this.rateList = rateList;
	}
	public String getShowMore() {
		return showMore;
	}
	public void setShowMore(String showMore) {
		this.showMore = showMore;
	}
	
}
