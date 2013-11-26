package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

/**
 * 淘宝礼品对象
 * @ClassName:   TaobaoGift 
 * @Description: 淘宝礼品对象(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-14 下午8:04:47 
 *
 */
public class TaobaoGift implements Serializable{
	private static final long serialVersionUID = 112342211112L;
	private String award;
	private String countDown;
	private String dateEnd;
	private String dateStart;
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getCountDown() {
		return countDown;
	}
	public void setCountDown(String countDown) {
		this.countDown = countDown;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	
}
