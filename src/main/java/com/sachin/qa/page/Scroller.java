package com.sachin.qa.page;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Scroller {
	private PageInfo info;
	private WebDriver driver;
	protected static final Logger logger = LoggerFactory.getLogger(Scroller.class);

	public Scroller(PageInfo info, WebDriver driver) {
		this.info = info;
		this.driver = driver;
	}

	public void scrollPage() throws Exception {
		driver.navigate().to(info.getPageUrl());
		new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(driver);
	}
}
