package com.sachin.qa.app.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.BuildType;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.app.utils.StreamUtils;
import com.sachin.qa.page.DifferenceType;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.console.ConsoleType;
import com.sachin.qa.page.diff.DiffGenerator;
import com.sachin.qa.page.image.ImageType;

public class UrlHandler implements Runnable {
	private WebDriverManager webDriverManager;
	private PageInfo pageInfo;
	protected static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

	public UrlHandler(WebDriverManager webDriverManager, String url) {
		this.webDriverManager = webDriverManager;
		pageInfo = new PageInfo(url);
		pageInfo.register(new ImageType(url));
		pageInfo.register(new ConsoleType(url));
		pageInfo.setBrowserName(webDriverManager.getName());
	}

	@Override
	public void run() {
		try {
			for (DifferenceType type : pageInfo.getTypes()) {
				type.apply();
				type.close();
			}
			StreamUtils.writeImageInfo(pageInfo);
			if (AppConstants.BUILD_TYPE == BuildType.POST && (AppConstants.HAS_DIFF)) {
				PageInfo pageInfoPre = StreamUtils.readPageInfo(pageInfo.getPageUrl(), webDriverManager.getName());
				new Thread(new DiffGenerator(pageInfoPre, pageInfo)).start();
			}
		} catch (Exception ex) {
			logger.error("Unable to cature URL: " + pageInfo.getPageUrl());
		}

	}

}
