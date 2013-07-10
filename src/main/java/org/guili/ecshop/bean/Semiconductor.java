package org.guili.ecshop.bean;

/**
 * 半导体
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
	private String producterdiscount;//厂商库存
	private String price;		//单价 (USD）美元(以后是double)
	private String lowestcount;	//最低订单量（未来是int）
	private String baozhuang;	//包装
	private String xilie;		//系列
	private String rate;		//数据速率
	private String dianya;		//电源电压
	private String dianliu;		//25° C 时的待机电流
	private String lianlu;		//链路范围，低功耗
	private String direction;	//方向
	private String wendu;		//工作温度
	private String size;		//尺寸
	private String standard;	//标准
	private String shutdown;	//关断
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
	public String getProducterdiscount() {
		return producterdiscount;
	}
	public void setProducterdiscount(String producterdiscount) {
		this.producterdiscount = producterdiscount;
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
	public String getBaozhuang() {
		return baozhuang;
	}
	public void setBaozhuang(String baozhuang) {
		this.baozhuang = baozhuang;
	}
	public String getXilie() {
		return xilie;
	}
	public void setXilie(String xilie) {
		this.xilie = xilie;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDianya() {
		return dianya;
	}
	public void setDianya(String dianya) {
		this.dianya = dianya;
	}
	public String getDianliu() {
		return dianliu;
	}
	public void setDianliu(String dianliu) {
		this.dianliu = dianliu;
	}
	public String getLianlu() {
		return lianlu;
	}
	public void setLianlu(String lianlu) {
		this.lianlu = lianlu;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getWendu() {
		return wendu;
	}
	public void setWendu(String wendu) {
		this.wendu = wendu;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getShutdown() {
		return shutdown;
	}
	public void setShutdown(String shutdown) {
		this.shutdown = shutdown;
	}
}
