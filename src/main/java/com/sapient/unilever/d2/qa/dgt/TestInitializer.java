package com.sapient.unilever.d2.qa.dgt;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.sapient.unilever.d2.qa.dgt.spider.Spider;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderConfig;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class TestInitializer {
	protected static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

	@BeforeSuite(enabled = true)
	public void init() {
		int numberOfCrawlers = Integer
				.parseInt(AppConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
		SpiderConfig config = new SpiderConfig().getConfig();
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		long start = System.currentTimeMillis();
		try {
			System.out.println("Please wait crawling site....");
			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.start(Spider.class, numberOfCrawlers);
			AppConstants.CRAWLING_TIME = System.currentTimeMillis() - start;
		} catch (Exception e) {
			logger.debug("Error in controller", e);
			System.out.println("Error in application: " + e);
			AppConstants.ERROR = true;
			AppConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
					+ e.getMessage();
		}
		System.out.println("\nExecuting Test Cases");
		System.out.println("---------------------------------------");
	}

	@AfterSuite(enabled = true)
	public void afterSuite() {
		FileUtils.deleteQuietly(new File("CrawlerConfigFile"));
	}
}
