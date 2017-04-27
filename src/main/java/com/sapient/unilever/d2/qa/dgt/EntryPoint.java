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

		// System.setProperty("BuildType", "Post");
		// System.setProperty("BrandName", "D2 Showcase");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		// System.setProperty("UrlsTextFile", "d:\\DoveUrls.txt");
		// System.setProperty("SiteAddress",
		// "https://www.knorr.com/br/home.html");

		// System.setProperty("imageDiff", "yes");
		// System.setProperty("jsDiff", "yes");
		// System.setProperty("htmlDiff", "No");
		//
		// System.setProperty("PreBuildVersion", "2.19.0.3");
		// System.setProperty("PreBuildTime", "27-April-2017_10-25AM");
		// HelperUtils.validate();

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

		// AppConstants.START_TIME = System.currentTimeMillis();
		// File file = new File(AppConstants.URL_TEXT);
		// HTMLGenerator reporter = null;
		// if (file.exists() && file.isFile()) {
		// try {
		// for (String url : FileUtils.readLines(file, "UTF-8")) {
		// ThreadManager.processUrl(url, false);
		// }
		// ThreadManager.cleanup();
		// // CSVReporter.generateReportAsCSV();
		// reporter = new HTMLGenerator();
		// reporter.generateImageReport();
		// reporter.generateJSReport();
		//
		// } catch (IOException e) {
		// logger.debug("Error in controller", e);
		// }
		// } else if (!AppConstants.SITE.isEmpty()) {
		// int numberOfCrawlers = Integer
		// .parseInt(AppConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers",
		// "30"));
		// SpiderConfig config = new SpiderConfig().getConfig();
		// PageFetcher pageFetcher = new PageFetcher(config);
		// RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		// robotstxtConfig.setEnabled(false);
		// RobotstxtServer robotstxtServer = new
		// RobotstxtServer(robotstxtConfig, pageFetcher);
		//
		// try {
		// System.out.println("Please wait crawling site....");
		// SpiderController controller = new SpiderController(config,
		// pageFetcher, robotstxtServer);
		// controller.start(Spider.class, numberOfCrawlers);
		// ThreadManager.cleanup();
		// // CSVReporter.generateReportAsCSV();
		// reporter = new HTMLGenerator();
		// reporter.generateImageReport();
		// reporter.generateJSReport();
		// } catch (Exception e) {
		// logger.debug("Error in controller", e);
		// System.out.println("Error in application: " + e);
		// AppConstants.ERROR = true;
		// AppConstants.ERROR_TEXT = "Something went wrong or there is some
		// error in faching URL data. Please review log for more detail. <br/>
		// Error: "
		// + e.getMessage();
		// }
		// }
		// FileUtils.deleteQuietly(new File(System.getProperty("user.dir"),
		// "crawler-data"));
	}
}
