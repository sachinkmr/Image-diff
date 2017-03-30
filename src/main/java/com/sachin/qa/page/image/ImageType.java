package com.sachin.qa.page.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sachin.qa.app.AppConstants;
import com.sachin.qa.app.selenium.WebDriverManager;
import com.sachin.qa.page.Featurable;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ImageType extends Featurable {

	private static final long serialVersionUID = 1L;
	protected static transient final Logger logger = LoggerFactory.getLogger(ImageType.class);
	private transient Screenshot shot;

	public ImageType(String url, WebDriverManager webDriverManager) {
		super(url, ".png", webDriverManager);
		this.resourcePath = AppConstants.FOLDER + File.separator + "images" + File.separator + fileName;
		new File(this.resourcePath).getParentFile().mkdirs();
	}

	@Override
	public void apply() throws Exception {
		if (this.getWebDriver() instanceof InternetExplorerDriver) {
			throw new Exception("IE driver is not supported");
		}
		shot = new AShot().shootingStrategy(ShootingStrategies.viewportNonRetina(AppConstants.SCROLL_DELAY,
				AppConstants.HEADER_PIXELS, AppConstants.FOOTER_PIXELS)).takeScreenshot(this.getWebDriver());
	}

	@Override
	public void close() throws Exception {
		try {
			ImageIO.write(shot.getImage(), "png", Files.newOutputStream(Paths.get(this.resourcePath)));
			shot = null;
		} catch (IOException e) {
			logger.error("Unable to Store Image: " + url, e);
		}
	}

}
