package com.sapient.unilever.d2.qa.dgt.page.image;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.AppConstants;
import com.sapient.unilever.d2.qa.dgt.page.Featurable;
import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;

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
		// shot = new
		// AShot().shootingStrategy(ShootingStrategies.viewportPasting(AppConstants.SCROLL_DELAY))
		// .takeScreenshot(this.getWebDriver());
	}

	@Override
	public void close() throws Exception {
		try {
			OutputStream stream = Files.newOutputStream(Paths.get(this.resourcePath));
			ImageIO.write(shot.getImage(), "png", stream);
			shot = null;
			stream.flush();
			stream.close();
		} catch (Exception e) {
			logger.error("Unable to Store Image: " + url, e);
		}
	}

}
