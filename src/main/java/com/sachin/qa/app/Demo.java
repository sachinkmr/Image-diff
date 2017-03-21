package com.sachin.qa.app;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Demo {

	public static void main(String[] args) {
		WebDriver driver = new FirefoxDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.store = [];");
		js.executeScript(
				"var oldf = console.log;console.log = function(){window.store.push(arguments);oldf.apply(console, arguments);};");
		driver.navigate().to("http://google.com");
		js.executeScript("console.log('sacacac')");

		try {
			System.out.println("1: " + js.executeScript("return window.store;"));
		} catch (Exception e) {
		}
		try {
			System.out.println("2: " + js.executeScript("return store;"));
		} catch (Exception e) {
		}

		driver.quit();

	}
}
