package com.sachin.qa.spider;

import java.net.MalformedURLException;

import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;

public class SpiderConfig extends CrawlConfig {

	public SpiderConfig getConfig() {
		this.setSiteUrl(AppConstants.SITE);
		if (null != AppConstants.USERNAME && !AppConstants.USERNAME.isEmpty()) {
			try {
				this.addAuthInfo(new BasicAuthInfo(AppConstants.USERNAME, AppConstants.PASSWORD, AppConstants.SITE));
			} catch (MalformedURLException e) {
				LoggerFactory.getLogger(SpiderConfig.class).debug("Error in controller", e);
			}
		}
		this.setUserAgentString(AppConstants.USER_AGENT);
		this.setUserAgentString(AppConstants.PROPERTIES.getProperty("crawler.userAgentString",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0"));
		this.setConnectionTimeout(
				Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setSocketTimeout(
				Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.connectionTimeout", "120000")));
		this.setFollowRedirects(
				Boolean.parseBoolean(AppConstants.PROPERTIES.getProperty("crawler.followRedirects", "true")));
		this.setPolitenessDelay(Integer.parseInt(AppConstants.PROPERTIES.getProperty("crawler.URLHitDelay", "200")));
		this.setProcessBinaryContentInCrawling(false);
		this.setCrawlStorageFolder(AppConstants.CRAWLER_DATA);
		this.setIncludeBinaryContentInCrawling(false);
		this.setIncludeHttpsPages(true);
		this.setMaxDownloadSize(Integer.parseInt(
				AppConstants.PROPERTIES.getProperty("crawler.maxDownloadSize", Integer.toString(Integer.MAX_VALUE))));
		return this;
	}

}
