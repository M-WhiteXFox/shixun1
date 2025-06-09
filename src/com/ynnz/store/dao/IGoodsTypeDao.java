package com.ynnz.store.dao;

import java.util.List;

import com.ynnz.store.pojo.GoodsType;

public interface IGoodsTypeDao {

	/**
	 * 根据父级ID查分类
	 *
	 * @param parentTypeId 为0时候查第一层级 * @return
	 */
	public List<GoodsType> getGoodsTypeList(int parentTypeId);

	/**
	 * 查所有商品分类信息
	 *
	 * @return
	 */
	public List<GoodsType> getGoodsTypeList();

	/**
	 * 添加信的分类
	 *
	 * @param typeName
	 * @param pTypeId
	 * @return
	 */
	public boolean addGoodsType(String typeName, int pTypeId);

	/**
	 * 更新某个商品分类的信息
	 *
	 * @param typeName
	 * @param pTypeId
	 * @param typeId
	 * @return
	 */
	public boolean updateGoodsType(String typeName, int pTypeId, int typeId);

	/**
	 * 查某分类下子分类数目
	 *
	 * @param pTypeId
	 * @return
	 */
	public int getChildrenCount(int pTypeId);

	/**
	 * 删除某个分类
	 * @param typeId
	 * @return
	 */
	public boolean deleteTypeById(int typeId);

	/**
	 * 查询某个分类
	 * @param typeId
	 * @return
	 */
	public GoodsType getTypeById(int typeId);

}
