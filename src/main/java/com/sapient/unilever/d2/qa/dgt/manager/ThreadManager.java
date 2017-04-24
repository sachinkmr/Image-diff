package com.sapient.unilever.d2.qa.dgt.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.selenium.Browser;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;
import com.sapient.unilever.d2.qa.dgt.site.MultiThreadedUrlHandler;
import com.sapient.unilever.d2.qa.dgt.site.UrlHandler;

public class ThreadManager {
	public static List<WebDriverManager> drivers;
	public static List<ExecutorService> services;
	public static ExecutorService dgtService;
	protected static final Logger logger = LoggerFactory.getLogger(ThreadManager.class);
	public static Map<String, Integer> URLs;
	private static int i = 0;
	private static boolean multiThreads;

	public static synchronized int getCount() {
		return ++i;
	}

	static {
		URLs = new ConcurrentHashMap<>();
		dgtService = Executors.newCachedThreadPool();
		drivers = new ArrayList<>(AppConstants.BROWSERS.size());
		services = new ArrayList<>(AppConstants.BROWSERS.size());
		for (Browser browser : AppConstants.BROWSERS) {
			try {
				switch (browser.getName().toLowerCase()) {
				case "chrome":
					WebDriverManager mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getChromeDriver();
					drivers.add(mngr);
					break;
				case "firefox":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getFireFoxDriver();
					drivers.add(mngr);
					break;
				case "ie":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getIEDriver();
					drivers.add(mngr);
					break;
				case "phantom":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getPhantomDriver();
					drivers.add(mngr);
					break;
				case "edge":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getEdgeDriver();
					drivers.add(mngr);
					break;
				case "iphone_with_chrome_emulation":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getiPhoneDriver();
					drivers.add(mngr);
					break;
				case "andriod_with_chrome_emulation":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getAndriodDriver();
					drivers.add(mngr);
					break;
				case "tablet_with_chrome_emulation":
					mngr = new WebDriverManager();
					mngr.setName(browser.getName());
					mngr.getiPadDriver();
					drivers.add(mngr);
					break;
				}
				services.add(Executors.newFixedThreadPool(1));
			} catch (Exception ex) {
				logger.error("Error in launching browser", ex);

			}
		}
	}

	public static void processUrl(String url, boolean multiThreaded) {
		multiThreads = multiThreaded;
		logger.info("Capturing URL: " + url);
		if (multiThreads) {
			// webDrivers = new ArrayList<>();
			for (int i = 0; i < services.size(); i++) {
				services.get(i).execute(new MultiThreadedUrlHandler(drivers.get(i).getName(), url));
			}
		} else {
			for (int i = 0; i < services.size(); i++) {
				services.get(i).execute(new UrlHandler(drivers.get(i), url));
			}
		}

	}

	public static void cleanup() {
		for (int i = 0; i < services.size(); i++) {
			services.get(i).shutdown();
			while (!services.get(i).isTerminated()) {
				logger.warn("Waiting 10 seconds for browsers to complete requests...");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			drivers.get(i).close();
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

	public static int pushURL(String pageUrl) {
		if (URLs.containsKey(pageUrl)) {
			return URLs.get(pageUrl);
		} else {
			int i = getCount();
			URLs.put(pageUrl, i);
			return i;
		}
	}

}
