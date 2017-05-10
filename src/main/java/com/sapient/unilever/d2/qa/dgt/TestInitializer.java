package com.sapient.unilever.d2.qa.dgt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;
import com.sapient.unilever.d2.qa.dgt.report.HTMLGenerator;
import com.sapient.unilever.d2.qa.dgt.spider.Spider;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderConfig;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class TestInitializer {
	protected static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

	@BeforeSuite(enabled = true)
	public void init() {

		AppConstants.START_TIME = System.currentTimeMillis();
	}

	@Test
	public void executeSuite() {
		System.out.println("Running Suite ");
		System.out.println("---------------------------------------");
		File file = new File(AppConstants.URL_TEXT);
		HTMLGenerator reporter = null;
		if (file.exists() && file.isFile()) {
			try {
				for (String url : FileUtils.readLines(file, "UTF-8")) {
					ThreadManager.processUrl(url);
				}
				ThreadManager.cleanup();
				System.out.println("\nGenerating Report ");
				System.out.println("---------------------------------------");
				reporter = new HTMLGenerator();
				reporter.generateImageReport();
				reporter.generateJSReport();
			} catch (IOException e) {
				logger.debug("Error in controller", e);
			}
		} else if (!AppConstants.SITE.isEmpty()) {
			int numberOfCrawlers = Integer
					.parseInt(AppConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "10"));
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
				System.out.println("\nGenerating Report ");
				System.out.println("---------------------------------------");
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

	@AfterSuite(enabled = true)
	public void afterSuite() {
		if (AppConstants.BUILD_TYPE == BuildType.PRE) {
			AppConstants.saveParam();
		}
		if (AppConstants.HOST != null) {
			try {
				AppConstants.HOST.remove(System.getProperty("HostName"));
				logger.info("Removeded " + System.getProperty("HostName") + " in etc/hosts file");
				AppConstants.HOST.close();
			} catch (Exception e) {
				logger.debug("Not able to clear host object", e);
			}
		}
		File file = new File(AppConstants.FOLDER).getParentFile();
		FileUtils.deleteQuietly(new File(file, "browsers.json"));
		FileUtils.deleteQuietly(new File(file, "Config.properties"));
		FileUtils.deleteQuietly(new File(file, "testng.xml"));
		FileUtils.deleteQuietly(new File("CrawlerConfigFile"));
		FileUtils.deleteQuietly(new File(System.getProperty("user.dir"), "crawler-data"));
	}
}
