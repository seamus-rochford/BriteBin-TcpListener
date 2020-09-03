package com.trandonsystems.britebin.model;

public class Unit {

	public int id;
	public String serialNo;
	public String location;

	// firmware values
	public String firmware;
	public long timeDiff;
	
	// flags
	public boolean binJustOn;
	public boolean regularPeriodicReporting;
	public boolean nbiotSimIssue;
	
}
