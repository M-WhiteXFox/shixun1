package com.ynnz.store.dao.impl;

import com.ynnz.store.dao.IGoodsStorageDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.GoodsStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class GoodsStorageDaoImpl implements IGoodsStorageDao {
    @Override
    public List<GoodsStorage> getAllGoodsStorage() {
        List<GoodsStorage> list = new ArrayList<>();
        String sql = "SELECT * FROM t_storage_record ORDER BY StorageDate DESC";

        List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, null);
        if (ret != null && ret.size() > 0) {
            for (Map<String, Object> map : ret) {
                GoodsStorage storage = new GoodsStorage();
                for (Entry<String, Object> entry : map.entrySet()) {
                    if ("RecordCode".equals(entry.getKey())) {
                        storage.setStorageNo((String) entry.getValue());
                    }
                    if ("GoodsName".equals(entry.getKey())) {
                        storage.setGoodsName((String) entry.getValue());
                    }
                    if ("Quantity".equals(entry.getKey())) {
                        storage.setStorageNum((Integer) entry.getValue());
                    }
                    if ("StorageDate".equals(entry.getKey())) {
                        Object value = entry.getValue();
                        if (value instanceof Date) {
                            storage.setStorageTime((Date) value);
                        } else if (value instanceof LocalDateTime) {
                            LocalDateTime ldt = (LocalDateTime) value;
                            storage.setStorageTime(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
                        }
                    }
                    if ("OperatorName".equals(entry.getKey())) {
                        storage.setOperator((String) entry.getValue());
                    }
                }
                list.add(storage);
            }
        }

        return list;
    }

    @Override
    public List<GoodsStorage> searchGoodsStorage(String searchType, String searchText) {
        List<GoodsStorage> list = new ArrayList<>();
        String sql = "SELECT * FROM t_storage_record WHERE ";

        if ("入库单号".equals(searchType)) {
            sql += "RecordCode LIKE ?";
        } else {
            sql += "GoodsName LIKE ?";
        }
        sql += " ORDER BY StorageDate DESC";

        List<Object> params = new ArrayList<>();
        params.add("%" + searchText + "%");

        List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, params);
        if (ret != null && ret.size() > 0) {
            for (Map<String, Object> map : ret) {
                GoodsStorage storage = new GoodsStorage();
                for (Entry<String, Object> entry : map.entrySet()) {
                    if ("RecordCode".equals(entry.getKey())) {
                        storage.setStorageNo((String) entry.getValue());
                    }
                    if ("GoodsName".equals(entry.getKey())) {
                        storage.setGoodsName((String) entry.getValue());
                    }
                    if ("Quantity".equals(entry.getKey())) {
                        storage.setStorageNum((Integer) entry.getValue());
                    }
                    if ("StorageDate".equals(entry.getKey())) {
                        Object value = entry.getValue();
                        if (value instanceof Date) {
                            storage.setStorageTime((Date) value);
                        } else if (value instanceof LocalDateTime) {
                            LocalDateTime ldt = (LocalDateTime) value;
                            storage.setStorageTime(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
                        }
                    }
                    if ("OperatorName".equals(entry.getKey())) {
                        storage.setOperator((String) entry.getValue());
                    }
                }
                list.add(storage);
            }
        }

        return list;
    }

    @Override
    public GoodsStorage getGoodsStorageByNo(String storageNo) {
        String sql = "SELECT * FROM t_storage_record WHERE RecordCode = ?";

        List<Object> params = new ArrayList<>();
        params.add(storageNo);

        List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, params);
        if (ret != null && ret.size() > 0) {
            Map<String, Object> map = ret.get(0);
            GoodsStorage storage = new GoodsStorage();
            for (Entry<String, Object> entry : map.entrySet()) {
                if ("RecordCode".equals(entry.getKey())) {
                    storage.setStorageNo((String) entry.getValue());
                }
                if ("GoodsName".equals(entry.getKey())) {
                    storage.setGoodsName((String) entry.getValue());
                }
                if ("Quantity".equals(entry.getKey())) {
                    storage.setStorageNum((Integer) entry.getValue());
                }
                if ("StorageDate".equals(entry.getKey())) {
                    Object value = entry.getValue();
                    if (value instanceof Date) {
                        storage.setStorageTime((Date) value);
                    } else if (value instanceof LocalDateTime) {
                        LocalDateTime ldt = (LocalDateTime) value;
                        storage.setStorageTime(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
                    }
                }
                if ("OperatorName".equals(entry.getKey())) {
                    storage.setOperator((String) entry.getValue());
                }
            }
            return storage;
        }

        return null;
    }

    @Override
    public boolean addGoodsStorage(GoodsStorage storage) {
        // 检查入库单号是否已存在
        if (getGoodsStorageByNo(storage.getStorageNo()) != null) {
            return false;
        }

        String sql = "INSERT INTO t_storage_record (RecordCode, GoodsName, Quantity, StorageDate, OperatorName) VALUES (?, ?, ?, ?, ?)";

        List<Object> params = new ArrayList<>();
        params.add(storage.getStorageNo());
        params.add(storage.getGoodsName());
        params.add(storage.getStorageNum());
        params.add(storage.getStorageTime());
        params.add(storage.getOperator());

        int result = ExecuteCommon.updateDatas(sql, params);
        if (result > 0) {
            // 更新商品库存
            String updateStockSql = "UPDATE t_goods SET StockNum = StockNum + ? WHERE GoodsName = ?";
            List<Object> stockParams = new ArrayList<>();
            stockParams.add(storage.getStorageNum());
            stockParams.add(storage.getGoodsName());

            result = ExecuteCommon.updateDatas(updateStockSql, stockParams);
            return result > 0;
        }

        return false;
    }

    @Override
    public boolean updateGoodsStorage(GoodsStorage storage) {
        // 获取原记录
        GoodsStorage oldStorage = getGoodsStorageByNo(storage.getStorageNo());
        if (oldStorage == null) {
            return false;
        }

        String sql = "UPDATE t_storage_record SET GoodsName = ?, Quantity = ?, OperatorName = ? WHERE RecordCode = ?";

        List<Object> params = new ArrayList<>();
        params.add(storage.getGoodsName());
        params.add(storage.getStorageNum());
        params.add(storage.getOperator());
        params.add(storage.getStorageNo());

        int result = ExecuteCommon.updateDatas(sql, params);
        if (result > 0) {
            // 更新商品库存
            String updateStockSql = "UPDATE t_goods SET StockNum = StockNum - ? + ? WHERE GoodsName = ?";
            List<Object> stockParams = new ArrayList<>();
            stockParams.add(oldStorage.getStorageNum());
            stockParams.add(storage.getStorageNum());
            stockParams.add(storage.getGoodsName());

            result = ExecuteCommon.updateDatas(updateStockSql, stockParams);
            return result > 0;
        }

        return false;
    }

    @Override
    public boolean deleteGoodsStorage(String storageNo) {
        // 获取原记录
        GoodsStorage storage = getGoodsStorageByNo(storageNo);
        if (storage == null) {
            return false;
        }

        String sql = "DELETE FROM t_storage_record WHERE RecordCode = ?";

        List<Object> params = new ArrayList<>();
        params.add(storageNo);

        int result = ExecuteCommon.updateDatas(sql, params);
        if (result > 0) {
            // 更新商品库存
            String updateStockSql = "UPDATE t_goods SET StockNum = StockNum - ? WHERE GoodsName = ?";
            List<Object> stockParams = new ArrayList<>();
            stockParams.add(storage.getStorageNum());
            stockParams.add(storage.getGoodsName());

            result = ExecuteCommon.updateDatas(updateStockSql, stockParams);
            return result > 0;
        }

        return false;
    }
}