package com.trandonsystems.test;

import com.trandonsystems.britebin.model.UnitMessage;
import com.trandonsystems.britebin.services.UnitServices;

public class Test {

	private static void TestSaveUnitReading() {
	
//		byte[] data = {(byte)0x01, // Message Type
//				(byte)0x3D, // Bin Level
//				(byte)0xA4, // Bin Level BC
//				(byte)0x12, // Flap openings 
//				(byte)0xCA,  
//				(byte)0x21, // Battery Voltage
//				(byte)0x12, // Temp.
//				(byte)0x86, // NoCompactions
//				(byte)0xFF, // Flags All set
//				(byte)0x9B, // Signal Strengths
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x04, // Serial No length
//				(byte)0xAC, // Serial Number
//				(byte)0x2B, 
//				(byte)0xE7, 
//				(byte)0xB6, 
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00,
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00, 
//				(byte)0x00,
//				(byte)0x00, 
//				(byte)0x00};
		// msg: 01284800554466345A760000
		// msg: 0180171000631A070800000004009E51F1
		byte[] data = {(byte)0x01, // Message Type
				(byte)0x80, // Bin Level
				(byte)0x17, // Bin Level BC
				(byte)0x10, // Flap openings 
				(byte)0x00,  
				(byte)0x63, // Battery Voltage
				(byte)0x1A, // Temp.
				(byte)0x07, // NoCompactions
				(byte)0x08, // Flags 
				(byte)0x00, // Signal Strengths
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x04, // Serial No length
				(byte)0x00, // Serial Number
				(byte)0x9E, 
				(byte)0x51, 
				(byte)0xF1, 
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00,
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00, 
				(byte)0x00,
				(byte)0x00, 
				(byte)0x00};
		
		UnitServices unitServices = new UnitServices();
		
		try {
			UnitMessage unitMsg = unitServices.saveUnitReading(data);
			System.out.println(unitMsg);
		}
		catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestSaveUnitReading();
	}

}
