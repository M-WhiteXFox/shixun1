package com.ynnz.store.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Goods {

	private int goodsId;// 主键，商品ID
	private String barCode;// 货号/条形编码
	private GoodsType goodsType;//货物所属类别
	private String goodsName;// 货物名称
	private float storePrice;// 进货价格
	private float salePrice;// 销售价格
	private float discount;// 折扣
	private int stockNum;// 库存数量
	private Date stockDate;// 入库时间
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public GoodsType getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public float getStorePrice() {
		return storePrice;
	}
	public void setStorePrice(float storePrice) {
		this.storePrice = storePrice;
	}
	public float getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(float salePrice) {
		this.salePrice = salePrice;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public int getStockNum() {
		return stockNum;
	}
	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}
	public Date getStockDate() {
		return stockDate;
	}
	public void setStockDate(Date stockDate) {
		this.stockDate = stockDate;
	}

}
