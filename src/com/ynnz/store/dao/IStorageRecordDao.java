package com.ynnz.store.dao;

import java.util.List;
import com.ynnz.store.pojo.StorageRecord;

public interface IStorageRecordDao {
    /**
     * 添加入库单记录
     * 
     * @param record 入库单信息
     * @return 是否添加成功
     */
    public boolean addStorageRecord(StorageRecord record);

    /**
     * 根据入库单号查询入库单
     * 
     * @param recordCode 入库单号
     * @return 入库单信息
     */
    public StorageRecord getStorageRecordByCode(String recordCode);

    /**
     * 查询入库单列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param goodsName 商品名称
     * @return 入库单列表
     */
    public List<StorageRecord> getStorageRecordList(String startDate, String endDate, String goodsName);
}