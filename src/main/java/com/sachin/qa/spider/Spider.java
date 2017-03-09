package com.sachin.qa.spider;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.manager.ThreadManager;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class Spider extends WebCrawler {
	protected static final Logger logger = LoggerFactory.getLogger(Spider.class);

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		Matcher m = AppConstants.SHOULD_VISIT_PATTERN.matcher(url.getURL());
		return url.isInternalLink() && !AppConstants.SKIPPED_URLS.contains(url.getModifiedHost()) && m.find()
				&& !AppConstants.ASSETS_PATTERN.matcher(url.getURL()).find();
	}

	@Override
	public void visit(Page page) {
		if (page.getWebURL().isInternalLink() && page.getStatusCode() == 200
				&& page.getContentType().contains("text/html")) {
			try {
				ThreadManager.processUrl(page.getWebURL().getURL());
			} catch (Exception e) {
			}
		}

	}

}
