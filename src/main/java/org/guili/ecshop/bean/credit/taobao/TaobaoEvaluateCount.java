package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

public class TaobaoEvaluateCount implements Serializable{
	private static final long serialVersionUID = 112342211111L;
	private int additional;
	private int  bad;
	private int correspond;
	private int good;
	private int goodFull;
	private int hascontent;
	private int normal;
	private int pic;
	private int total;
	public int getAdditional() {
		return additional;
	}
	public void setAdditional(int additional) {
		this.additional = additional;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public int getCorrespond() {
		return correspond;
	}
	public void setCorrespond(int correspond) {
		this.correspond = correspond;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getGoodFull() {
		return goodFull;
	}
	public void setGoodFull(int goodFull) {
		this.goodFull = goodFull;
	}
	public int getHascontent() {
		return hascontent;
	}
	public void setHascontent(int hascontent) {
		this.hascontent = hascontent;
	}
	public int getNormal() {
		return normal;
	}
	public void setNormal(int normal) {
		this.normal = normal;
	}
	public int getPic() {
		return pic;
	}
	public void setPic(int pic) {
		this.pic = pic;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
