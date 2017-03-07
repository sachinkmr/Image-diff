package com.sachin.qa.spider;

import java.io.File;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.D2Page;
import com.sachin.qa.app.ThreadManager;
import com.sachin.qa.app.utils.StreamUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Spider extends WebCrawler {
    private StreamUtils streamUtils = new StreamUtils();
    protected static final Logger logger = LoggerFactory.getLogger(Spider.class);

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
	Matcher m = AppConstants.SHOULD_VISIT_PATTERN.matcher(url.getURL());
	return url.isInternalLink() && !AppConstants.SKIPPED_URLS.contains(url.getModifiedHost()) && m.find()
		&& !AppConstants.ASSETS_PATTERN.matcher(url.getURL()).find();
    }

    @Override
    public void visit(Page page) {
	File file = new File(AppConstants.CRAWLER_DATA, page.getWebURL().hashCode() + ".webUrl");
	if (page.getWebURL().isInternalLink() && page.getStatusCode() == 200
		&& page.getContentType().contains("text/html")) {
	    try {
		D2Page p = new D2Page(page.getWebURL().getURL());
		p.setTemplate(((HtmlParseData) page.getParseData()).getHtml());
		if (!AppConstants.d2Pages.contains(p)) {
		    AppConstants.d2Pages.add(p);
		    ThreadManager.manageThreads(p.getUrl(), (SpiderController) this.getMyController());
		}
	    } catch (Exception e) {
	    }
	    streamUtils.savePage(file, page);
	}
	
    }

}
