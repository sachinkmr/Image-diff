package com.sachin.qa.spider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sachin.qa.app.AppConstants;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SpiderController extends CrawlController {
    private Executor executor;

    public Executor getExecutor() {
	return this.executor;
    }

    public SpiderController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer)
	    throws Exception {
	super(config, pageFetcher, robotstxtServer);
	executor= Executors.newFixedThreadPool(AppConstants.BROWSER_INSTANCE); 
    }
    

}
