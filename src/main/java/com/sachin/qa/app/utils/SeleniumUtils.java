package com.sachin.qa.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.selenium.ProcessKiller;

public class SeleniumUtils {
	protected static final Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

	/**
	 * This method is used to kill all the processes running of servers if any
	 * is running as system processes.
	 *
	 * @author Sachin
	 **/
	public static void killService(String serviceName) {
		try {
			if (ProcessKiller.isProcessRunning(serviceName)) {
				ProcessKiller.killProcess(serviceName);
			}
		} catch (Exception ex) {
			logger.warn("Unable to close server: " + serviceName, ex);
		}
	}
}
