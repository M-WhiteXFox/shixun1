package com.ynnz.store.pojo;

import java.util.Date;

public class StorageRecord {
    private int recordId; // 入库单ID
    private String recordCode; // 入库单号
    private int goodsId; // 商品ID
    private String goodsName; // 商品名称
    private int quantity; // 入库数量
    private float storePrice; // 进货价格
    private float totalAmount; // 总金额
    private Date storageDate; // 入库时间
    private int operatorId; // 操作员ID
    private String operatorName; // 操作员姓名

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(float storePrice) {
        this.storePrice = storePrice;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}