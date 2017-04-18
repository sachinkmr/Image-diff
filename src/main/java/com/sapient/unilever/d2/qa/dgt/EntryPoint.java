package com.sapient.unilever.d2.qa.dgt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;
import com.sapient.unilever.d2.qa.dgt.report.HTMLGenerator;
import com.sapient.unilever.d2.qa.dgt.spider.Spider;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderConfig;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		// System.setProperty("BuildType", "Pre");
		// System.setProperty("BrandName", "Dove");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		// System.setProperty("UrlsTextFile", "d:\\DoveUrls.txt");
		// System.setProperty("SiteAddress",
		// "http://d2showcase.unileversolutions.com/us/en/home.html");

		// System.setProperty("imageDiff", "yes");
		// System.setProperty("jsDiff", "yes");
		// System.setProperty("htmlDiff", "No");

		// System.setProperty("PreBuildVersion", "2.18.1");
		// System.setProperty("PreBuildTime", "18-April-2017_03-47PM");
		// HelperUtils.validate();

		AppConstants.START_TIME = System.currentTimeMillis();
		File file = new File(AppConstants.URL_TEXT);
		HTMLGenerator reporter = null;
		if (file.exists() && file.isFile()) {
			try {
				for (String url : FileUtils.readLines(file, "UTF-8")) {
					ThreadManager.processUrl(url);
				}
				ThreadManager.cleanup();
				// CSVReporter.generateReportAsCSV();
				reporter = new HTMLGenerator();
				reporter.generateImageReport();
				reporter.generateJSReport();

			} catch (IOException e) {
				logger.debug("Error in controller", e);
			}
		} else if (!AppConstants.SITE.isEmpty()) {
			int numberOfCrawlers = Integer
					.parseInt(AppConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
			SpiderConfig config = new SpiderConfig().getConfig();
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			robotstxtConfig.setEnabled(false);
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

			try {
				System.out.println("Please wait crawling site....");
				SpiderController controller = new SpiderController(config, pageFetcher, robotstxtServer);
				controller.start(Spider.class, numberOfCrawlers);
				ThreadManager.cleanup();
				// CSVReporter.generateReportAsCSV();
				reporter = new HTMLGenerator();
				reporter.generateImageReport();
				reporter.generateJSReport();
			} catch (Exception e) {
				logger.debug("Error in controller", e);
				System.out.println("Error in application: " + e);
				AppConstants.ERROR = true;
				AppConstants.ERROR_TEXT = "Something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
						+ e.getMessage();
			}
		}
	}
}
