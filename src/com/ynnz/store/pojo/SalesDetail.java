package com.ynnz.store.pojo;

public class SalesDetail {

	private int sdId;// 主键ID
	private Sales sales;// 收银单
	private Goods goods = new Goods();// 销售的货品
	private int saleNum;// 销售数量
	private float aloneAmount;// 成交价格

	public int getSdId() {
		return sdId;
	}

	public void setSdId(int sdId) {
		this.sdId = sdId;
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public float getAloneAmount() {
		return aloneAmount;
	}

	public void setAloneAmount(float aloneAmount) {
		this.aloneAmount = aloneAmount;
	}

}
