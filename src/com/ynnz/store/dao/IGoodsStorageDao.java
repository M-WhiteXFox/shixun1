package com.ynnz.store.dao;

import com.ynnz.store.pojo.GoodsStorage;
import java.util.List;

public interface IGoodsStorageDao {
    /**
     * 获取所有入库记录
     */
    List<GoodsStorage> getAllGoodsStorage();

    /**
     * 搜索入库记录
     * 
     * @param searchType 搜索类型（入库单号/商品名称）
     * @param searchText 搜索内容
     */
    List<GoodsStorage> searchGoodsStorage(String searchType, String searchText);

    /**
     * 根据入库单号获取入库记录
     */
    GoodsStorage getGoodsStorageByNo(String storageNo);

    /**
     * 添加入库记录
     */
    boolean addGoodsStorage(GoodsStorage storage);

    /**
     * 更新入库记录
     */
    boolean updateGoodsStorage(GoodsStorage storage);

    /**
     * 删除入库记录
     */
    boolean deleteGoodsStorage(String storageNo);
}