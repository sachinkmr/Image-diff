package com.sapient.unilever.d2.qa.dgt.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManagerProvider;
import com.sapient.unilever.d2.qa.dgt.site.UrlHandler;

public class URLsManager implements AutoCloseable {
	protected static final Logger logger = LoggerFactory.getLogger(URLsManager.class);

	private final List<WebDriverManager> managers;
	private final ExecutorService services;
	private int i;
	private String browserName;

	public URLsManager(String browserName) {
		this.browserName = browserName;
		if (AppConstants.MULTI_THREADED) {
			managers = new ArrayList<>(AppConstants.BROWSER_INSTANCE);
		} else {
			managers = new ArrayList<>(1);
		}
		services = Executors.newFixedThreadPool(managers.size());
	}

	/**
	 * @param url
	 */
	public void manageURL(String url) {
		if (managers.size() < AppConstants.BROWSER_INSTANCE) {
			try {
				managers.add(WebDriverManagerProvider.getWebDrivermanager(browserName));
			} catch (Exception e) {
				logger.error("Unable to launch browser", e);
			}
		}
		services.execute(new UrlHandler(managers.get(i++ % managers.size()), url));
	}

	public List<WebDriverManager> getManagers() {
		return managers;
	}

	public ExecutorService getServices() {
		return services;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		services.shutdown();
		while (!services.isTerminated()) {
			logger.warn("Waiting 10 seconds for browsers to complete requests...");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (WebDriverManager manager : managers) {
			manager.close();
		}
	}

	// public synchronized int getCounter() {
	// return i++;
	// }

}
