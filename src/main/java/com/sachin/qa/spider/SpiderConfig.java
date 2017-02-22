package com.sachin.qa.spider;

import java.net.MalformedURLException;

import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;

public class SpiderConfig extends CrawlConfig {

	public SpiderConfig getConfig() {
		this.setSiteUrl(SpiderConstants.SITE);
		if (null != SpiderConstants.USERNAME && !SpiderConstants.USERNAME.isEmpty()) {
			try {
				this.addAuthInfo(
						new BasicAuthInfo(SpiderConstants.USERNAME, SpiderConstants.PASSWORD, SpiderConstants.SITE));
			} catch (MalformedURLException e) {
				LoggerFactory.getLogger(SpiderConfig.class).debug("Error in controller", e);
			}
		}
		this.setUserAgentString(SpiderConstants.USER_AGENT);
		this.setUserAgentString(SpiderConstants.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));
		this.setCrawlStorageFolder(SpiderConstants.CRAWL_STORAGE_FOLDER);
		this.setConnectionTimeout(
				Integer.parseInt(SpiderConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setSocketTimeout(
				Integer.parseInt(SpiderConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setFollowRedirects(
				Boolean.parseBoolean(SpiderConstants.PROPERTIES.getProperty("crawler.followRedirects", "true")));
		this.setPolitenessDelay(Integer.parseInt(SpiderConstants.PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		this.setProcessBinaryContentInCrawling(false);
		this.setIncludeBinaryContentInCrawling(
				Boolean.parseBoolean(SpiderConstants.PROPERTIES.getProperty("crawler.binaryContent", "true")));
		this.setIncludeHttpsPages(true);
		this.setMaxDownloadSize(Integer.parseInt(SpiderConstants.PROPERTIES.getProperty("crawler.maxDownloadSize",
				Integer.toString(Integer.MAX_VALUE))));
		return this;
	}

}
