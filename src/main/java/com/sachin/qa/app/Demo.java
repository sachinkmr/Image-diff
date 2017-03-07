package com.sachin.qa.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.sachin.qa.image.ImageInfo;
import com.sachin.qa.image.diff.DiffFinder;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Demo {

	public static void main(String[] args) {
		// System.out.println(Hex.encodeHexString(
		// "http://liptonntea.com/asdasdasdasd/asdasdasdasda/sdasdasdasd/asdasdasdasd/asdasdasd/asd".getBytes()));
		// System.out.println(
		// "http://liptonntea.com/asdasdasdasd/asdasdasdasda/sdasdasdasd/asdasdasdasd/asdasdasd/asd1".hashCode());
		ImageInfo info = new ImageInfo("http://google.com");
		info.setDiffGiff("D:\\diff.gif");
		info.setDiffImage("D:\\diff.png");
		info.setImagePathOld("D:\\main1.png");
		info.setImagePathNew("D:\\main2.png");
		info.setName("2");
		WebDriver driver = new FirefoxDriver();
		driver.get("http://google.com");
		Screenshot one = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
		// JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("document.getElementById('sfdiv').outerHTML=''");
		Screenshot two = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
		driver.quit();
		try {
			ImageIO.write(two.getImage(), "png", Files.newOutputStream(Paths.get(info.getImagePathNew())));
			ImageIO.write(one.getImage(), "png", Files.newOutputStream(Paths.get(info.getImagePathOld())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(new DiffFinder(info)).start();

	}
}
