package com.ynnz.store.dao;

import com.ynnz.store.pojo.StorageRecord;
import java.util.List;

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

    /**
     * 更新入库单记录
     * 
     * @param record 入库单信息
     * @return 是否更新成功
     */
    public boolean updateStorageRecord(StorageRecord record);

    /**
     * 删除入库单记录
     * 
     * @param recordCode 入库单号
     * @return 是否删除成功
     */
    public boolean deleteStorageRecord(String recordCode);
}