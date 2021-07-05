package com.trandonsystems.britebin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class EnvUtil {

	final static Logger LOGGER = Logger.getLogger(EnvUtil.class);
	final static String ENVIRONMENT_PROPERTIES_PATH = "/environment/environment.properties";
	final static Properties ENVIRONMENT_PROPERTIES = new Properties();

	static {
		LOGGER.info("ENVIRONMENT_PROPERTIES_PATH: " + ENVIRONMENT_PROPERTIES_PATH);
		try (InputStream inputStream = (EnvUtil.class).getResourceAsStream(ENVIRONMENT_PROPERTIES_PATH)) {
			ENVIRONMENT_PROPERTIES.load(inputStream);
		} catch(IOException ex) {
			LOGGER.error("While loading environment properties: " + ex.getMessage(), ex);
		}
		LOGGER.trace("Environment: " + ENVIRONMENT_PROPERTIES.getProperty("env.name"));
	}
	
	public static String get(String key) {
		String value = ENVIRONMENT_PROPERTIES.getProperty(key);
		if (value == null) {
			LOGGER.warn("No environment property exists with key: " + key);
		}

		return value;
	}
}
