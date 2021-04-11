package com.trandonsystems.britebin.services;

public class Hex {

    public static int HexToInt(String hexStr)
    {
        hexStr = hexStr.toUpperCase();

        int result = 0;
        for (int i = 0; i < hexStr.length(); i++)
        {
            char hexChar = hexStr.charAt(i);
            result = result * 16
                            + ((int)hexChar < (int)'A' ?
                                ((int)hexChar - (int)'0') :
                                10 + ((int)hexChar - (int)'A'));
        }
        return result;
    }


    public static String ByteToHex(byte inData)
    {
        return Integer.toHexString(((((int)inData) & 240) >> 4)) + Integer.toHexString((((int)inData) & 15));
    }	

    
    public static String ByteArrayToHex(byte[] inData)
    {
    	String hexStr = "";
    	for (int i = 0; i < inData.length; i++) {
    		byte singleByte = inData[i];
    		hexStr = hexStr + ByteToHex(singleByte);
    	}
        return hexStr;
    }	

    
    public static byte[] hexStringToByteArray(String s) {
        byte[] data = new byte[s.length()/2];
        for (int i = 0; i < data.length; i ++) {
            data[i] = (byte) ((Character.digit(s.charAt(i*2), 16) << 4)
                    + Character.digit(s.charAt(i*2 + 1), 16));
        }
        return data;
    }
    
}
