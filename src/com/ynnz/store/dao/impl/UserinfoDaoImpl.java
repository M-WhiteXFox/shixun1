package com.ynnz.store.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ynnz.store.dao.IUserinfoDao;
import com.ynnz.store.db.ExecuteCommon;
import com.ynnz.store.pojo.UserInfo;
import com.ynnz.store.util.StringUtils;

public class UserinfoDaoImpl implements IUserinfoDao {

	@Override
	public boolean addUser(UserInfo user) {
		String sql = "INSERT into t_userinfo(SalesmanName,Mobile,Pwd,Gender,BaseSalary,CommissionRate,Role) values(?,?,?,?,?,?,?);";
		List<Object> values = new ArrayList<Object>();
		values.add(user.getSaleManName());
		values.add(user.getMobile());
		values.add(user.getPwd());
		values.add(user.getGender());
		values.add(user.getBasesalary());
		values.add(user.getCommissionRate());
		values.add(user.getRole());
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public boolean delUser(int userId) {
		String sql = "DELETE FROM t_userinfo WHERE SalesmanID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public boolean updateUser(UserInfo user) {
		String sql = "UPDATE t_userinfo u set u.SalesmanName=?,u.Mobile=?,u.Pwd=?,u.Gender=?,"
				+ "u.BaseSalary=?,u.CommissionRate=?,u.Role=? where u.SalesmanID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(user.getSaleManName());
		values.add(user.getMobile());
		values.add(user.getPwd());
		values.add(user.getGender());
		values.add(user.getBasesalary());
		values.add(user.getCommissionRate());
		values.add(user.getRole());
		values.add(user.getSaleManId());
		int num = ExecuteCommon.updateDatas(sql, values);
		return num > 0 ? true : false;
	}

	@Override
	public UserInfo getUser(int userId) {
		String sql = "SELECT SalesmanID, SalesmanName,Mobile,Pwd,Gender,BaseSalary,CommissionRate,Role FROM t_userinfo u where u.SalesmanID=?";
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		UserInfo user = new UserInfo();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Entry<String, Object>> ite = (ret.get(0)).entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) ite.next();
				if ("SalesmanID".equals(entry.getKey())) {
					user.setSaleManId((int) entry.getValue());
				}
				if ("SalesmanName".equals(entry.getKey())) {
					user.setSaleManName((String) entry.getValue());
				}
				if ("Mobile".equals(entry.getKey())) {
					user.setMobile((String) entry.getValue());
				}
				if ("Pwd".equals(entry.getKey())) {
					user.setPwd((String) entry.getValue());
				}
				if ("Gender".equals(entry.getKey())) {
					user.setGender((String) entry.getValue());
				}
				if ("BaseSalary".equals(entry.getKey())) {
					user.setBasesalary((float) entry.getValue());
				}
				if ("CommissionRate".equals(entry.getKey())) {
					user.setCommissionRate((BigDecimal) entry.getValue());
				}
				if ("Role".equals(entry.getKey())) {
					user.setRole((String) entry.getValue());
				}
			}
		}
		return user;
	}

	@Override
	public List<UserInfo> getUserList() {
		String sql = "SELECT SalesmanID, SalesmanName,Mobile,Pwd,Gender,BaseSalary,CommissionRate,Role FROM t_userinfo ";
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, null);
		UserInfo user = null;
		List<UserInfo> userList = new ArrayList<UserInfo>();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Map<String, Object>> iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				user = new UserInfo();
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("SalesmanID".equals(entry.getKey())) {
						user.setSaleManId((int) entry.getValue());
					}
					if ("SalesmanName".equals(entry.getKey())) {
						user.setSaleManName((String) entry.getValue());
					}
					if ("Mobile".equals(entry.getKey())) {
						user.setMobile((String) entry.getValue());
					}
					if ("Pwd".equals(entry.getKey())) {
						user.setPwd((String) entry.getValue());
					}
					if ("Gender".equals(entry.getKey())) {
						user.setGender((String) entry.getValue());
					}
					if ("BaseSalary".equals(entry.getKey())) {
						user.setBasesalary((float) entry.getValue());
					}
					if ("CommissionRate".equals(entry.getKey())) {
						user.setCommissionRate((BigDecimal) entry.getValue());
					}
					if ("Role".equals(entry.getKey())) {
						user.setRole((String) entry.getValue());
					}
				}
				userList.add(user);
			}
		}
		return userList;
	}

	@Override
	public UserInfo getUserByMobile(String mobile) {
		String sql = "SELECT SalesmanID, SalesmanName,Mobile,Pwd,Gender,BaseSalary,CommissionRate,Role FROM t_userinfo u where u.Mobile=?";
		List<Object> values = new ArrayList<Object>();
		values.add(mobile);
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, values);
		UserInfo user = new UserInfo();
		if (ret != null && ret.size() > 0) {
			for (Iterator<Entry<String, Object>> ite = (ret.get(0)).entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = (Entry<String, Object>) ite.next();
				if ("SalesmanID".equals(entry.getKey())) {
					user.setSaleManId((int) entry.getValue());
				}
				if ("SalesmanName".equals(entry.getKey())) {
					user.setSaleManName((String) entry.getValue());
				}
				if ("Mobile".equals(entry.getKey())) {
					user.setMobile((String) entry.getValue());
				}
				if ("Pwd".equals(entry.getKey())) {
					user.setPwd((String) entry.getValue());
				}
				if ("Gender".equals(entry.getKey())) {
					user.setGender((String) entry.getValue());
				}
				if ("BaseSalary".equals(entry.getKey())) {
					user.setBasesalary((float) entry.getValue());
				}
				if ("CommissionRate".equals(entry.getKey())) {
					user.setCommissionRate((BigDecimal) entry.getValue());
				}
				if ("Role".equals(entry.getKey())) {
					user.setRole((String) entry.getValue());
				}
			}
		} else {
			user = null;
		}
		return user;
	}

	@Override
	public List<UserInfo> getUserSalaryStatistic(String monthDate) {
		StringBuffer sql = new StringBuffer("SELECT U.SalesmanName,U.Role,U.Mobile,U.BaseSalary,U.CommissionRate,\r\n"
				+ "SUM(S.Amount) AS SaleMoney,date_format(S.SalesDate,'%Y-%m') AS Dmonth FROM t_userinfo U \r\n"
				+ "LEFT JOIN t_sales S ON U.SalesmanID=S.SalesmanID  GROUP BY U.SalesmanID, date_format(S.SalesDate,'%Y-%m') ");
		List<Object> values = new ArrayList<Object>();
		List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql.toString(), values);
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (ret != null && ret.size() > 0) {
			boolean hasMonth = true;
			for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				UserInfo saleMan = new UserInfo();
				hasMonth = true;
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) ite.next();
					if ("Dmonth".equals(entry.getKey())) {
						saleMan.setSaleMonth((String) entry.getValue());
					}
					if ("SalesmanName".equals(entry.getKey())) {
						saleMan.setSaleManName((String) entry.getValue());
					}
					if ("Role".equals(entry.getKey())) {
						saleMan.setRole(((String) entry.getValue()));
					}
					if ("Mobile".equals(entry.getKey())) {
						saleMan.setMobile(((String) entry.getValue()));
					}
					if ("BaseSalary".equals(entry.getKey())) {
						saleMan.setBasesalary((float) entry.getValue());
					}
					if ("CommissionRate".equals(entry.getKey())) {
						saleMan.setCommissionRate((BigDecimal) entry.getValue());
					}
					if ("SaleMoney".equals(entry.getKey()) && entry.getValue() != null) {
						saleMan.setSaleMoney(((BigDecimal) entry.getValue()).floatValue());
					}
				}
				list.add(saleMan);
			}
			if (StringUtils.isNotEmpty(monthDate) && list != null && list.size() > 0) {
				for (UserInfo userInfo : list) {
					if (!monthDate.equals(userInfo.getSaleMonth())) {
						userInfo.setSaleMoney(0);
					}
				}
			}
		}
		return list;
	}

}
