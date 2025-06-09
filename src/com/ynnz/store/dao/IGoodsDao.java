package com.ynnz.store.dao;

import java.util.List;

import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.GoodsType;

public interface IGoodsDao {

	/**
	 * 根据条码查出货品
	 *
	 * @param barCode
	 * @return
	 */
	public Goods getGoodsByBarCode(String barCode);

	/**
	 * 货品入库
	 *
	 * @param goods
	 * @return
	 */
	public boolean addGoods(Goods goods);

	/**
	 * 更新货品信息
	 *
	 * @param goods
	 * @return
	 */
	public boolean updateGoods(Goods goods);

	/**
	 * 更新货品库存数量
	 *
	 * @param goodsId 货品ID
	 * @param num     货品库存
	 * @return
	 */
	public boolean updateStoreNum(int goodsId, int num);

	/**
	 * 商品返回库存
	 * @param goodsId
	 * @param num
	 * @return
	 */
	public boolean returnStoreNum(int goodsId, int num);

	/**
	 * 浏览商品信息
	 * @param barcode
	 * @param goodsName
	 * @param startDate
	 * @param endDate
	 * @param goodsType
	 * @return
	 */
	public List<Goods> getGoodsListView(String barcode, String goodsName, String startDate, String endDate,
										GoodsType goodsType);

	/**
	 * 查某分类下商品数目
	 *
	 * @param typeId
	 * @return
	 */
	public int getGoodsCountByType(int typeId);

}
