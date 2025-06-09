package com.ynnz.store.pojo;

public class GoodsType {

	private int typeId;//主键，类别id
	private String typeName;//类别名称
	private GoodsType parentType;

	public GoodsType() {
		super();
	}
	public GoodsType(String typeName) {
		super();
		this.typeName = typeName;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public GoodsType getParentType() {
		return parentType;
	}
	public void setParentType(GoodsType parentType) {
		this.parentType = parentType;
	}
	@Override
	public String toString() {
		return this.typeName;
	}

}
