package com.sapient.unilever.d2.qa.dgt;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;
import com.sapient.unilever.d2.qa.dgt.spider.Spider;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderConfig;
import com.sapient.unilever.d2.qa.dgt.spider.SpiderController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EntryPoint {
    protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {

	System.setProperty("BrandName", "Dove");
	// System.setProperty("BuildVersion", "2.17");
	// System.setProperty("SiteAddress",
	// "http://www.axe.com/us/en/home.html");
	// System.setProperty("Username", "axed2stage");
	// System.setProperty("Password", "S@pient123");
	System.setProperty("UrlsTextFile", "D:\\DoveUrls.txt");
	System.setProperty("imageDiff", "no");
	System.setProperty("jsDiff", "no");
	System.setProperty("htmlDiff", "No");
	System.setProperty("BuildType", "pre");
	System.setProperty("web", "false");
	// System.setProperty("PreBuildVersion", "2.18.1");
	// System.setProperty("PreBuildTime", "03-April-2017_01-42PM");
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
		AppConstants.ERROR_TEXT = "Something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
			+ e.getMessage();
	    }
	}
	ThreadManager.cleanup();

    }
}