package com.ynnz.store.dao;

import java.util.List;

import com.ynnz.store.pojo.Sales;
import com.ynnz.store.pojo.SalesDetail;

public interface ISalesDetailDao {

	/**
	 * 插入商品销售记录
	 * @param sale 流水单信息
	 * @param saleDetails 流水单里商品信息
	 * @return
	 */
	public int addSalesDetails(Sales sale,List<SalesDetail> saleDetails);

	/**
	 * 根据小票流水号查小票里的商品信息
	 * @param barcode
	 * @return
	 */
	public List<SalesDetail> getSaleGoodsByCode(String receiptCode);

	/**
	 * 根据主键删除收银单里某个商品
	 * @param sdid
	 * @return
	 */
	public boolean deleteSaleDetail(int sdid);

	/**
	 * 删除某单子里的某个商品
	 * @param saleId
	 * @param goodsId
	 * @return
	 */
	public boolean deleteSaleDetail(int saleId,int goodsId);

	/**
	 * 删除收银单信息
	 * @param saleId
	 * @return
	 */
	public boolean deleteSale(int saleId);

	/**
	 * 根据导购员和日期段统计销售利润信息
	 * @param saleManId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Sales> getSalesStatistic(int saleManId,String startDate,String endDate);

}
