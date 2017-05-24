package com.sapient.unilever.d2.qa.dgt;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sapient.unilever.d2.qa.dgt.selenium.WebDriverManager;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Demo {
    protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
	try {
	    WebDriverManager mngr = new WebDriverManager();
	    mngr.setName("iPhone");
	    WebDriver driver = mngr.getiPhoneDriver();
	    driver.get("http://google.com");
	    Screenshot sc1 = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1500))
		    .takeScreenshot(driver);
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("document.querySelectorAll('input[name=\"q\"]')[0].style.border=\"2px green dotted\";");
	    Screenshot sc2 = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1500))
		    .takeScreenshot(driver);
	    ImageDiff diff = new ImageDiffer().makeDiff(sc1, sc2);
	    BufferedImage img = diff.getTransparentMarkedImage();
	    OutputStream pngStream = Files.newOutputStream(Paths.get("d:/sac.png"));
	    ImageIO.write(img, "png", pngStream);
	    pngStream.flush();
	    pngStream.close();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
