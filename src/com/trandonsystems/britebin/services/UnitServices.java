package com.trandonsystems.britebin.services;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;

import org.apache.log4j.Logger;

import com.trandonsystems.britebin.database.UnitDAL;
import com.trandonsystems.britebin.model.UnitMessage;
import com.trandonsystems.britebin.model.UnitReading;
import com.trandonsystems.britebin.model.Unit;

public class UnitServices {

	static Logger log = Logger.getLogger(UnitServices.class);

	
	private UnitMessage processBriteBinData(long rawDataId, byte[] data) throws Exception {

		log.info("processBriteBinData - start");

		UnitMessage unitMsg = new UnitMessage();
		UnitReading reading = new UnitReading();

		reading.msgType = data[0] & 0xff;
		
		// Helper variables
		int idSize;
		Unit unit;

		switch (reading.msgType) {
		case 1:
			reading.binLevelBC = data[1] & 0xff;
			reading.binLevel = data[2] & 0xff;
			reading.noFlapOpening = (data[3] & 0xff) * 256 + (data[4] & 0xff);
			reading.batteryVoltage = data[5] & 0xff;
			reading.temperature = data[6];   // signed value
			reading.noCompactions = data[7] & 0xff;
			
			int flags = data[8] & 0xff;
			reading.batteryUVLO = ((flags & 0x01) == 0x01);
			reading.binEmptiedLastPeriod = ((flags & 0x02) == 0x02);
			reading.batteryOverTempLO = ((flags & 0x04) == 0x04);
			reading.binLocked = ((flags & 0x08) == 0x08);
			reading.binFull = ((flags & 0x10) == 0x10);
			reading.binTilted = ((flags & 0x20) == 0x20);
			reading.serviceDoorClosed = ((flags & 0x40) == 0x40);
			reading.flapStuckOpen = ((flags & 0x80) == 0x80);
			
			flags = data[9] & 0xff;
			reading.serviceDoorOpen = ((flags & 0x01) == 0x01);
			
//			int signalStrength = data[9] & 0xff;
//			reading.nbIoTSignalStrength = signalStrength >> 4;
			reading.nbIoTSignalStrength = 0;
			
			// These come in as unsigned and i need to formulate them
			reading.rsrq = -19.5 + 0.5 * (data[10] & 0xff);   
			reading.rsrp = -140 + (data[11] & 0xff);
			
			idSize = data[12]; // note this is byte length
			
			if (data.length > 30) {
				// BriteBin Tcp Messages are 30 bytes or less
				throw new Exception("BriteBin messages are 30 bytes or less - this message is " + data.length + "bytes long");
			} else if (data.length < (13 + idSize)) {
				throw new Exception("There is not enough bytes in the message for a serialNo of size " + idSize + " bytes.");
			}
			reading.serialNo = "";
			for (int i = 0; i < idSize; i++) {
				reading.serialNo += Hex.ByteToHex(data[13 + i]);
			}
			
			log.debug("SerialNo (before): " + reading.serialNo);
			// Remove leading zero's from serialNo
			int i = 0;
			while (reading.serialNo.charAt(i) == '0' && i < reading.serialNo.length() - 1)
			    i++;
			reading.serialNo = reading.serialNo.substring(i).toUpperCase();
			log.debug("SerialNo (after): " + reading.serialNo);
			
	        unit = UnitDAL.getUnitBySerialNo(1, reading.serialNo);

	        reading.readingDateTime = Instant.now();
			
	        // Set firmware parameters in the reading
	        reading.firmware = unit.firmware;
	        reading.timeDiff = unit.timeDiff;
	        reading.binJustOn = unit.binJustOn;
	        reading.regularPeriodicReporting = unit.regularPeriodicReporting;
	        reading.nbiotSimIssue = unit.nbiotSimIssue;
	        
	        log.info(reading);
			
			unitMsg = UnitDAL.saveReading(rawDataId, unit.id, reading);
			
			break;
		case 5:
			String firmware = String.format("%02d", data[1] & 0xff);
			firmware += "-" + String.format("%02d", data[2] & 0xff);
			firmware += "-" + String.format("%02d", data[3] & 0xff);
			firmware += " " + String.format("%02d", data[4] & 0xff);
			firmware += ":" + String.format("%02d", data[5] & 0xff);
			firmware += ":" + String.format("%02d", data[6] & 0xff);
			
			reading.firmware = firmware;
			log.debug("Firmware: " + firmware);

			try {
				int hour = Integer.parseInt(String.format("%02d", data[7] & 0xff));
				int minute = Integer.parseInt(String.format("%02d", data[8] & 0xff));
				int second = Integer.parseInt(String.format("%02d", data[9] & 0xff));

				Instant now = Instant.now();
				Instant unitTime = now.atZone(ZoneOffset.UTC)
											.withHour(hour)
											.withMinute(minute)
											.withSecond(second)
											.toInstant();
				long timeDiff = unitTime.getEpochSecond() - now.getEpochSecond();
			
				reading.timeDiff = timeDiff;
			} catch (Exception ex) {
				log.error("ERROR: Message Type = 5: Converting device time to valid time failed " + ex.getMessage());
				reading.timeDiff = 0;
			}

			// flags
			int flags5 = data[10] & 0xff;
			reading.binJustOn = ((flags5 & 0x01) == 0x01);
			reading.regularPeriodicReporting = ((flags5 & 0x02) == 0x02);
			reading.nbiotSimIssue = ((flags5 & 0x04) == 0x04);  // irrelevent for NB-IoT because will not be received - only useful for Sigfox

			// Byte 11 - hardwareRevision
			reading.hardwareRevision = data[11] & 0xff;
			
			idSize = data[12]; // note this is byte length
			
			if (data.length > 30) {
				// BriteBin Tcp Messages are 30 bytes or less
				throw new Exception("BriteBin messages are 30 bytes or less - this message is " + data.length + "bytes long");
			} else if (data.length < (13 + idSize)) {
				throw new Exception("There is not enough bytes in the message for a serialNo of size " + idSize + " bytes.");
			}
			reading.serialNo = "";
			for (int j = 0; j < idSize; j++) {
				reading.serialNo += Hex.ByteToHex(data[13 + j]);
			}
			
			log.debug("SerialNo (before): " + reading.serialNo);
			// Remove leading zero's from serialNo
			int j = 0;
			while (reading.serialNo.charAt(j) == '0' && j < reading.serialNo.length() - 1)
			    j++;
			reading.serialNo = reading.serialNo.substring(j).toUpperCase();
			log.debug("SerialNo (after): " + reading.serialNo);
			
	        unit = UnitDAL.getUnitBySerialNo(1, reading.serialNo);

	        reading.readingDateTime = Instant.now();
			
			log.info(reading);
			
			unitMsg = UnitDAL.saveReadingFirmware(rawDataId, unit.id, reading);
			
			break;
		default:
			throw new Exception("UnitServices.saveUnitReading: Unknown message type msgType: " + reading.msgType);
		}

		log.info("processBriteBinData - end");

		return unitMsg;
	}
		
	public UnitMessage saveUnitReading(byte[] data) throws Exception {
		try {
			log.info("UnitServices.saveUnitReading");
			
			UnitMessage unitMsg = new UnitMessage();
			
			// Save the raw data to the DB
			long rawDataId = UnitDAL.saveRawData(data);
			
			unitMsg = processBriteBinData(rawDataId, data);
						
			return unitMsg;
		}
		catch(SQLException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}
    
	public void markMessageAsSent(UnitMessage unitMsg) throws SQLException {
		UnitDAL.markMessageAsSent(unitMsg);
	}
}
