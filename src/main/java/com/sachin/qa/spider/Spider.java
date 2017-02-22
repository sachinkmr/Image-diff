package com.sachin.qa.spider;

import java.io.File;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.spider.helpers.StreamUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class Spider extends WebCrawler {
	private StreamUtils streamUtils = new StreamUtils();
	protected static final Logger logger = LoggerFactory.getLogger(Spider.class);

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		Matcher m = SpiderConstants.SHOULD_VISIT_PATTERN.matcher(url.getURL());
		return !SpiderConstants.SKIPPED_URLS.contains(url.getModifiedHost())
				&& (m.find() || SpiderConstants.ASSETS_PATTERN.matcher(url.getURL()).find());
	}

	@Override
	public void visit(Page page) {
		File file = new File(SpiderConstants.DATA_LOCATION, page.getWebURL().hashCode() + ".webUrl");
		if (page.getWebURL().isInternalLink() && page.getStatusCode() == 200
				&& page.getContentType().contains("text/html")) {
			streamUtils.savePage(file, page);
		}
		// SEOPage seoPage = new SEOPage(page);
		// streamUtils.writeFile(file, seoPage);
		// try {
		// if (page.getWebURL().isInternalLink() && page.getStatusCode() == 200
		// && page.getContentType().contains("text/html")) {
		//
		// SEOCrawlerConfig config = (SEOCrawlerConfig)
		// this.getMyController().getConfig();
		// if (CrawlerConstants.TESTS.contains("pageSpeed")) {
		// config.getExecutor().execute(new
		// PageSpeed(page.getWebURL().getURL()));
		// }
		// if (CrawlerConstants.TESTS.contains("structuredData")) {
		// config.getExecutor().execute(new
		// StructuredData(page.getWebURL().getURL(), seoPage.getHtml()));
		// }
		// }
		// } catch (Exception ex) {
		// logger.debug("3rd party api error for url: " +
		// page.getWebURL().getURL(), ex);
		// }
	}

}
