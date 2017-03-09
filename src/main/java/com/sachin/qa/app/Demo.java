package com.sachin.qa.app;

import org.apache.commons.codec.binary.Base64;

public class Demo {

	public static void main(String[] args) {
		System.out.println();
		System.out.println(Base64.encodeBase64URLSafeString("Sachin".getBytes()));
		// ImageInfo info = new ImageInfo("http://google.com");
		// info.setDiffGiff("D:\\diff.gif");
		// info.setDiffImage("D:\\diff.png");
		// info.setImagePathOld("D:\\main1.png");
		// info.setImagePathNew("D:\\main2.png");
		// info.setName("2");
		// WebDriver driver = new FirefoxDriver();
		// driver.navigate().to("http://google.com");
		// Screenshot one = new
		// AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
		// // JavascriptExecutor js = (JavascriptExecutor) driver;
		// // js.executeScript("document.getElementById('sfdiv').outerHTML=''");
		// Screenshot two = new
		// AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
		// driver.quit();
		// try {
		// ImageIO.write(two.getImage(), "png",
		// Files.newOutputStream(Paths.get(info.getImagePathNew())));
		// ImageIO.write(one.getImage(), "png",
		// Files.newOutputStream(Paths.get(info.getImagePathOld())));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// new Thread(new DiffFinder(info)).start();

	}
}
