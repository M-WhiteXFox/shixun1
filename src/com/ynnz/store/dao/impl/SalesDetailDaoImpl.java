package com.ynnz.store.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.ynnz.store.dao.ISalesDetailDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.Sales;
import com.ynnz.store.pojo.SalesDetail;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.StringUtils;

public class SalesDetailDaoImpl implements ISalesDetailDao {

	@Override
	public int addSalesDetails(Sales sale, List<SalesDetail> saleDetails) {
		int num = 0;
		String addSaleSql = "INSERT INTO t_sales(ReceiptsCode,SalesDate,Amount,SalesmanID,CashierID)\r\n"
				+ "VALUES(?,now(),?,?,?);";
		List<Object> values = new ArrayList<Object>();
		values.add(sale.getReceiptsCode());
		values.add(sale.getAmount());
		values.add(sale.getSalesMan().getSaleManId());
		values.add(sale.getCrasher().getSaleManId());
		int saleId = ExecuteCommon.updateQueryDatas(addSaleSql, values);
		String addSaleDetailSql = "INSERT INTO t_salesdetail(SalesID,GoodsID,Quantity,AloneAmount)\r\n" + "VALUES";
		if (saleDetails != null && saleDetails.size() > 0) {
			Object[] row = new Object[4];
			List<Object> dataPra = new ArrayList<Object>();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < saleDetails.size(); i++) {
				SalesDetail detail = saleDetails.get(i);
				if ((i + 1) == saleDetails.size()) {
					sb.append("(?,?,?,?);");
				} else {
					sb.append("(?,?,?,?),");
				}
				row = new Object[4];
				row[0] = saleId;
				row[1] = detail.getGoods().getGoodsId();
				row[2] = detail.getSaleNum();
				row[3] = detail.getAloneAmount();
				dataPra.add(row);
			}
			num = ExecuteCommon.updateBatchDatas(addSaleDetailSql + sb.toString(), dataPra);
		}
		return num;
	}

	@Override
	public List<SalesDetail> getSaleGoodsByCode(String receiptCode) {
		String sql = "SELECT SD.SalesID,G.GoodsID, G.BarCode,G.GoodsName,SD.AloneAmount,SD.Quantity,"
				+ "U.SalesmanID,U.SalesmanName,S.SalesDate,S.Amount FROM t_salesdetail SD \r\n"
				+ "INNER JOIN t_goods G ON SD.GoodsID=G.GoodsID\r\n"
				+ "INNER JOIN t_sales S ON SD.SalesID=S.SalesID\r\n"
				+ "LEFT JOIN t_userinfo U ON S.SalesmanID=U.SalesmanID\r\n";
		List<Object> values = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(receiptCode)) {
			sql += "WHERE S.ReceiptsCode = ? \r\n";
			values.add(receiptCode);
		}
		sql += "ORDER BY S.SalesDate DESC ";
		SalesDetail saleDetail;
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		List<SalesDetail> list = new ArrayList<SalesDetail>();
		if (ret != null && ret.size() > 0) {
			for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				saleDetail = new SalesDetail();
				Goods goods = new Goods();
				Sales sale = new Sales();
				UserInfo u = new UserInfo();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("SalesID".equals(entry.getKey())) {
						sale.setSalesId((int) entry.getValue());
					}
					if ("GoodsID".equals(entry.getKey())) {
						goods.setGoodsId((int) entry.getValue());
					}
					if ("BarCode".equals(entry.getKey())) {
						goods.setBarCode((String) entry.getValue());
					}
					if ("GoodsName".equals(entry.getKey())) {
						goods.setGoodsName((String) entry.getValue());
					}
					if ("AloneAmount".equals(entry.getKey())) {
						saleDetail.setAloneAmount(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("Quantity".equals(entry.getKey())) {
						saleDetail.setSaleNum((int) entry.getValue());
					}
					if ("SalesmanName".equals(entry.getKey())) {
						u.setSaleManName(((String) entry.getValue()));
					}
					if ("SalesDate".equals(entry.getKey())) {
						Object value = entry.getValue();
						if (value instanceof java.util.Date) {
							sale.setSalesDate((Date) value);
						} else if (value instanceof java.time.LocalDateTime) {
							LocalDateTime ldt = (LocalDateTime) value;
							sale.setSalesDate(java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
						}
					}
					if ("Amount".equals(entry.getKey())) {
						sale.setAmount(((BigDecimal) entry.getValue()).floatValue());
					}
				}
				sale.setSalesMan(u);
				saleDetail.setSales(sale);
				saleDetail.setGoods(goods);
				list.add(saleDetail);
			}
		}
		return list;
	}

	@Override
	public boolean deleteSaleDetail(int sdid) {
		String sql = "DELETE FROM t_salesdetail  WHERE SDID=? ";
		List<Object> values = new ArrayList<Object>();
		values.add(sdid);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num >= 0 ? true : false;
	}

	@Override
	public boolean deleteSaleDetail(int saleId, int goodsId) {
		String sql = "DELETE FROM t_salesdetail  WHERE SalesID=? AND GoodsID=? ";
		List<Object> values = new ArrayList<Object>();
		values.add(saleId);
		values.add(goodsId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num >= 0 ? true : false;
	}

	@Override
	public boolean deleteSale(int saleId) {
		String sql = "DELETE FROM t_sales  WHERE SalesID=? ";
		List<Object> values = new ArrayList<Object>();
		values.add(saleId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num >= 0 ? true : false;
	}

	@Override
	public List<Sales> getSalesStatistic(int saleManId, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer(
				"SELECT T1.ReceiptsCode,T1.SalesDate,T1.Amount,T1.profit,U1.SalesmanName ,U2.SalesmanName AS crasher  FROM(\r\n"
						+ "SELECT S.ReceiptsCode,"
						+ "MAX(S.SalesDate) AS SalesDate,"
						+ "MAX(S.Amount) AS Amount,"
						+ "MAX(S.Amount)-SUM(G.StorePrice*SD.Quantity) AS profit,"
						+ "MAX(S.SalesmanID) AS SalesmanID,"
						+ "MAX(S.CashierID) AS CashierID\r\n"
						+ " FROM t_goods G \r\n" + "INNER JOIN t_salesdetail SD ON G.GoodsID=SD.GoodsID\r\n"
						+ "INNER JOIN t_sales S ON SD.SalesID=S.SalesID\r\n" + "GROUP BY S.ReceiptsCode\r\n"
						+ "HAVING 1=1  \r\n");
		List<Object> values = new ArrayList<Object>();
		if (saleManId != 0) {
			sql.append("AND MAX(S.SalesmanID)=?  ");
			values.add(saleManId);
		}
		if (StringUtils.isNotEmpty(startDate)) {
			sql.append("AND MAX(S.SalesDate) >=?  ");
			values.add(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql.append("AND MAX(S.SalesDate) <=?  ");
			values.add(endDate);
		}
		sql.append(") T1\r\n" + "LEFT JOIN t_userinfo U1 ON T1.SalesmanID=U1.SalesmanID\r\n"
				+ "LEFT JOIN t_userinfo U2 ON T1.CashierID=U2.SalesmanID\r\n" + "ORDER BY T1.SalesDate DESC");
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql.toString(), values);
		List<Sales> list = new ArrayList<Sales>();
		if (ret != null && ret.size() > 0) {
			for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				Sales sale = new Sales();
				UserInfo saleMan = new UserInfo();
				UserInfo crasher = new UserInfo();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("ReceiptsCode".equals(entry.getKey())) {
						sale.setReceiptsCode((String) entry.getValue());
					}
					if ("SalesDate".equals(entry.getKey())) {
						Object value = entry.getValue();
						if (value instanceof java.util.Date) {
							sale.setSalesDate((Date) value);
						} else if (value instanceof java.time.LocalDateTime) {
							LocalDateTime ldt = (LocalDateTime) value;
							sale.setSalesDate(java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
						}
					}
					if ("Amount".equals(entry.getKey())) {
						sale.setAmount(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("profit".equals(entry.getKey())) {
						sale.setProfit(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("SalesmanName".equals(entry.getKey())) {
						saleMan.setSaleManName(((String) entry.getValue()));
					}
					if ("crasher".equals(entry.getKey())) {
						crasher.setSaleManName(((String) entry.getValue()));
					}

				}
				sale.setSalesMan(saleMan);
				sale.setCrasher(crasher);
				list.add(sale);
			}
		}
		return list;
	}

}
