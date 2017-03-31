package com.sachin.qa.app;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.manager.ThreadManager;
import com.sachin.qa.spider.Spider;
import com.sachin.qa.spider.SpiderConfig;
import com.sachin.qa.spider.SpiderController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EntryPoint {
    protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
	System.setProperty("BrandName", "Sunsilk");
	System.setProperty("BuildVersion", "2.17");
	// System.setProperty("SiteAddress", "http://www.dove.com/");
//	System.setProperty("Username", "axed2stage");
//	System.setProperty("Password", "S@pient123");
	System.setProperty("UrlsTextFile", "D:\\DoveUrls.txt");
	System.setProperty("imageDiff", "no");
	System.setProperty("jsDiff", "no");
	System.setProperty("htmlDiff", "No");
	System.setProperty("BuildType", "pre");
	// System.setProperty("PreBuildVersion", "2.18.1");
	// System.setProperty("PreBuildTime", "29-March-2017_12-32PM");
	// HelperUtils.validate();
	File file = new File(AppConstants.URL_TEXT);
	if (file.exists() && file.isFile()) {
	    try {
		for (String url : FileUtils.readLines(file, "UTF-8")) {
		    ThreadManager.processUrl(url);
		}
		// StreamUtils.generateReportCSV();
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
	    long start = System.currentTimeMillis();
	    try {
		System.out.println("Please wait crawling site....");
		SpiderController controller = new SpiderController(config, pageFetcher, robotstxtServer);
		controller.start(Spider.class, numberOfCrawlers);
		AppConstants.CRAWLING_TIME = System.currentTimeMillis() - start;
		// StreamUtils.generateReportCSV();
	    } catch (Exception e) {
		logger.debug("Error in controller", e);
		System.out.println("Error in application: " + e);
		AppConstants.ERROR = true;
		AppConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
			+ e.getMessage();
	    }
	}
	ThreadManager.cleanup();
    }
}
