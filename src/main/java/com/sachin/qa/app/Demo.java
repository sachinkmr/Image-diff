package com.sachin.qa.app;

import java.awt.image.BufferedImage;

import org.openqa.selenium.firefox.FirefoxDriver;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Demo {

	public static void main(String[] args) {
	    new AShot()
	    .shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(new FirefoxDriver());
		BufferedImage d=new ImageDiffer().makeDiff(new AShot()
	    .shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(new FirefoxDriver()), new AShot()
	    .shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(new FirefoxDriver())).getMarkedImage();
	}
}
