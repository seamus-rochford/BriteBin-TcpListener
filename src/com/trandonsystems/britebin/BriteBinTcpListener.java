package com.trandonsystems.britebin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.trandonsystems.britebin.threads.ListenerThread;
import com.trandonsystems.britebin.utils.Util;

public class BriteBinTcpListener {

	static Logger log = Logger.getLogger(BriteBinTcpListener.class);

	public static void main(String[] args) {

        log.info("Server started ... ");

		int port = Util.britebinTcpPort;
		log.info("Port : " + port);
		
//		UtilDAL.envName = System.getenv("ENV_NAME");
//		UtilDAL.envName = "PROD";
//		log.info("ENV_NAME: " + UtilDAL.envName);
		
        try (ServerSocket serverSocket = new ServerSocket(port)) {
        	 
            log.info("Server is listening on port " + port);
 
            while (true) {

            	Socket socket = serverSocket.accept();
                log.info("New client connected");
                log.info("Client Ip: " + ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().toString());
 
                new ListenerThread(socket).start();
            }
 
        } catch (IOException ex) {
            log.error("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }		

        log.info(" ... Server Terminated ");
	}

}
