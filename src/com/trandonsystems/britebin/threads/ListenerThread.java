package com.trandonsystems.britebin.threads;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.log4j.Logger;

import com.trandonsystems.britebin.model.UnitMessage;
import com.trandonsystems.britebin.services.UnitServices;

public class ListenerThread extends Thread {

	static Logger log = Logger.getLogger(ListenerThread.class);
    private Socket socket;
    UnitServices unitServices = new UnitServices();
    
    public ListenerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
        	log.debug("Start communication - Message received - processing");
        	
        	// Takes input from the client socket
        	DataInputStream input = new DataInputStream(socket.getInputStream());
        	// Writes on client socket
        	DataOutputStream output = new DataOutputStream(socket.getOutputStream());

    		// Receiving data from client
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		byte buffer[] = new byte[1024];
    		baos.write(buffer, 0, input.read(buffer));
    		
    		byte result[] = baos.toByteArray();
    		
//    		for (int i = 0; i < result.length; i++) {
//    			System.out.println(i + ":" + (result[i] & 0xFF));
//    		}
    		log.debug("Recieved from client (bytes): " + result + " Byte Size: " + result.length); 
    		String inStr = Arrays.toString(result);
    		log.debug("Recieved from client (numbers): " + inStr); 
    		
    		UnitMessage unitMsg = unitServices.saveUnitReading(result);
    		// BriteBin message must be xx length long
    		
    		// *************************************************************************************
    		// WARNING the length has not been checked
    		// *************************************************************************************
    		
    		if (result.length == 16) {
    			unitMsg = unitServices.saveUnitReading(result);
    		} else {
    			throw new Exception("Tekelek messages must be 140 bytes");
    		}
    		
    		// echoing back to client
    		if (unitMsg.replyMessage) {
    			output.write(unitMsg.message);
    			unitServices.markMessageAsSent(unitMsg);
    			log.debug("Message set to unitId: " + unitMsg.unitId + " messageId: " + unitMsg.messageId + "   Message (bytes): " + unitMsg.message + "    Message(numbers): " + Arrays.toString(unitMsg.message));
    		}
	    	
    		// Close the connection and the streams
            socket.close();
            input.close();
            output.close();

        } catch (IOException exIO) {
            log.error("Server exception: " + exIO.getMessage());
            exIO.printStackTrace();
    	} catch (SQLException exSQL) {
    		log.error("Server exception: " + exSQL.getMessage());
    		exSQL.printStackTrace();
        } catch (Exception ex) {
            log.error("Server exception: " + ex.getMessage());
            ex.printStackTrace();
    	} finally {
            log.info("Communication ended");            
    	}
    }
    
}
