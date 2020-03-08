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
    
}
