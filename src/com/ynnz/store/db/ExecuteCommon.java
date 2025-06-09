package com.ynnz.store.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import com.mysql.jdbc.Statement;

/**
 * @author 吕琼华 获取Statement对象实例
 */
public class ExecuteCommon {
	/**
	 * 执行增、删、改三类操作，以及执行表的创建，修改等DDL操作
	 *
	 * @param sql
	 * @param values
	 * @return 返回影响的行数
	 * @throws Exception
	 * @throws SQLException
	 */
	public static int updateDatas(String sql, List<Object> values) {
		int num = 0;
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					ps.setObject((i + 1), values.get(i));
				}
			}
			num = ps.executeUpdate();
			DBConnection.closeConnection(null, ps, con);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 执行批量操作
	 *
	 * @param sql
	 * @param values
	 * @return 返回操作的行数
	 * @throws Exception
	 * @throws SQLException
	 */
	public static int  updateBatchDatas(String sql, List<Object> values) {
		int  num = 0;
		try {
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			System.out.println("sql:"+sql);
			if (values != null) {
				int k = 0;
				for (int i = 0; i < values.size(); i++) {
					Object[] row = (Object[])values.get(i);
					for (int j = 0; j < row.length; j++) {
						k = k+1;
						ps.setObject(k, row[j]);
						System.out.println(k+":"+row[j]);
					}
				}
			}
			num = ps.executeUpdate();
			DBConnection.closeConnection(null, ps, con);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 执行增、删、改三类操作，以及执行表的创建，修改等DDL操作
	 *
	 * @param sql
	 * @param values
	 * @return 返回插入数据的主键ID
	 * @throws Exception
	 * @throws SQLException
	 */
	public static int updateQueryDatas(String sql, List<Object> values) {
		int id = 0 ;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					ps.setObject((i + 1), values.get(i));
				}
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			id = rs.getInt(1);

			DBConnection.closeConnection(rs, ps, con);
		} catch (SQLException e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		} catch (Exception e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 查询数据结果集
	 *
	 * @param sql
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> queryDatas(String sql, List<Object> values) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					ps.setObject((i + 1), values.get(i));
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData(); // 获得列集
			int col = rsm.getColumnCount(); // 获得列的个数
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> obj = new HashMap<String, Object>();
					for (int i = 0; i < col; i++) {
//						String colName = rsm.getColumnName(i + 1);//如果字段名重命名后这个方法获取不到
						String colName = rsm.getColumnLabel(i + 1);
						obj.put(colName, rs.getObject(colName));
					}
					ret.add(obj);
				}
			}
			DBConnection.closeConnection(rs, ps, con);
		} catch (SQLException e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		} catch (Exception e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 获取查询结果行数
	 * @param sql
	 * @param values
	 * @return
	 */
	public static int getCounts(String sql, List<Object> values) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowCount = 0;
		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					ps.setObject((i + 1), values.get(i));
				}
			}
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				rowCount = rs.getInt(1);
			}
			DBConnection.closeConnection(rs, ps, con);
		} catch (SQLException e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		} catch (Exception e) {
			DBConnection.closeConnection(rs, ps, con);
			e.printStackTrace();
		}

		return rowCount;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = null;
//		sql = "INSERT into t_userinfo(SalesmanName,Mobile,Pwd,Gender,BaseSalary,CommissionRate,Role) values(?,?,?,?,?,?,?);";
		List<Object> datas = new ArrayList<Object>();
		datas.add("张四");
//		datas.add("15921322468");
//		datas.add("123456");
//		datas.add("男");
		datas.add(2222.46);
//		datas.add(0.25);
//		datas.add("店长");
		datas.add(8);
//		sql="UPDATE t_userinfo u set u.SalesmanName=?,u.BaseSalary=? where u.SalesmanID=?";
		sql = "SELECT * FROM t_userinfo";
		try {
//			int num = ExecuteCommon.updateDatas(sql, datas);
//			System.out.println("更新行数：" + num);
			List<Map<String, Object>> ret = ExecuteCommon.queryDatas(sql, null);
			for (Map<String, Object> map : ret) {
				for (Iterator<Entry<String, Object>> ite = map.entrySet().iterator(); ite.hasNext();) {
					Entry<String, Object> entry = ite.next();
					System.out.println(entry.getKey() + "-----" + entry.getValue());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
