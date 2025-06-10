package com.ynnz.store.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ynnz.store.dao.IStorageRecordDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.StorageRecord;
import com.ynnz.store.util.StringUtils;

public class StorageRecordDaoImpl implements IStorageRecordDao {

    @Override
    public boolean addStorageRecord(StorageRecord record) {
        String sql = "INSERT INTO t_storage_record(RecordCode, GoodsID, GoodsName, Quantity, StorePrice, TotalAmount, StorageDate, OperatorID, OperatorName) "
                +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object> values = new ArrayList<Object>();
        values.add(record.getRecordCode());
        values.add(record.getGoodsId());
        values.add(record.getGoodsName());
        values.add(record.getQuantity());
        values.add(record.getStorePrice());
        values.add(record.getTotalAmount());
        values.add(record.getStorageDate());
        values.add(record.getOperatorId());
        values.add(record.getOperatorName());

        int num = ExecuteCommon.updateDatas(sql, values);
        return num > 0;
    }

    @Override
    public StorageRecord getStorageRecordByCode(String recordCode) {
        String sql = "SELECT * FROM t_storage_record WHERE RecordCode = ?";
        List<Object> values = new ArrayList<Object>();
        values.add(recordCode);

        List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
        if (ret != null && ret.size() > 0) {
            StorageRecord record = new StorageRecord();
            Map<String, Object> map = ret.get(0);
            for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
                Entry<String, Object> entry = ite.next();
                if ("RecordID".equals(entry.getKey())) {
                    record.setRecordId((int) entry.getValue());
                }
                if ("RecordCode".equals(entry.getKey())) {
                    record.setRecordCode((String) entry.getValue());
                }
                if ("GoodsID".equals(entry.getKey())) {
                    record.setGoodsId((int) entry.getValue());
                }
                if ("GoodsName".equals(entry.getKey())) {
                    record.setGoodsName((String) entry.getValue());
                }
                if ("Quantity".equals(entry.getKey())) {
                    record.setQuantity((int) entry.getValue());
                }
                if ("StorePrice".equals(entry.getKey())) {
                    record.setStorePrice(((Number) entry.getValue()).floatValue());
                }
                if ("TotalAmount".equals(entry.getKey())) {
                    record.setTotalAmount(((Number) entry.getValue()).floatValue());
                }
                if ("StorageDate".equals(entry.getKey())) {
                    record.setStorageDate((Date) entry.getValue());
                }
                if ("OperatorID".equals(entry.getKey())) {
                    record.setOperatorId((int) entry.getValue());
                }
                if ("OperatorName".equals(entry.getKey())) {
                    record.setOperatorName((String) entry.getValue());
                }
            }
            return record;
        }
        return null;
    }

    @Override
    public List<StorageRecord> getStorageRecordList(String startDate, String endDate, String goodsName) {
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM t_storage_record WHERE 1=1 ");
        List<Object> values = new ArrayList<Object>();

        if (StringUtils.isNotEmpty(startDate)) {
            sql.append("AND StorageDate >= ? ");
            values.add(startDate);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append("AND StorageDate <= ? ");
            values.add(endDate);
        }
        if (StringUtils.isNotEmpty(goodsName)) {
            sql.append("AND GoodsName LIKE ? ");
            values.add("%" + goodsName + "%");
        }

        sql.append("ORDER BY StorageDate DESC");

        List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql.toString(), values);
        List<StorageRecord> records = new ArrayList<StorageRecord>();

        if (ret != null && ret.size() > 0) {
            for (Map<String, Object> map : ret) {
                StorageRecord record = new StorageRecord();
                for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
                    Entry<String, Object> entry = ite.next();
                    if ("RecordID".equals(entry.getKey())) {
                        record.setRecordId((int) entry.getValue());
                    }
                    if ("RecordCode".equals(entry.getKey())) {
                        record.setRecordCode((String) entry.getValue());
                    }
                    if ("GoodsID".equals(entry.getKey())) {
                        record.setGoodsId((int) entry.getValue());
                    }
                    if ("GoodsName".equals(entry.getKey())) {
                        record.setGoodsName((String) entry.getValue());
                    }
                    if ("Quantity".equals(entry.getKey())) {
                        record.setQuantity((int) entry.getValue());
                    }
                    if ("StorePrice".equals(entry.getKey())) {
                        record.setStorePrice(((Number) entry.getValue()).floatValue());
                    }
                    if ("TotalAmount".equals(entry.getKey())) {
                        record.setTotalAmount(((Number) entry.getValue()).floatValue());
                    }
                    if ("StorageDate".equals(entry.getKey())) {
                        record.setStorageDate((Date) entry.getValue());
                    }
                    if ("OperatorID".equals(entry.getKey())) {
                        record.setOperatorId((int) entry.getValue());
                    }
                    if ("OperatorName".equals(entry.getKey())) {
                        record.setOperatorName((String) entry.getValue());
                    }
                }
                records.add(record);
            }
        }
        return records;
    }
}