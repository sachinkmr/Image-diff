package com.sapient.unilever.d2.qa.dgt.spider;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;
import com.sapient.unilever.d2.qa.dgt.page.D2Page;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
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
				if (page.getParseData() instanceof HtmlParseData) {
					HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
					String html = htmlParseData.getHtml();
					D2Page d2page = new D2Page(html, true);
					d2page.setSite(page.getWebURL().getURL());
					if (AppConstants.PAGES.add(d2page)) {
						ThreadManager.processUrl(page.getWebURL().getURL(), false);
					}
				}
			} catch (Exception e) {
			}
		}

	}

}
