package com.bim.base;

import org.apache.log4j.Logger;
public class BaseClass {
	
	protected Logger logger = Logger.getLogger(getClass());
	
	public void debug(String message) {
		logger.debug(message);
	}
	public void error(String message) {
		logger.error(message);
	}
	public void info(String message) {
		logger.info(message);
	}
}
