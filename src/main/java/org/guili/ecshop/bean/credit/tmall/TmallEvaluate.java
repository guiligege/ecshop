package org.guili.ecshop.bean.credit.tmall;

import java.io.Serializable;
import java.util.List;

/**
 * 天猫评论列表
 * @author guili
 *
 */
public class TmallEvaluate implements Serializable{
	private static final long serialVersionUID = 8415348176219665159L;
	private String allAppendCount;
	private String allBadCount;
	private String allNormalCount;
	private String feedGoodCount;
	private String index;
	private List<TmallSingleEvaluate> items;
	private String total;
	
	public String getAllAppendCount() {
		return allAppendCount;
	}
	public void setAllAppendCount(String allAppendCount) {
		this.allAppendCount = allAppendCount;
	}
	public String getAllBadCount() {
		return allBadCount;
	}
	public void setAllBadCount(String allBadCount) {
		this.allBadCount = allBadCount;
	}
	public String getAllNormalCount() {
		return allNormalCount;
	}
	public void setAllNormalCount(String allNormalCount) {
		this.allNormalCount = allNormalCount;
	}
	public String getFeedGoodCount() {
		return feedGoodCount;
	}
	public void setFeedGoodCount(String feedGoodCount) {
		this.feedGoodCount = feedGoodCount;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public List<TmallSingleEvaluate> getItems() {
		return items;
	}
	public void setItems(List<TmallSingleEvaluate> items) {
		this.items = items;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
