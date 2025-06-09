package com.ynnz.store.pojo;

import java.util.Date;

public class Sales {

	private int salesId;// 主键ID
	private String receiptsCode;// 小票流水号
	private Date salesDate;// 销售日期
	private float amount;// 总的销售价格
	private UserInfo salesMan;// 导购员
	private UserInfo crasher;// 收银员
	private float profit;//单子的利润

	public int getSalesId() {
		return salesId;
	}

	public void setSalesId(int salesId) {
		this.salesId = salesId;
	}

	public String getReceiptsCode() {
		return receiptsCode;
	}

	public void setReceiptsCode(String receiptsCode) {
		this.receiptsCode = receiptsCode;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public UserInfo getSalesMan() {
		return salesMan;
	}

	public void setSalesMan(UserInfo salesMan) {
		this.salesMan = salesMan;
	}

	public UserInfo getCrasher() {
		return crasher;
	}

	public void setCrasher(UserInfo crasher) {
		this.crasher = crasher;
	}

	public float getProfit() {
		return profit;
	}

	public void setProfit(float profit) {
		this.profit = profit;
	}

}
