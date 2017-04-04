package com.sachin.qa.app.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.BuildType;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.app.utils.StreamUtils;
import com.sachin.qa.page.Featurable;
import com.sachin.qa.page.PageInfo;
import com.sachin.qa.page.Scroller;
import com.sachin.qa.page.diff.Differentiator;
import com.sachin.qa.page.image.ImageDiffInfo;
import com.sachin.qa.page.image.ImageType;
import com.sachin.qa.page.js.JsDiffInfo;
import com.sachin.qa.page.js.JsType;

public class UrlHandler implements Runnable {
	private WebDriverManager webDriverManager;
	private PageInfo pageInfo;
	protected static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

	public UrlHandler(WebDriverManager webDriverManager, String url) {
		this.webDriverManager = webDriverManager;
		pageInfo = new PageInfo(url, webDriverManager.getName());
		pageInfo.register(new ImageType(url, webDriverManager));
		pageInfo.register(new JsType(url, webDriverManager));
	}

	@Override
	public void run() {
		Scroller.scrollPage(webDriverManager.getWebDriver(), pageInfo.getPageUrl());
		try {
			Thread.sleep(AppConstants.PAGE_WAIT);
			for (Featurable type : pageInfo.getTypes()) {
				type.apply();
				type.close();
			}
			StreamUtils.writePageInfo(pageInfo);
			if (AppConstants.BUILD_TYPE == BuildType.POST && AppConstants.HAS_DIFF) {
				PageInfo pageInfoPre = StreamUtils.readPageInfo(pageInfo.getPageUrl(), webDriverManager.getName());
				Differentiator differ = new Differentiator(pageInfoPre.getPageName().replaceAll(".info", ".diff"));
				if (AppConstants.IMAGE_DIFF)
					differ.register(new ImageDiffInfo(pageInfoPre, pageInfo));
				if (AppConstants.JS_DIFF)
					differ.register(new JsDiffInfo(pageInfoPre, pageInfo));
				new Thread(differ).start();
			}
		} catch (Exception ex) {
			logger.error("Unable to cature URL: " + pageInfo.getPageUrl());
		}
	}

}
