package org.guili.ecshop.bean;

/**
 * 抓取数据信息
 * @author guili
 */
public class Semiconductor {
	private String guige;		//规格
	private String imagepath;   //图片路径
	private String producterkey;//不同厂商的编号
	private String code;		//零件编号
	private String producter;	//生产商
	private String desc;		//描述
	private String discount;	//现有库存（未来是int）
//	private String producterdiscount;//厂商库存
	private String price;		//单价 (USD）美元(以后是double)
	private String lowestcount;	//最低订单量（未来是int）
	private String function;	//功能描述
	public String getGuige() {
		return guige;
	}
	public void setGuige(String guige) {
		this.guige = guige;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public String getProducterkey() {
		return producterkey;
	}
	public void setProducterkey(String producterkey) {
		this.producterkey = producterkey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProducter() {
		return producter;
	}
	public void setProducter(String producter) {
		this.producter = producter;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLowestcount() {
		return lowestcount;
	}
	public void setLowestcount(String lowestcount) {
		this.lowestcount = lowestcount;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
}
