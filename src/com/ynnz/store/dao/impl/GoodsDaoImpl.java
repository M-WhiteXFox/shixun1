package com.ynnz.store.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ynnz.store.dao.IGoodsDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.Goods;
import com.ynnz.store.pojo.GoodsType;
import com.ynnz.store.util.StringUtils;

public class GoodsDaoImpl implements IGoodsDao {

	@Override
	public Goods getGoodsByBarCode(String barCode) {
		String sql = "SELECT g.GoodsID,g.BarCode,g.GoodsName,g.StorePrice,g.SalePrice,g.Discount,g.StockNum,g.StockDate,\r\n"
				+ "s.TypeID AS sTypeId,s.TypeName AS sTypeName,f.TypeID AS pTypeId,f.TypeName AS pTypeName\r\n"
				+ "FROM t_goods g\r\n" + "LEFT JOIN t_goodstype s  ON g.TypeID=s.TypeID\r\n"
				+ "LEFT JOIN t_goodstype f  ON s.ParentID=f.TypeID\r\n" + " WHERE g.BarCode=?";
		List<Object> values = new ArrayList<Object>();
		values.add(barCode);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		Goods goods = new Goods();
		GoodsType stype = new GoodsType();
		GoodsType ftype = new GoodsType();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Entry<String, Object>> ite = (ret.get(0)).entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) ite.next();
				if ("GoodsID".equals(entry.getKey())) {
					goods.setGoodsId((int) entry.getValue());
				}
				if ("BarCode".equals(entry.getKey())) {
					goods.setBarCode((String) entry.getValue());
				}
				if ("sTypeId".equals(entry.getKey())) {
					stype.setTypeId((int) entry.getValue());
				}
				if ("sTypeName".equals(entry.getKey())) {
					stype.setTypeName((String) entry.getValue());
				}
				if ("pTypeId".equals(entry.getKey())) {
					ftype.setTypeId((int) entry.getValue());
				}
				if ("pTypeName".equals(entry.getKey())) {
					ftype.setTypeName((String) entry.getValue());
				}
				if ("GoodsName".equals(entry.getKey())) {
					goods.setGoodsName((String) entry.getValue());
				}
				if ("StorePrice".equals(entry.getKey())) {
					goods.setStorePrice(((BigDecimal) entry.getValue()).floatValue());
				}
				if ("SalePrice".equals(entry.getKey())) {
					goods.setSalePrice(((BigDecimal) entry.getValue()).floatValue());
				}
				if ("Discount".equals(entry.getKey())) {
					goods.setDiscount(((BigDecimal) entry.getValue()).floatValue());
				}
				if ("StockNum".equals(entry.getKey())) {
					goods.setStockNum((int) entry.getValue());
				}
				if ("StockDate".equals(entry.getKey())) {
					Object val = entry.getValue();
					if (val instanceof Date) {
						goods.setStockDate((Date) val);
					} else if (val instanceof java.time.LocalDateTime) {
						java.time.LocalDateTime ldt = (java.time.LocalDateTime) val;
						goods.setStockDate(java.sql.Timestamp.valueOf(ldt));
					} else {
						goods.setStockDate(null);
					}
				}
			}
			stype.setParentType(ftype);
			goods.setGoodsType(stype);
		} else {
			goods = null;
		}
		return goods;
	}

	@Override
	public boolean addGoods(Goods goods) {
		String sql = "INSERT into t_goods(BarCode,GoodsName,StorePrice,SalePrice,Discount,StockNum,StockDate,TypeID) values(?,?,?,?,?,?,?,?);";
		List<Object> values = new ArrayList<Object>();
		values.add(goods.getBarCode());
		values.add(goods.getGoodsName());
		values.add(goods.getStorePrice());
		values.add(goods.getSalePrice());
		values.add(goods.getDiscount());
		values.add(goods.getStockNum());
		values.add(goods.getStockDate());
		values.add(goods.getGoodsType() == null ? 0 : goods.getGoodsType().getTypeId());
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public boolean updateGoods(Goods goods) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStoreNum(int goodsId, int goodsNum) {
		String sql = "UPDATE t_goods g  set StockNum=? where GoodsID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(goodsNum);
		values.add(goodsId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public List<Goods> getGoodsListView(String barcode, String goodsName, String startDate, String endDate,
			GoodsType goodsType) {
		StringBuilder sql = new StringBuilder(
				"SELECT g.GoodsID,g.BarCode,g.GoodsName,g.StorePrice,g.SalePrice,g.Discount,g.StockNum,g.StockDate,\r\n"
						+ "s.TypeID AS sTypeId,s.TypeName AS sTypeName\r\n" + "FROM t_goods g\r\n"
						+ "LEFT JOIN t_goodstype s  ON g.TypeID=s.TypeID\r\n WHERE 1=1\r\n");
		List<Object> values = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(barcode)) {
			sql.append("AND g.BarCode LIKE ?  ");
			values.add("%" + barcode + "%");
		}
		if (StringUtils.isNotEmpty(goodsName)) {
			sql.append("AND g.GoodsName LIKE ?  ");
			values.add("%" + goodsName + "%");
		}
		if (StringUtils.isNotEmpty(startDate)) {
			sql.append("AND g.StockDate >=?  ");
			values.add(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql.append("AND g.StockDate <=?  ");
			values.add(endDate);
		}
		if (goodsType != null && goodsType.getTypeId() != 0) {
			sql.append("AND g.TypeID =?  ");
			values.add(goodsType.getTypeId());
		}
		sql.append(" ORDER BY  g.StockDate DESC ");
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql.toString(), values);
		Goods goods;
		GoodsType stype;
		List<Goods> goodsList = new ArrayList<Goods>();
		if (ret != null && ret.size() > 0) {
			for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				goods = new Goods();
				stype = new GoodsType();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("GoodsID".equals(entry.getKey())) {
						goods.setGoodsId((int) entry.getValue());
					}
					if ("BarCode".equals(entry.getKey())) {
						goods.setBarCode((String) entry.getValue());
					}
					if ("sTypeId".equals(entry.getKey())) {
						stype.setTypeId((int) entry.getValue());
					}
					if ("sTypeName".equals(entry.getKey())) {
						stype.setTypeName((String) entry.getValue());
					}
					if ("GoodsName".equals(entry.getKey())) {
						goods.setGoodsName((String) entry.getValue());
					}
					if ("StorePrice".equals(entry.getKey())) {
						goods.setStorePrice(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("SalePrice".equals(entry.getKey())) {
						goods.setSalePrice(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("Discount".equals(entry.getKey())) {
						goods.setDiscount(((BigDecimal) entry.getValue()).floatValue());
					}
					if ("StockNum".equals(entry.getKey())) {
						goods.setStockNum((int) entry.getValue());
					}
					if ("StockDate".equals(entry.getKey())) {
						goods.setStockDate((Date) entry.getValue());
					}
				}
				goods.setGoodsType(stype);
				goodsList.add(goods);
			}
		}
		return goodsList;
	}

	@Override
	public boolean returnStoreNum(int goodsId, int num) {
		String sql = "UPDATE t_goods g  set StockNum=(StockNum+?) where GoodsID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(num);
		values.add(goodsId);
		int ret = ExecuteCommon.updateDatas(sql, values);
		return ret > 0 ? true : false;
	}

	@Override
	public int getGoodsCountByType(int typeId) {
		String sql = "SELECT COUNT(1) FROM t_goods G WHERE G.TypeID=?;";
		List<Object> values = new ArrayList<Object>();
		values.add(typeId);
		int num = ExecuteCommon.getCounts(sql, values);
		return num;
	}

}
