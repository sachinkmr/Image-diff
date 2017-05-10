package com.sapient.unilever.d2.qa.dgt;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import com.sapient.unilever.d2.qa.dgt.utils.HelperUtils;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {

		System.setProperty("BuildType", "pre");
		System.setProperty("BrandName", "D2 Test");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		// System.setProperty("UrlsTextFile", "d:\\DoveUrls.txt");
		System.setProperty("SiteAddress", "https://www.suredeodorant.co.uk/home.html");

		System.setProperty("imageDiff", "yes");
		System.setProperty("jsDiff", "yes");
		// System.setProperty("htmlDiff", "No");

		// System.setProperty("HostName", "www.knorr.com");
		// System.setProperty("HostIP", "52.29.239.245");

		// System.setProperty("PreBuildVersion", "2.19.0.3");
		// System.setProperty("PreBuildTime", "04-May-2017_02-35PM");

		try {
			List<String> suites = new ArrayList<>();
			suites.add(HelperUtils.getResourceFile("testng.xml"));
			TestNG testng = new TestNG();
			testng.setTestSuites(suites);
			testng.setUseDefaultListeners(false);
			testng.setVerbose(0);
			testng.run();
		} catch (Exception e) {
			logger.error("Error in application: ", e);
			AppConstants.ERROR = true;
			AppConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
					+ e.getMessage();
		}

	}
}
