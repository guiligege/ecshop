package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

public class TaobaoImpress implements Serializable{
	private static final long serialVersionUID = 112342211113L;
	private String attribute;
	private String count;
	private String title;
	private String value;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
