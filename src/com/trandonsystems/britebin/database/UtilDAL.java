package com.trandonsystems.britebin.database;

public class UtilDAL {

	public static String envName = "";
	
	// Local DB
	public static String connUrl = "jdbc:mysql://localhost:3306/britebin?serverTimezone=UTC";
	public static String username = "admin";
	public static String password = "Rebel123456#.";

	public UtilDAL() {
		if(envName == null)
			envName = "";
		switch (envName) {
		case "PROD":
			connUrl = "jdbc:mysql://localhost:3306/britebin?serverTimezone=UTC";
			username = "admin";
			password = "Rebel123456#.";
			break;
		default:
			connUrl = "jdbc:mysql://localhost:3306/britebin?serverTimezone=UTC";
			username = "admin";
			password = "Rebel123456#.";
			break;
		}
	}
}
