package org.guili.ecshop.bean.credit.tmall;

import java.io.Serializable;

/**
 * 用于记录tmall商品评价的对象
 * @ClassName:   TmallAnalyzeBean 
 * @Description: 用于记录tmall商品评价的对象(这里用一句话描述这个类的作用) 
 * @author:      guilige 
 * @date         2013-11-26 下午7:25:50 
 *
 */
public class TmallAnalyzeBean  implements Serializable{
	private static final long serialVersionUID = 112342211121L;
	private Long id;
	private double correspond;				 //商家以前的信用评分
	private double badWeightALL;		 //整体中差评权重
	private int total;					 //用户评价总数
	private int badAndNormalWeightSingle;//单个商品中差评权重
	private int badWeightSingle;		 //单个商品中差评权重
	private int twicePerson;			 //2次评论的人数
	private int threestimesPerson;		 //3次评论的人数
	private int moreThreestimesPerson;	 //4次及其以上的人数
	private String brandname;			 //品牌名称
	private String brandurl;			 //品牌url
	private String producturl;			 //产品url
	
	public double getBadWeightALL() {
		return badWeightALL;
	}
	public void setBadWeightALL(double badWeightALL) {
		this.badWeightALL = badWeightALL;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBadAndNormalWeightSingle() {
		return badAndNormalWeightSingle;
	}
	public void setBadAndNormalWeightSingle(int badAndNormalWeightSingle) {
		this.badAndNormalWeightSingle = badAndNormalWeightSingle;
	}
	public int getBadWeightSingle() {
		return badWeightSingle;
	}
	public void setBadWeightSingle(int badWeightSingle) {
		this.badWeightSingle = badWeightSingle;
	}
	public int getTwicePerson() {
		return twicePerson;
	}
	public void setTwicePerson(int twicePerson) {
		this.twicePerson = twicePerson;
	}
	public int getThreestimesPerson() {
		return threestimesPerson;
	}
	public void setThreestimesPerson(int threestimesPerson) {
		this.threestimesPerson = threestimesPerson;
	}
	public int getMoreThreestimesPerson() {
		return moreThreestimesPerson;
	}
	public void setMoreThreestimesPerson(int moreThreestimesPerson) {
		this.moreThreestimesPerson = moreThreestimesPerson;
	}
	public double getCorrespond() {
		return correspond;
	}
	public void setCorrespond(double correspond) {
		this.correspond = correspond;
	}
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public String getBrandurl() {
		return brandurl;
	}
	public void setBrandurl(String brandurl) {
		this.brandurl = brandurl;
	}
	public String getProducturl() {
		return producturl;
	}
	public void setProducturl(String producturl) {
		this.producturl = producturl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
