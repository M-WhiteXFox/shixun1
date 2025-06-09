package com.ynnz.store.pojo;

/**
 * 字典数据
 * @author 吕琼华
 *
 */
public class Dictionary {

	private int id;//主键ID
	private String typeName;//字典名称
	private String typeValue;//字典值
	private int dicType;//字典中的类型
	private int dicOrder;//字典类型下的排列顺序
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public int getDicType() {
		return dicType;
	}
	public void setDicType(int dicType) {
		this.dicType = dicType;
	}
	public int getDicOrder() {
		return dicOrder;
	}
	public void setDicOrder(int dicOrder) {
		this.dicOrder = dicOrder;
	}


}
