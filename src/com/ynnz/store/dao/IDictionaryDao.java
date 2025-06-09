package com.ynnz.store.dao;

import java.util.List;

import com.ynnz.store.pojo.Dictionary;

public interface IDictionaryDao {

	/**
	 * 新增或更新一个数据字典项
	 *
	 * @param dictionary
	 * @return
	 */
	public boolean addDictionary(Dictionary dictionary);

	/**
	 * 更新数据字典信息
	 *
	 * @param dictionary
	 * @return
	 */
	public boolean updateDictionary(Dictionary dictionary);

	/**
	 * 查询某个类型的字典数据
	 *
	 * @param dicType
	 * @return
	 */
	public List<Dictionary> getListByType(int dicType);

	/**
	 * 查询某个类型下的某个名称的字典数据
	 *
	 * @param dicType 字典类型
	 * @param name    字典名称
	 * @return
	 */
	public Dictionary getDictionaryByTypeAndName(int dicType, String name);
}
