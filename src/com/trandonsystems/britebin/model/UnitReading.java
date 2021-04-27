package com.trandonsystems.britebin.model;

import java.time.Instant;

public class UnitReading {

	public long unitId;
	
	public String serialNo;
	
	// data
	public int msgType;
	
	// These are message type 1 values
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
	public boolean batteryOverTempLO;
	public boolean binLocked;
	public boolean binFull;
	public boolean binTilted;
	public boolean flapStuckOpen;

	public boolean serviceDoorClosed = false;   // This is set once after a service door stuck open
	public boolean serviceDoorOpen = false;

	public double rssi;  // Received Signal Strength Indicator
	public int src;		 // Sonic Result Code - used by tekelek
	public double snr;
	public int ber;
	public double rsrq;	// Reference Signal Received Quality
	public int rsrp; 	// Reference Signal Received Power
	
	
	// These are message type 5 values
	public String firmware;
	public long timeDiff;
	
	// flags
	public boolean binJustOn;
	public boolean regularPeriodicReporting;
	public boolean nbiotSimIssue;
	
	public int hardwareRevision;
	
	public Instant readingDateTime;
		
}
