package com.sapient.unilever.d2.qa.dgt.site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManagerProvider;

public class MultiThreadedUrlHandler implements Runnable {
	public static ExecutorService services;
	public List<WebDriverManager> drivers;
	private String url;
	static {
		services = Executors.newFixedThreadPool(AppConstants.BROWSER_INSTANCE);
	}

	public MultiThreadedUrlHandler(String browserName, String url) {
		this.url = url;
		drivers = new ArrayList<>(AppConstants.BROWSER_INSTANCE);
		for (int i = 0; i < AppConstants.BROWSER_INSTANCE; i++) {
			drivers.add(WebDriverManagerProvider.getWebDrivermanager(browserName));
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < AppConstants.BROWSER_INSTANCE; i++) {

		}
	}

}
