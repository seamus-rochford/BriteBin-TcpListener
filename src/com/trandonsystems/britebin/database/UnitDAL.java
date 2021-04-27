package com.trandonsystems.britebin.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trandonsystems.britebin.model.UnitMessage;
import com.trandonsystems.britebin.model.UnitReading;
import com.trandonsystems.britebin.model.Unit;

public class UnitDAL {

	static final String SOURCE = "NB-IoT BB";
	
	static Logger log = Logger.getLogger(UnitDAL.class);
	static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static long saveRawData(byte[] data) throws SQLException{

		log.info("UnitDAL.saveRawData(data)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
			throw new SQLException("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String spCall = "{ call SaveRawReadings(?, ?) }";
		log.info("SP Call: " + spCall);

		long id = 0;
		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setBytes(1, data);
			spStmt.setString(2, SOURCE);
			ResultSet rs = spStmt.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt("id");
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
		
		return id;
	}

	public static UnitMessage saveReading(long rawDataId, long unitId, UnitReading reading) throws SQLException {

		log.info("UnitDAL.saveReading(rawDataId, reading)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		UnitMessage unitMsg = new UnitMessage();
		
		String spCall = "{ call SaveReadingNBIoT_V2(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
		log.debug("SP Call: " + spCall);

		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			unitMsg = getUnitMsg(conn, reading.serialNo);
			log.debug("unitMsg: " + gson.toJson(unitMsg));		

			spStmt.setLong(1, unitMsg.unitId);
			spStmt.setString(2, reading.serialNo);
			spStmt.setLong(3, rawDataId);
			spStmt.setInt(4, reading.msgType);
			spStmt.setInt(5, reading.binLevelBC);
			spStmt.setInt(6, reading.binLevel);
			spStmt.setInt(7, reading.noFlapOpening);
			spStmt.setInt(8, reading.batteryVoltage);
			spStmt.setInt(9, reading.temperature);
			spStmt.setInt(10, reading.noCompactions);
			spStmt.setInt(11, reading.batteryUVLO ? 1 : 0);
			spStmt.setInt(12, reading.binEmptiedLastPeriod ? 1 : 0);
			spStmt.setInt(13, reading.batteryOverTempLO ? 1 : 0);
			spStmt.setInt(14, reading.binLocked ? 1 : 0);
			spStmt.setInt(15, reading.binFull ? 1 : 0);
			spStmt.setInt(16, reading.binTilted ? 1 : 0);
			spStmt.setInt(17, reading.serviceDoorClosed ? 1 : 0);
			spStmt.setInt(18, reading.flapStuckOpen ? 1 : 0);
			spStmt.setInt(19, reading.serviceDoorOpen ? 1 : 0);
			spStmt.setDouble(20, reading.rssi);
			spStmt.setInt(21, reading.src);
			spStmt.setDouble(22, reading.snr);
			spStmt.setInt(23, reading.ber);
			spStmt.setDouble(24, reading.rsrq);
			spStmt.setInt(25, reading.rsrp);

			// Convert java.time.Instant to java.sql.timestamp
			Timestamp ts = Timestamp.from(reading.readingDateTime);
		    spStmt.setTimestamp(26, ts);
		    
			spStmt.setString(27, SOURCE);
			
			spStmt.setString(28, reading.firmware);
			spStmt.setLong(29, reading.timeDiff);
			spStmt.setInt(30, reading.binJustOn ? 1 : 0);
			spStmt.setInt(31, reading.regularPeriodicReporting ? 1 : 0);
			spStmt.setInt(32, reading.nbiotSimIssue ? 1 : 0);

			spStmt.executeQuery();

		} catch (SQLException ex) {
			log.error("UnitDAL.saveReading: " + ex.getMessage());
			throw ex;
		}
		
		return unitMsg;
	}
	
	public static UnitMessage saveReadingFirmware(long rawDataId, long unitId, UnitReading reading) throws SQLException {

		log.info("UnitDAL.saveReadingFirmware(rawDataId, unitId, reading)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		UnitMessage unitMsg = new UnitMessage();
		
		String spCall = "{ call saveReadingFirmware(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
		log.debug("SP Call: " + spCall);

		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			unitMsg = getUnitMsg(conn, reading.serialNo);
			log.debug("unitMsg: " + gson.toJson(unitMsg));		

			spStmt.setLong(1, unitId);
			spStmt.setLong(2, rawDataId);
			spStmt.setString(3, reading.firmware);
			spStmt.setLong(4, reading.timeDiff);
			spStmt.setInt(5, reading.binJustOn ? 1 : 0);
			spStmt.setInt(6, reading.regularPeriodicReporting ? 1 : 0);
			spStmt.setInt(7, reading.nbiotSimIssue ? 1 : 0);
			spStmt.setInt(8,  reading.hardwareRevision);
			spStmt.setString(9, SOURCE);

			spStmt.executeQuery();

		} catch (SQLException ex) {
			log.error("UnitDAL.saveReadingFirmware: " + ex.getMessage());
			throw ex;
		}
		
		log.info("UnitDAL.saveReadingFirmware(rawDataId, unitId, reading) - end");

		return unitMsg;
	}
	
	
	public static UnitMessage getUnitMsg(Connection conn, String serialNo) throws SQLException {
		log.info("UnitDAL.getUnitMsg(conn, serialNo)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}
 
		log.debug("SerialNo: " + serialNo);
		String spCall = "{ call GetUnitMessage(?) }";
		log.debug("SP Call: " + spCall);

		UnitMessage unitMsg = new UnitMessage();
		
		try (CallableStatement spStmt = conn.prepareCall(spCall)) {
			spStmt.setString(1, serialNo);
			ResultSet rs = spStmt.executeQuery();

			if (rs.next()) {
				unitMsg.unitId = rs.getInt("unitId");
				unitMsg.replyMessage = rs.getBoolean("replyMessage");
				unitMsg.messageId = rs.getInt("messageId");
				unitMsg.message = rs.getBytes("message");
			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		}

		if (unitMsg.message != null) {
			int msgType = unitMsg.message[0] & 0xff;
			if (msgType == 4) {
				// Here we want to set the time of the device, so we have to get the current date and time just before we send the message
				
				// Get the current UTC Date/Time to set for the unit
				
				// Get UTC Date/Time
				LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
				
				byte[] msg = new byte[8];
				msg[0] = (byte)msgType;
				msg[1] = (byte)(now.getYear() % 100);  // Get 2 digit year part
				msg[2] = (byte)now.getMonthValue();
				msg[3] = (byte)now.getDayOfMonth();
				msg[4] = (byte)now.getHour();
				msg[5] = (byte)now.getMinute();
				msg[6] = (byte)now.getSecond();
				msg[7] = unitMsg.message[7];
				
				unitMsg.message = msg;
			}
		}
		
		return unitMsg;
	}
	
	public static void markMessageAsSent(UnitMessage unitMsg) throws SQLException{

		log.info("UnitDAL.markMessageAsSent(unitMsg)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String spCall = "{ call MarkMessageAsSent(?) }";
		log.info("SP Call: " + spCall);

		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setInt(1, unitMsg.messageId);
			spStmt.executeUpdate();

		} catch (SQLException ex) {
			log.error("UnitDAL.getUnit" + ex.getMessage());
			throw ex;
		}
	}

	public static Unit getUnitBySerialNo(int filterUserId, String serialNo) {
		log.info("UnitDAL.get(id)");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			log.error("ERROR: Can't create instance of driver" + ex.getMessage());
		}

		String spCall = "{ call GetUnitBySerialNo(?, ?) }";
		log.info("SP Call: " + spCall);
		
		Unit unit = new Unit();
		
		try (Connection conn = DriverManager.getConnection(UtilDAL.connUrl, UtilDAL.username, UtilDAL.password);
				CallableStatement spStmt = conn.prepareCall(spCall)) {

			spStmt.setInt(1,  filterUserId);
			spStmt.setString(2, serialNo);
			ResultSet rs = spStmt.executeQuery();

			unit.serialNo = serialNo;
			if (rs.next()) {
				unit.id = rs.getInt("id");
				unit.location = rs.getString("location");
			
				// firmware values
				unit.firmware = rs.getString("units.firmware");
				unit.timeDiff = rs.getLong("units.timeDiff");
				unit.binJustOn = (rs.getInt("units.binJustOn") == 1);
				unit.regularPeriodicReporting = (rs.getInt("units.regularPeriodicReporting") == 1);
				unit.nbiotSimIssue = (rs.getInt("units.nbiotIssue") == 1);
			}

		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}

		return unit;
	}

}
