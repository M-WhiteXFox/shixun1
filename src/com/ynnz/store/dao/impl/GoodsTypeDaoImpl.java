package com.ynnz.store.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ynnz.store.dao.IGoodsTypeDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.GoodsType;

public class GoodsTypeDaoImpl implements IGoodsTypeDao {

	@Override
	public List<GoodsType> getGoodsTypeList(int parentTypeId) {
		String sql = "SELECT s.TypeID,s.TypeName,s.TypeID as pid,s.TypeName as pname FROM t_goodstype s\r\n"
				+ "LEFT JOIN t_goodstype p ON s.ParentID=p.TypeID\r\n" + " WHERE s.ParentID = ? ";
		List<Object> values = new ArrayList<Object>();
		values.add(parentTypeId);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		GoodsType goodsType = null;
		List<GoodsType> goodsTypeList = new ArrayList<GoodsType>();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Map<String, Object>> iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				goodsType = new GoodsType();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("TypeID".equals(entry.getKey())) {
						goodsType.setTypeId((int) entry.getValue());
					}
					if ("TypeName".equals(entry.getKey())) {
						goodsType.setTypeName((String) entry.getValue());
					}
					GoodsType pType = new GoodsType();
					if ("pid".equals(entry.getKey())) {
						pType.setTypeId(((int) entry.getValue()));
					}
					if ("pname".equals(entry.getKey())) {
						pType.setTypeName((String) entry.getValue());
					}
					goodsType.setParentType(pType);
				}
				goodsTypeList.add(goodsType);
			}
		}
		return goodsTypeList;
	}

	@Override
	public List<GoodsType> getGoodsTypeList() {
		String sql = "SELECT S1.TypeID,S1.TypeName,S2.TypeName AS pname FROM t_goodstype S1 \r\n"
				+ "LEFT JOIN t_goodstype S2 ON S1.ParentID=S2.TypeID \r\n" + "ORDER BY S1.ParentID,TypeID ";
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, null);
		GoodsType goodsType = null;
		List<GoodsType> goodsTypeList = new ArrayList<GoodsType>();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Map<String, Object>> iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				goodsType = new GoodsType();
				GoodsType pType = new GoodsType();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("TypeID".equals(entry.getKey())) {
						goodsType.setTypeId((int) entry.getValue());
					}
					if ("TypeName".equals(entry.getKey())) {
						goodsType.setTypeName((String) entry.getValue());
					}
					if ("pname".equals(entry.getKey()) && entry.getValue() != null) {
						pType.setTypeName((String) entry.getValue());
					}
				}
				goodsType.setParentType(pType);
				goodsTypeList.add(goodsType);
			}
		}
		return goodsTypeList;
	}

	@Override
	public boolean addGoodsType(String typeName, int pTypeId) {
		String sql = "INSERT INTO t_goodstype(TypeName,ParentID)  VALUES(?,?);";
		List<Object> values = new ArrayList<Object>();
		values.add(typeName);
		values.add(pTypeId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public boolean updateGoodsType(String typeName, int pTypeId, int typeId) {
		String sql = "UPDATE t_goodstype  SET TypeName=?, ParentID=? WHERE TypeID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(typeName);
		values.add(pTypeId);
		values.add(typeId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public int getChildrenCount(int pTypeId) {
		String sql = "SELECT COUNT(1) FROM t_goodstype S WHERE S.ParentID=?;";
		List<Object> values = new ArrayList<Object>();
		values.add(pTypeId);
		int num = ExecuteCommon.getCounts(sql, values);
		return num;
	}

	@Override
	public boolean deleteTypeById(int typeId) {
		String sql = "DELETE FROM t_goodstype WHERE TypeID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(typeId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public GoodsType getTypeById(int typeId) {
		String sql = "SELECT *  FROM t_goodstype WHERE TypeID=? ";
		List<Object> values = new ArrayList<Object>();
		values.add(typeId);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		GoodsType goodsType = null;
		if (ret != null && ret.size() > 0) {
			goodsType = new GoodsType();
			GoodsType pType = new GoodsType();
			for (Iterator<Entry<String, Object>> ite = ret.get(0).entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) ite.next();
				if ("TypeID".equals(entry.getKey())) {
					goodsType.setTypeId((int) entry.getValue());
				}
				if ("TypeName".equals(entry.getKey())) {
					goodsType.setTypeName((String) entry.getValue());
				}
				if ("ParentID".equals(entry.getKey()) && entry.getValue() != null) {
					pType.setTypeId((int) entry.getValue());
				}
			}
			goodsType.setParentType(pType);
		}
		return goodsType;
	}

}
