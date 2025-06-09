package com.ynnz.store.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.ynnz.store.util.Constants;

/**
 * 获取数据库连接
 *
 * @author 吕琼华
 *
 */
public class DBConnection {
	private static String DRIVER_STRING = "";
	private static String DB_STRING = "";
	private static String USERNAME = "";
	private static String PASSWORD = "";

	static {
		Properties properties = new Properties();
		try {
			// 使用InPutStream流读取properties文件
			InputStream bufferedReader = DBConnection.class.getResourceAsStream("db.properties");
			properties.load(bufferedReader);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 获取key对应的value值
		DRIVER_STRING = properties.getProperty(Constants.DRIVER_STRING);
		DB_STRING = properties.getProperty(Constants.DB_STRING);
		USERNAME = properties.getProperty(Constants.USERNAME);
		PASSWORD = properties.getProperty(Constants.PASSWORD);
		try {
			// 加载驱动程序
			Class.forName(DRIVER_STRING);
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动异常！");
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接对象
	 *
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public static Connection getConnection() throws Exception {
		try {
			return DriverManager.getConnection(DB_STRING, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("获取数据库连接出错！", e);
		}
	}

	/**
	 * 关闭数据库操作，回收数据库资源
	 *
	 * @param rs
	 * @param ps
	 * @param con
	 * @throws SQLException
	 */
	public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
