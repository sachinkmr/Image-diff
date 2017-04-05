package com.sapient.unilever.d2.qa.dgt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Demo {
	protected static final Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "servers/chromedriver.exe");
		System.setProperty("webdriver.ie.driver", "servers/IEDriverServer.exe");
		System.setProperty("webdriver.gecko.driver", "servers/geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "servers/MicrosoftWebDriver.exe");
		System.setProperty("phantomjs.binary.path", "servers/phantomjs.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://dove.com/us");
		Set<By> s = new HashSet<>();
		s.add(By.cssSelector("nav.o-navbar"));
		Screenshot shot = new AShot().ignoredElements(s)
				.shootingStrategy(ShootingStrategies.viewportNonRetina(1000, 0, 0)).takeScreenshot(driver);

		try {
			ImageIO.write(shot.getImage(), "png", Files.newOutputStream(Paths.get("D:\\img.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.quit();
	}
}
