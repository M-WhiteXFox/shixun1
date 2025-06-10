package com.ynnz.store.pojo;

import java.util.Date;

public class GoodsStorage {
    private String storageNo; // 入库单号
    private String goodsName; // 商品名称
    private Integer storageNum; // 入库数量
    private Date storageTime; // 入库时间
    private String operator; // 操作员

    public String getStorageNo() {
        return storageNo;
    }

    public void setStorageNo(String storageNo) {
        this.storageNo = storageNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}