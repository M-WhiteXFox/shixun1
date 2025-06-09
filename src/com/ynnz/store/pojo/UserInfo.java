package com.ynnz.store.pojo;

import java.math.BigDecimal;

public class UserInfo {

	private int saleManId;// 员工主键ID
	private String saleManName;// 员工名称
	private String pwd;//员工密码
	private String mobile;// 员工手机号码
	private String gender;// 性别
	private float basesalary;// 基本工资
	private BigDecimal commissionRate;// 提成比例
	private String role;// 角色名称：店长，导购员，收银员
	private float saleMoney;// 销售额
	private String saleMonth;//销售月yyyy-MM

	public UserInfo() {
		super();
	}

	public UserInfo(String saleManName) {
		super();
		this.saleManName = saleManName;
	}

	public int getSaleManId() {
		return saleManId;
	}

	public void setSaleManId(int saleManId) {
		this.saleManId = saleManId;
	}

	public String getSaleManName() {
		return saleManName;
	}

	public void setSaleManName(String saleManName) {
		this.saleManName = saleManName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getBasesalary() {
		return basesalary;
	}

	public void setBasesalary(float basesalary) {
		this.basesalary = basesalary;
	}

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public float getSaleMoney() {
		return saleMoney;
	}

	public void setSaleMoney(float saleMoney) {
		this.saleMoney = saleMoney;
	}

	public String getSaleMonth() {
		return saleMonth;
	}

	public void setSaleMonth(String saleMonth) {
		this.saleMonth = saleMonth;
	}

	@Override
	public String toString() {
		return this.saleManName;
	}


}
