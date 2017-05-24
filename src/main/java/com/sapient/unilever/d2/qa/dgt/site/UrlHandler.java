package com.sapient.unilever.d2.qa.dgt.site;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.BuildType;
import com.sapient.unilever.d2.qa.dgt.manager.ThreadManager;
import com.sapient.unilever.d2.qa.dgt.page.Featurable;
import com.sapient.unilever.d2.qa.dgt.page.PageInfo;
import com.sapient.unilever.d2.qa.dgt.page.diff.Differentiator;
import com.sapient.unilever.d2.qa.dgt.page.image.ImageDiffInfo;
import com.sapient.unilever.d2.qa.dgt.page.image.ImageType;
import com.sapient.unilever.d2.qa.dgt.page.js.JsDiffInfo;
import com.sapient.unilever.d2.qa.dgt.page.js.JsType;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;
import com.sapient.unilever.d2.qa.dgt.utils.StreamUtils;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class UrlHandler implements Runnable {
	private WebDriverManager webDriverManager;
	private PageInfo pageInfo;
	private String url;
	protected static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

	public UrlHandler(WebDriverManager webDriverManager, String url) {
		this.webDriverManager = webDriverManager;
		this.url = url;
		pageInfo = new PageInfo(url, webDriverManager.getName());
		if (AppConstants.IMAGE_DIFF)
			pageInfo.register(new ImageType(url, webDriverManager));
		if (AppConstants.JS_DIFF)
			pageInfo.register(new JsType(url, webDriverManager));
	}

	@Override
	public void run() {
		logger.info("Executing URL: " + pageInfo.getPageUrl());
		System.out.println("Executing URL: " + pageInfo.getPageUrl());
		if (AppConstants.SCROLLER)
			scrollPage();
		try {
			Thread.sleep(AppConstants.PAGE_WAIT);
			for (Featurable type : pageInfo.getTypes()) {
				type.apply();
				type.close();
			}
			StreamUtils.writePageInfo(pageInfo);
			if (AppConstants.BUILD_TYPE == BuildType.POST) {
				PageInfo pageInfoPre = StreamUtils.readPageInfo(pageInfo.getPageUrl(), webDriverManager.getName());
				Differentiator differ = new Differentiator(pageInfoPre.getPageName().replaceAll(".info", ".diff"));
				if (AppConstants.IMAGE_DIFF)
					differ.register(new ImageDiffInfo(pageInfoPre, pageInfo));
				if (AppConstants.JS_DIFF)
					differ.register(new JsDiffInfo(pageInfoPre, pageInfo));
				ThreadManager.dgtService.execute(differ);
			}
			// Thread.sleep(1000);
		} catch (org.openqa.selenium.TimeoutException e) {
			logger.error("Timeout in capturing URL: " + pageInfo.getPageUrl(), e);
		} catch (Exception ex) {
			logger.error("Unable to capture URL: " + pageInfo.getPageUrl(), ex);
		}
	}

	private void scrollPage() {
		try {
			WebDriver driver = webDriverManager.getWebDriver();
			driver.navigate().to(url);
			new AShot().shootingStrategy(ShootingStrategies.viewportPasting(AppConstants.SCROLL_DELAY))
					.takeScreenshot(driver);
		} catch (Exception e) {
			logger.info("Error in scrolling page: " + url, e);
		}
	}
}
