package com.trandonsystems.britebin.model;

public class UnitTCPMessage {

	// data
	public int msgType;	
	
	// Message Type 2 values
	public byte compactionPeriod;
	public byte batteryUVLOThreshold;;
	public byte overTemperatureThreshold;
	public byte underTemperatureThreshold;
	public byte binCompactionThreshold;
	public byte binFullLevel;
	
	// Message Type 3 values
	public byte timezone;						// (N-12 = PCB Timezone; Example GMT-2 would be 10)
	public byte setNightModeEnterTime;			// (N*10 minutes. Example 5AM would be 30; 5PM would be 102)
	public byte setNightModeExitTime;			// (N*10 minutes) - Note: (Night Mode period needs to be longer than one Compaction period)

	//flags - these are sent with both Message TYpe 2 & 3
	public boolean lockBin;
	public boolean unlockBin;
	public boolean resetCompactCounter;
	public boolean softwareResetMCU;
	public boolean forceTimeSyncNTPServer;

}
