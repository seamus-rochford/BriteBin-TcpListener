package com.trandonsystems.britebin.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class EmailDAL {

	static Logger log = Logger.getLogger(EmailDAL.class);

	
	public static int sendEmail(int alertId, String email, String subject, boolean htmlBody, String body) throws SQLException {
		// This does not actually send an email - instead it inputs a record into the Alerts email tabel
		// and the Alert service will pick it up and send it
		log.info("EmailDAL.sendEmail()");

		String spCall = "{ call InsertEmail(?, ?, ?, ?, ?, ?) }";
		log.info("SP Call: " + spCall);

		int id = 0;
		
		try (Connection conn = UtilDAL.getConnection();
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setInt(1, id);
			spStmt.setInt(2, alertId);
			spStmt.setString(3, email);
			spStmt.setString(4, subject);
			spStmt.setInt(5, htmlBody ? 1 : 0);
			spStmt.setString(6, body);
			spStmt.executeUpdate();
			
			id = spStmt.getInt(1);
			
			return id;

		} catch (SQLException ex) {
			log.error("EmailDAL - ERROR - sendEmail: " + ex.getMessage());
			throw ex;
		}			
	}
}
