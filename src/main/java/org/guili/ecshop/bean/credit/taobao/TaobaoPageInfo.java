package org.guili.ecshop.bean.credit.taobao;

import java.io.Serializable;

public class TaobaoPageInfo implements Serializable{
	private static final long serialVersionUID = 112342211114L;
	private String lastPage;
	private String page;
	private String items;
	private String pages;
	private String beginIndex;
	private String length;
	private String firstPage;
	private String endIndex;
	private String offset;
	private String itemsPerPage;
	public String getLastPage() {
		return lastPage;
	}
	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public String getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(String beginIndex) {
		this.beginIndex = beginIndex;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(String firstPage) {
		this.firstPage = firstPage;
	}
	public String getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getItemsPerPage() {
		return itemsPerPage;
	}
	public void setItemsPerPage(String itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	
}
