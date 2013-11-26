package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

/**
 * 不同评论次数的人数
 * @author guili
 *
 */
public class EvaluateTime implements Serializable{
	/** 
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */  
	private static final long serialVersionUID = 112342211110L;
	private int TwicePeople;
	private int ThreestimesPeople;
	private int MoreThreestimesPeople;
	public int getTwicePeople() {
		return TwicePeople;
	}
	public void setTwicePeople(int twicePeople) {
		TwicePeople = twicePeople;
	}
	public int getThreestimesPeople() {
		return ThreestimesPeople;
	}
	public void setThreestimesPeople(int threestimesPeople) {
		ThreestimesPeople = threestimesPeople;
	}
	public int getMoreThreestimesPeople() {
		return MoreThreestimesPeople;
	}
	public void setMoreThreestimesPeople(int moreThreestimesPeople) {
		MoreThreestimesPeople = moreThreestimesPeople;
	}
	
}
