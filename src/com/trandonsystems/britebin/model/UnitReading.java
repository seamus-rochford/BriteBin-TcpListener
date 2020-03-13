package com.trandonsystems.britebin.model;

import java.time.Instant;

public class UnitReading {

	public long unitId;
	
	public String serialNo;
	
	// data
	public int msgType;
	
	public int binLevel;		// unsigned
	public int binLevelBC;		// unsigned
	public int noFlapOpening;	// unsigned
	public int batteryVoltage;	// unsigned
	public int temperature;		// *** signed ***
	public int noCompactions;	// unsigned
	
	// Signal strengths
	public int nbIoTSignalStrength;
	
	//flags
	public boolean batteryUVLO;
	public boolean binEmptiedLastPeriod;
	public boolean overUnderTempLO;
	public boolean binLocked;
	public boolean binFull;
	public boolean binTilted;
	public boolean serviceDoorOpen;
	public boolean flapStuckOpen;

	public int rssi;
	public int src;
	public int snr;
	public int ber;
	
	public Instant readingDateTime;
		
}
