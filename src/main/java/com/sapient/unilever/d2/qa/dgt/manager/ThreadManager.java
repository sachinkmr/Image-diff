package com.sapient.unilever.d2.qa.dgt.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.selenium.Browser;

public class ThreadManager {
	public static ExecutorService dgtService;
	public static List<URLsManager> urlsManagers;
	protected static final Logger logger = LoggerFactory.getLogger(ThreadManager.class);

	static {
		urlsManagers = new ArrayList<>(AppConstants.BROWSERS.size());
		dgtService = Executors.newFixedThreadPool(10);
		for (Browser browser : AppConstants.BROWSERS) {
			urlsManagers.add(new URLsManager(browser.getName()));
		}
	}

	public static void processUrl(String url) {
		logger.info("Capturing URL: " + url);
		for (URLsManager manager : urlsManagers) {
			manager.manageURL(url);
		}
	}

	public static void cleanup() {
		for (URLsManager manager : urlsManagers) {
			try {
				manager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dgtService.shutdown();
		while (!dgtService.isTerminated()) {
			logger.warn("Waiting 5 seconds for threads to complete requests...");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
