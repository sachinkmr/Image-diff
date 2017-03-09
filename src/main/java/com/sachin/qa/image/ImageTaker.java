package com.sachin.qa.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ImageTaker {
	private ImageInfo info;
	private WebDriver driver;
	protected static final Logger logger = LoggerFactory.getLogger(ImageTaker.class);

	public ImageTaker(ImageInfo info, WebDriver driver) {
		this.info = info;
		this.driver = driver;
	}

	public void capture() throws Exception {
		driver.navigate().to(info.getPageUrl());
		if (driver instanceof InternetExplorerDriver) {
			throw new Exception("IE driver is not supported");
		}
		new AShot().shootingStrategy(ShootingStrategies.viewportPasting(200)).takeScreenshot(driver);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			logger.error("waiting intruppted: ", e1);
		}
		Screenshot shot = new AShot().shootingStrategy(
				ShootingStrategies.viewportNonRetina(100, AppConstants.HEADER_PIXELS, AppConstants.FOOTER_PIXELS))
				.takeScreenshot(driver);
		try {
			if (driver instanceof ChromeDriver || driver instanceof PhantomJSDriver) {
				LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				List<String> list = new ArrayList<>();
				list.add("Logging console logs for: " + info.getPageUrl());
				list.add("Browser Type: " + info.getBrowserName());
				list.add("----------------------------------------------------------------------");
				for (LogEntry entry : logEntries) {
					list.add(entry.getLevel() + " : " + entry.getMessage());
				}
				String fileName = Base64
						.encodeBase64URLSafeString((info.getPageUrl() + info.getBrowserName()).getBytes());
				if (AppConstants.IS_DIFF) {
					FileUtils.writeLines(new File(new File(info.getImagePathNew()).getParentFile(), fileName + ".log"),
							"UTF-8", list);
				} else {
					FileUtils.writeLines(new File(new File(info.getImagePathOld()).getParentFile(), fileName + ".log"),
							"UTF-8", list);
				}
			}
		} catch (Exception e) {
			logger.warn("unable to read console log for: " + info.getPageUrl(), e);
		}
		try {
			if (AppConstants.IS_DIFF) {
				ImageIO.write(shot.getImage(), "png", Files.newOutputStream(Paths.get(info.getImagePathNew())));
			} else {
				ImageIO.write(shot.getImage(), "png", Files.newOutputStream(Paths.get(info.getImagePathOld())));
			}
		} catch (IOException e) {
			logger.error("Unable to Store Image: ", e);
		}
	}
}
