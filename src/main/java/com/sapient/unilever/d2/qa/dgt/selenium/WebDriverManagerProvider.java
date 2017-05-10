package com.sapient.unilever.d2.qa.dgt.selenium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverManagerProvider {
	protected static final Logger logger = LoggerFactory.getLogger(WebDriverManagerProvider.class);

	public static WebDriverManager getWebDrivermanager(String browserName) throws Exception {
		WebDriverManager mngr = new WebDriverManager();
		try {
			switch (browserName.toLowerCase()) {
			case "chrome":
				mngr.setName(browserName);
				mngr.getChromeDriver();
				break;
			case "firefox":
				mngr.setName(browserName);
				mngr.getFireFoxDriver();
				break;
			case "ie":
				mngr.setName(browserName);
				mngr.getIEDriver();
				break;
			case "phantom":
				mngr.setName(browserName);
				mngr.getPhantomDriver();
				break;
			case "edge":
				mngr.setName(browserName);
				mngr.getEdgeDriver();
				break;
			case "iphone_with_chrome_emulation":
				mngr.setName(browserName);
				mngr.getiPhoneDriver();
				break;
			case "andriod_with_chrome_emulation":
				mngr.setName(browserName);
				mngr.getAndriodDriver();
				break;
			case "tablet_with_chrome_emulation":
				mngr.setName(browserName);
				mngr.getiPadDriver();
				break;
			}
		} catch (Exception ex) {
			throw new Exception("Error in launching browser", ex);
		}
		return mngr;
	}
}
