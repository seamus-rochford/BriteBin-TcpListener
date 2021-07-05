package com.trandonsystems.britebin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.trandonsystems.britebin.utils.EnvUtil;

public class UtilDAL {

//	public static String envName = "";
	
	// Local DB
//	public static String connUrl = "jdbc:mysql://localhost:3306/britebin?serverTimezone=UTC";
//	public static String username = "admin";
//	public static String password = "Rebel123456#.";

	static Logger LOGGER = Logger.getLogger(UtilDAL.class);
	
	private static final String CONNECTION_URL = EnvUtil.get("db.connectionURL");
	private static final String DRIVER_CLASS = EnvUtil.get("db.driverClass");
	private static final String USERNAME = EnvUtil.get("db.username");
	private static final String PASSWORD = EnvUtil.get("db.password");
	
	public static Connection getConnection() throws SQLException {

		try {
			Class.forName(DRIVER_CLASS).getDeclaredConstructors();
		} catch (Exception ex) {
			LOGGER.error("Can't create instance of driver: " + ex.getMessage(), ex);
			throw new SQLException("ERROR: Can't create instance of driver" + ex.getMessage());
		}
		
		Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
		LOGGER.debug("Database connection established");
		
		return connection;
	}	
}
