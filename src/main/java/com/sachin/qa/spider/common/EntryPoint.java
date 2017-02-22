package com.sachin.qa.spider.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.spider.Spider;
import com.sachin.qa.spider.SpiderConfig;
import com.sachin.qa.spider.SpiderConstants;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		System.setProperty("SiteAddress", "http://www.liptontea.com/");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		int numberOfCrawlers = Integer
				.parseInt(SpiderConstants.PROPERTIES.getProperty("crawler.numberOfCrawlers", "30"));
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
			SpiderConstants.CRAWLING_TIME = System.currentTimeMillis() - start;
		} catch (Exception e) {
			logger.debug("Error in controller", e);
			System.out.println("Error in application: " + e);
			SpiderConstants.ERROR = true;
			SpiderConstants.ERROR_TEXT = "URL is down, something went wrong or there is some error in faching URL data. Please review log for more detail. <br/> Error: "
					+ e.getMessage();
		}

		// if (null == System.getProperty("SiteAddress") ||
		// System.getProperty("SiteAddress").isEmpty()) {
		// try {
		// throw new SEOException("Site url is missing");
		// } catch (SEOException e) {
		// logger.error("Error in application: ", e);
		// return;
		// }
		// }
		// String red =
		// HttpRequestUtils.getRedirectedURL(System.getProperty("SiteAddress"),
		// System.getProperty("Username"), System.getProperty("Password"));
		// if (red != null) {
		// CrawlerConstants.SITE = red;
		// }
		// try {
		// if (HelperUtils
		// .getFluentResponse(CrawlerConstants.SITE, CrawlerConstants.USERNAME,
		// CrawlerConstants.PASSWORD)
		// .returnResponse().getStatusLine().getStatusCode() != 200) {
		// throw new Exception(
		// "URL is down, something went wrong or there is some error in faching
		// URL data. Please review log for more detail.");
		// }
		// List<String> suites = new ArrayList<>();
		// suites.add(HelperUtils.getResourceFile("testng.xml"));
		// TestNG testng = new TestNG();
		// testng.setTestSuites(suites);
		// testng.setAnnotationTransformer(new SEOTransformer());
		// testng.setUseDefaultListeners(false);
		// testng.setVerbose(0);
		// testng.run();
		// } catch (Exception e) {
		// logger.error("Error in application: ", e);
		// CrawlerConstants.ERROR = true;
		// CrawlerConstants.ERROR_TEXT = "URL is down, something went wrong or
		// there is some error in faching URL data. Please review log for more
		// detail. <br/> Error: "
		// + e.getMessage();
		// }
		// CrawlerConstants.SITE = System.getProperty("SiteAddress");
		// ExtentReporterNG.generateReport();
	}
}
