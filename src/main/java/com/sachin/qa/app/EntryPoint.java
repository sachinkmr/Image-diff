package com.sachin.qa.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.selenium.Browser;
import com.sachin.qa.spider.Spider;
import com.sachin.qa.spider.SpiderConfig;
import com.sachin.qa.spider.SpiderController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		System.setProperty("SiteAddress", "http://www.liptontea.com/");
		for (Browser browserType : AppConstants.BROWSERS) {
			System.out.println(browserType.getName());
		}
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		int numberOfCrawlers = Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
		SpiderConfig config = new SpiderConfig().getConfig();
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		long start = System.currentTimeMillis();
		try {
			System.out.println("Please wait crawling site....");
			SpiderController controller = new SpiderController(config, pageFetcher, robotstxtServer);
			controller.start(Spider.class, numberOfCrawlers);
			AppConstants.CRAWLING_TIME = System.currentTimeMillis() - start;

		} catch (Exception e) {
			logger.debug("Error in controller", e);
			System.out.println("Error in application: " + e);
			AppConstants.ERROR = true;
			AppConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
					+ e.getMessage();
		}
	}
}
