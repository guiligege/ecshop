package org.guili.ecshop.bean.spider;

import java.util.Date;

/**
 * 高信誉店铺信息
 * @ClassName:   TopStore 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-12-11 上午11:58:29 
 *
 */
public class TopStore {
	private int id;
	private String storetag;
	private String storeinfo;
	private String xingyong;
	private Date   createtime;
	
	public String getStoretag() {
		return storetag;
	}
	public void setStoretag(String storetag) {
		this.storetag = storetag;
	}
	public String getStoreinfo() {
		return storeinfo;
	}
	public void setStoreinfo(String storeinfo) {
		this.storeinfo = storeinfo;
	}
	public String getXingyong() {
		return xingyong;
	}
	public void setXingyong(String xingyong) {
		this.xingyong = xingyong;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
