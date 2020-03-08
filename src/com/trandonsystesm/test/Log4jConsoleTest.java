package com.trandonsystesm.test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This is a Java console app that you can execute within your IDE for visual
 * confirmation that the log4j system is appending to console and/or file as
 * specified in log4j config (i.e. resources/log4j.xml). It is also useful for a
 * quick visual test when you change the pattern of log statements via
 * ConversionPattern in log4j.xml.
 * 
 * <p>
 * Right-click Log4jConsoleTest.java and either select Run As > Java Application
 * or press ALT+SHIFT+X,J.
 * </p>
 * 
 * <p>
 * As a result, you should typically see test messages from all log levels
 * (TRACE, DEBUG, INFO, WARN, ERROR, FATAL) both on the console view of your IDE
 * and in any log files created by file appenders defined in log4j.xml.
 * </p>
 * 
 * @author Cody Burleson
 */
public class Log4jConsoleTest {
	
	static final Logger log = Logger.getLogger(Log4jConsoleTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log4jConsoleTest console = new Log4jConsoleTest();
		console.execute();
	}

	public Log4jConsoleTest() {
	}

	public void execute() {
		
		if (log.isTraceEnabled()) {
			log.trace("Test: TRACE level message.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Test: DEBUG level message.");
		}
		if (log.isInfoEnabled()) {
			log.info("Test: INFO level message.");
		}
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("Test: WARN level message.");
		}
		if (log.isEnabledFor(Level.ERROR)) {
			log.error("Test: ERROR level message.");
		}
		if (log.isEnabledFor(Level.FATAL)) {
			log.fatal("Test: FATAL level message.");
		}
	}

}