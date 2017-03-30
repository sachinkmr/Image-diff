package com.sachin.qa.page;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Scroller {
	protected static final Logger logger = LoggerFactory.getLogger(Scroller.class);

	public static void scrollPage(WebDriver driver, String url) {
		try {
			driver.navigate().to(url);
			new AShot().shootingStrategy(ShootingStrategies.viewportPasting(AppConstants.SCROLL_DELAY))
					.takeScreenshot(driver);
		} catch (Exception e) {
			logger.debug("Error in scrolling page: " + url, e);
		}

	}
}
