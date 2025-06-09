package com.ynnz.store.dao;

import java.util.List;

import com.ynnz.store.pojo.UserInfo;

/**
 * 用户信息管理接口
 *
 * @author 吕琼华
 *
 */
public interface IUserinfoDao {

	/**
	 * 新增加员工
	 *
	 * @param user
	 */
	public boolean addUser(UserInfo user);

	/**
	 * 删除某个员工
	 *
	 * @param userId
	 * @return
	 */
	public boolean delUser(int userId);

	/**
	 * 修改某个员工的信息
	 *
	 * @param userId
	 * @return
	 */
	public boolean updateUser(UserInfo user);

	/**
	 * 查询某个用户的信息
	 *
	 * @param userId
	 * @return
	 */
	public UserInfo getUser(int userId);

	/**
	 * 查询所有用户信息
	 *
	 * @return
	 */
	public List<UserInfo> getUserList();

	/**
	 * 查询某个用户的信息
	 *
	 * @param mobile
	 * @return
	 */
	public UserInfo getUserByMobile(String mobile);

	/**
	 * 指定月份统计员工薪资
	 * @param monthDate
	 * @return
	 */
	public List<UserInfo> getUserSalaryStatistic(String monthDate);
}
