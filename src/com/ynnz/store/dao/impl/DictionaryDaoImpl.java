package com.ynnz.store.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ynnz.store.dao.IDictionaryDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.Dictionary;

public class DictionaryDaoImpl implements IDictionaryDao {

	@Override
	public boolean addDictionary(Dictionary dictionary) {
		String sql = "INSERT INTO t_dictionary(DIC_NAME,DIC_VALUE,DIC_TYPE,DIC_ORDER) VALUES(?,?,?,?);";
		List<Object> values = new ArrayList<Object>();
		values.add(dictionary.getTypeName());
		values.add(dictionary.getTypeValue());
		values.add(dictionary.getDicType());
		values.add(dictionary.getDicOrder());
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public boolean updateDictionary(Dictionary dictionary) {
		String sql = "UPDATE t_dictionary SET DIC_NAME=?,DIC_VALUE=?,DIC_TYPE=?,DIC_ORDER=? WHERE ID=?;";
		List<Object> values = new ArrayList<Object>();
		values.add(dictionary.getTypeName());
		values.add(dictionary.getTypeValue());
		values.add(dictionary.getDicType());
		values.add(dictionary.getDicOrder());
		values.add(dictionary.getId());
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public List<Dictionary> getListByType(int dicType) {
		String sql = "SELECT ID,DIC_NAME,DIC_VALUE,DIC_TYPE,DIC_ORDER FROM t_dictionary WHERE DIC_TYPE=? ORDER BY DIC_ORDER;";
		List<Object> values = new ArrayList<Object>();
		values.add(dicType);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		Dictionary dic = null;
		List<Dictionary> dicList = new ArrayList<Dictionary>();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Map<String, Object>> iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				dic = new Dictionary();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("ID".equals(entry.getKey())) {
						dic.setId((int) entry.getValue());
					}
					if ("DIC_NAME".equals(entry.getKey())) {
						dic.setTypeName((String) entry.getValue());
					}
					if ("DIC_VALUE".equals(entry.getKey())) {
						dic.setTypeValue((String) entry.getValue());
					}
					if ("DIC_TYPE".equals(entry.getKey())) {
						dic.setDicType((int) entry.getValue());
					}
					if ("DIC_ORDER".equals(entry.getKey())) {
						dic.setDicOrder((int) entry.getValue());
					}
				}
				dicList.add(dic);
			}
		}
		return dicList;
	}

	@Override
	public Dictionary getDictionaryByTypeAndName(int dicType, String name) {
		String sql = "SELECT ID,DIC_NAME,DIC_VALUE,DIC_TYPE,DIC_ORDER FROM t_dictionary WHERE DIC_TYPE=? AND DIC_NAME=? ORDER BY DIC_ORDER;";
		List<Object> values = new ArrayList<Object>();
		values.add(dicType);
		values.add(name);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		Dictionary dic = null;
		if (ret != null && ret.size() > 0) {
			dic = new Dictionary();
			for (Iterator<Entry<String, Object>> ite = (ret.get(0)).entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) ite.next();
				if ("ID".equals(entry.getKey())) {
					dic.setId((int) entry.getValue());
				}
				if ("DIC_NAME".equals(entry.getKey())) {
					dic.setTypeName((String) entry.getValue());
				}
				if ("DIC_VALUE".equals(entry.getKey())) {
					dic.setTypeValue((String) entry.getValue());
				}
				if ("DIC_TYPE".equals(entry.getKey())) {
					dic.setDicType((int) entry.getValue());
				}
				if ("DIC_ORDER".equals(entry.getKey())) {
					dic.setDicOrder((int) entry.getValue());
				}
			}
		}
		return dic;
	}

}
